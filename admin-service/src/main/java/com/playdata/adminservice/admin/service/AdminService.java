package com.playdata.adminservice.admin.service;

import com.playdata.adminservice.admin.dto.AdminSearchDto;
import com.playdata.adminservice.admin.dto.req.*;
import com.playdata.adminservice.admin.dto.res.AdminEmailAuthResDto;
import com.playdata.adminservice.admin.dto.res.AdminListResDto;
import com.playdata.adminservice.admin.dto.res.AdminLoginResDto;
import com.playdata.adminservice.admin.entity.Admin;
import com.playdata.adminservice.admin.entity.Role;
import com.playdata.adminservice.admin.repository.AdminRepository;
import com.playdata.adminservice.common.auth.JwtTokenProvider;
import com.playdata.adminservice.common.auth.TokenUserInfo;
import com.playdata.adminservice.common.dto.CommonResDto;
import com.playdata.adminservice.common.enumeration.ErrorCode;
import com.playdata.adminservice.common.exception.CommonException;
import jakarta.mail.MessagingException;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class AdminService {

    private final AdminRepository adminRepository;

    // 비밀번호 인코딩용
    private final PasswordEncoder passwordEncoder;

    // 로그인 토큰 발급용
    private final JwtTokenProvider jwtTokenProvider;

    // 로그인 인증 이메일용
    private final MaIlSenderService mailSenderService;

    // Redis 저장용 redisTemplate
    private final RedisTemplate<String, Object> redisTemplate;

    // Redis key 상수
    // 인증 코드 저장용
    private static final String VERIFICATION_CODE_KEY = "admin_email_verify:code:";

    // 인증 코드 발급 횟수
    private static final String VERIFICATION_ATTEMPT_KEY = "admin_email_verify:attempt:";

    // 인증 코드 발송 금지 상태
    private static final String VERIFICATION_BLOCK_KEY = "admin_email_verify:block:";


    /**
     *
     * @param adminSaveReqDto
     * @return
     */
    // 총 관리자 회원가입
    public CommonResDto create(AdminSaveReqDto adminSaveReqDto) {

        String email = adminSaveReqDto.getEmail();
        Optional<Admin> findAdmin = adminRepository.findByEmail(email);

        // 이메일 중복 검증 로직
        if (findAdmin.isPresent()) {
            throw new CommonException(ErrorCode.DUPLICATED_DATA, "이미 존재하는 이메일입니다.");
        }

        // 부가적인 정보를 담아서 Admin을 던짐
        Admin createdAdmin = adminSaveReqDto.toEntity(passwordEncoder);
      
        // DB에 저장
        adminRepository.save(createdAdmin);

        return new CommonResDto(HttpStatus.CREATED, "회원가입에 성공하였습니다", true);

    }

    /**
     *
     * @param adminSaveReqDto
     * @return
     */
    // 관리자 생성
    public CommonResDto plus(AdminSaveReqDto adminSaveReqDto) {

        Optional<Admin> findAdmin = adminRepository.findByEmail(adminSaveReqDto.getEmail());

        // 이메일 중복 검증
        if (findAdmin.isPresent()) {
            throw new CommonException(ErrorCode.DUPLICATED_DATA, "이미 존재하는 이메일 입니다.");
        }

        // 부가적인 정보를 담아서 Admin을 던짐
        Admin PlusAdmin = adminSaveReqDto.toEntity(passwordEncoder);

        // DB에 저장
        adminRepository.save(PlusAdmin);

        CommonResDto resDto = new CommonResDto(HttpStatus.CREATED, "관리자 생성에 성공하였습니다.", true);
        return resDto;
    }

    /**
     *
     * @param adminLoginReqDto
     * @return
     */
    // 로그인
    public CommonResDto login(AdminLoginReqDto adminLoginReqDto) {

        // Admin email 조회
        Optional<Admin> findAdmin = adminRepository.findByEmail(adminLoginReqDto.getEmail());

        if (!findAdmin.isPresent()) { // email 정보가 없다면 회원가입 x
            throw new CommonException(ErrorCode.ACCOUNT_NOT_FOUND);
        } else  {
            // 위 findAdmin 에서 조회를 하고 꺼내서 아래 인코딩된 비밀번호를 찾아야함
            Admin foundAdmin = findAdmin.get();
            String password = adminLoginReqDto.getPassword();

            // 탈퇴한 관리자면 에러
            if (!findAdmin.get().isActive()) {
                throw new CommonException(ErrorCode.ACCOUNT_DISABLED);
            }
            // 비밀번호가 일치 하지 않는 경우
            if(!passwordEncoder.matches(password, foundAdmin.getPassword())) {
                throw new CommonException(ErrorCode.INVALID_PASSWORD);
            } else {
                if (foundAdmin.getIsFirst()) {
                    // 토큰 생성
                    String token = jwtTokenProvider.createToken(foundAdmin.getEmail(), foundAdmin.getRole(), foundAdmin.getAdminId());

                    // token과 email을 화면단으로 리턴
                    return new CommonResDto(HttpStatus.OK,
                            "로그인에 성공하였습니다.",
                            new AdminLoginResDto(foundAdmin.getEmail(), foundAdmin.getName(),
                                    foundAdmin.getRole(), token, foundAdmin.getIsFirst()));
                }
                return sendVerifyEmailCode(adminLoginReqDto.getEmail());
            }
        }
    }
  
    /**
     *
     * @param email
     * @return
     */
    // 로그인 이메일의 유효성을 확인하기 위해 인증번호를 발송하는 로직
    // 이메일 인증번호 발송 로직
    public CommonResDto sendVerifyEmailCode(String email) {

        // 차단 상태 확인
        // 이메일 인증번호 발송을 3회 이상한 경우
        if(isBlocked(email)){
            throw new CommonException(ErrorCode.ACCOUNT_LOCKED, "현재 인증 이메일 발송이 차단된 이메일입니다.");
        }
        Optional<Admin> foundEmail =
                adminRepository.findByEmail(email);
        // 이미 존재하는 이메일인 경우 -> 회원가입 불가
        if (foundEmail.isEmpty()) {
            // 이미 존재하는 이메일이라는 에러를 발생 -> controller가 이 에러를 처리
            throw new CommonException(ErrorCode.ACCOUNT_NOT_FOUND);
        }

        // 이메일로 인증번호 발송
        String authNum = null;
        try {
            authNum = mailSenderService.loginMain(email);
            // 인증 코드를 redis에 저장하자
            String key = VERIFICATION_CODE_KEY + email;
            // 인증코드의 유효 시간은 5분으로 지정
            redisTemplate.opsForValue().set(key, authNum, Duration.ofMinutes(5));
        } catch (MessagingException e) {
            throw new CommonException(ErrorCode.MAIL_SERVER_ERROR);
        }

        // 나중에 더미데이터를 편하게 넣기 위해서 인증번호를 로그로 남기기 위함
        // 실제 서비스에서는 아래의 return문에 authNum을 삭제해야함.
        return new CommonResDto(HttpStatus.OK, "회원가입 인증코드가 이메일로 발송되었습니다.", authNum);

    }

    /**
     *
     * @param authResDto
     * @return
     */
    // 로그인 이메일 2차 인증 검증
    public CommonResDto loginVerifyCode(@Valid AdminEmailAuthResDto authResDto) {

        // 이메일 검증 로직 호출
        CommonResDto resDto = verifyEmailCode(authResDto);

        if (!(boolean) resDto.getResult()) {
            throw new CommonException(ErrorCode.INVALID_PASSWORD);
        }

        // admin 정보 가져오기
        Admin admin = adminRepository.findByEmail(authResDto.getEmail()).orElseThrow(() -> new CommonException(ErrorCode.BAD_REQUEST));

        // 토큰 생성
        String token = jwtTokenProvider.createToken(admin.getEmail(), admin.getRole(), admin.getAdminId());

        // token과 email을 화면단으로 리턴
        return new CommonResDto(HttpStatus.OK,
                "로그인에 성공하였습니다.",
                new AdminLoginResDto(admin.getEmail(),admin.getName(), admin.getRole(), token, admin.getIsFirst()));
    }

    /**
     *
     * @param tokenUserInfo
     * @param newEmail
     * @return
     */
    // 이메일 변경 요청
    public CommonResDto modifyEmail(TokenUserInfo tokenUserInfo, String newEmail) {

        Optional<Admin> findAdmin = adminRepository.findByEmail(newEmail);

        // 변경한 이메일이 사용 중인 이메일인지 검증
        if (findAdmin.isPresent()) {
            throw new CommonException(ErrorCode.DUPLICATED_DATA, "이미 존재하는 이메일 입니다.");
        }
        String authCode = sendEmailAuthCode(newEmail, "MODIFY");

        // 나중에 더미데이터를 편하게 넣기 위해서 인증번호를 로그로 남기기 위함
        // 실제 서비스에서는 아래의 return 문에 authNum 을 삭제해야함.
        return new CommonResDto(HttpStatus.OK, "인증코드가 새로운 이메일로 발송되었습니다.", authCode);
    }

    /**
     *
     * @param authResDto
     * @param userInfo
     * @return
     */
    // 이메일 변경 요청 검증
    public CommonResDto verifyAdminNewEmail(@Valid AdminEmailAuthResDto authResDto, TokenUserInfo userInfo) {

        // 이메일 검증 로직 호출
        CommonResDto resDto =  verifyEmailCode(authResDto);

        // userInfo 에서 관리자 Id 조회
        Optional<Admin> findAdmin = adminRepository.findById(userInfo.getAdminId());

        // 변경할 관리자가 있는지, 활성화 상태인지 검증
        if (!findAdmin.isPresent() || !findAdmin.get().isActive()) {
            throw new CommonException(ErrorCode.ACCOUNT_NOT_FOUND);
        }

        // 이메일 변경 요청 사용자의 유효성 확인 후
        // 해당 사용자의 이메일 변경 및 저장
        Admin admin = findAdmin.get();

        // entity 에서 수정 생성자 호출
        admin.modifyEmail(authResDto.getEmail());
        
        // isFirst = false로 변경
        admin.changeIsFirst();

        // DB 저장
        adminRepository.save(admin);

        return resDto;
    }

    /**
     *
     * @param email
     * @return
     */
    // 비밀번호 변경 요청
    public CommonResDto modifyPasswordReq(String email) {

        Optional<Admin> findAdmin = adminRepository.findByEmail(email);

        // 계정이 존재하고 활성화 상태인지 조회
        if (!findAdmin.isPresent() ||  !findAdmin.get().isActive()) {
            throw new CommonException(ErrorCode.ACCOUNT_NOT_FOUND);
        }
        // 이메일 발송 로직 호출
        String authCode = sendEmailAuthCode(email, "MODIFY");

        // 추후 배포 시 인증코드 제거 후 리턴
        return new CommonResDto(HttpStatus.OK, "인증 코드가 이메일로 전송되었습니다.", authCode);
    }

    /**
     *
     * @param userInfo
     * @param authReqDto
     * @return
     */
    // 비밀번호 변경 요청 검증
    public CommonResDto verifyNewPassword(TokenUserInfo userInfo, AdminPasswordAuthReqDto authReqDto) {

        // 기존 이메일 값과, 인증번호 값 받기
        AdminEmailAuthResDto required = new AdminEmailAuthResDto(userInfo.getEmail(), authReqDto.getAuthCode());

        // 인증번호 검증 로직 호출
        CommonResDto resDto = verifyEmailCode(required);

        // HttpStatus 값이 false 일 때 에러 = 검증 실패
        if (!(boolean) resDto.getResult()) {
            throw new CommonException(ErrorCode.INVALID_AUTH_CODE);
        }

        return resDto;
    }

    /**
     *
     * @param userInfo
     * @param modifyReqDto
     * @return
     */
    // 비밀번호 변경
    public CommonResDto modifyPassword(TokenUserInfo userInfo, AdminPasswordModifyReqDto modifyReqDto) {

        Optional<Admin> findAdmin = adminRepository.findByEmail(userInfo.getEmail());

        // 계정이 존재하고 활성화 상태인지 조회
        if (!findAdmin.isPresent() || !findAdmin.get().isActive()) {
            throw new CommonException(ErrorCode.ACCOUNT_NOT_FOUND, "회원이 존재하지 않습니다.");
        }

        Admin admin = findAdmin.get();

        // 변경된 이메일 인코딩
        Admin encoder = admin.modifyPassword(passwordEncoder, modifyReqDto.getPassword());

        // DB에 저장
        adminRepository.save(encoder);

        return new CommonResDto(HttpStatus.OK, "비밀번호를 변경 하였습니다. 다시 로그인 해주세요", true);
    }

    /**
     *
     * @param userInfo
     * @param modifyReqDto
     * @return
     */
    // 비밀번호, 이메일 외의 정보 수정
    public CommonResDto myPageModify(TokenUserInfo userInfo, AdminModifyReqDto modifyReqDto) {

        Optional<Admin> findAdmin = adminRepository.findById(userInfo.getAdminId());

        // 관리자가 존재하는지, 활성화 상태인지 검증
        if (!findAdmin.isPresent() || !findAdmin.get().isActive()) {
            throw new CommonException(ErrorCode.UNKNOWN_HOST, "회원정보가 없습니다.");
        }

        Admin admin = findAdmin.get();

        // 마이페이지에 등록 되어 있는 데이터와 동일한 데이터로 변경 시도 시 예외
        if (modifyReqDto.getName().equals(findAdmin.get().getName()) ||
                modifyReqDto.getPhone().equals(findAdmin.get().getPhone())) {
            throw new CommonException(ErrorCode.DUPLICATED_DATA);
        }

        admin.modifyMyPage(modifyReqDto);
        adminRepository.save(admin);
        return new CommonResDto(HttpStatus.OK, "수정에 성공하였습니다.", true);
    }

    /**
     *
     * @param adminSearchDto
     * @param pageable
     * @return
     */
    // 관리자 목록 조회
    public Page<AdminListResDto> adminList(AdminSearchDto adminSearchDto, Pageable pageable) {

        Page<Admin> adminList = adminRepository.findList(adminSearchDto, pageable);

        // Entity → DTO 변환
        return adminList.map(admin ->
                AdminListResDto.builder()
                        .admin(admin)
                        .build()
        );
    }

    /**
     *
     * @param adminRoleModifyReqDto
     * @return
     */
    // 총 관리자가 타 관리자 권한, 활성화 여부 수정
    public CommonResDto roleModify(AdminRoleModifyReqDto adminRoleModifyReqDto) {

        Admin findAdmin = adminRepository.findById(adminRoleModifyReqDto.getAdminId())
                .orElseThrow(() -> new CommonException(ErrorCode.UNKNOWN_HOST, "변경할 관리자를 찾을 수 없습니다."));

        // 권한 변경
        findAdmin.changeRole(adminRoleModifyReqDto.getRole());

        // 활성화 상태 변경
        findAdmin.changeActive(adminRoleModifyReqDto.getActive());

        adminRepository.save(findAdmin);




        return new CommonResDto(HttpStatus.OK, "권한/활성화 상태가 수정되었습니다.", true);
    }

    /**
     *
     * @param authResDto  --> email, authCode (인증번호)
     * @return
     */
    // 이메일로 발송된 인증코드 검증 로직
    private CommonResDto verifyEmailCode(AdminEmailAuthResDto authResDto) {

        String email = authResDto.getEmail();
        String authCode = authResDto.getAuthCode();

        // redis에 저장된 인증 코드 조회
        String key = VERIFICATION_CODE_KEY + email;
        Object foundCode = redisTemplate.opsForValue().get(key);
        // 인증 코드 유효시간이 만료된 경우
        if(foundCode == null) {
            throw new CommonException(ErrorCode.BAD_REQUEST);
            //throw new CommonException(ErrorCode.EXPIRED_AUTH_CODE);
        }

        // 인증 시도 횟수 증가
        int attemptCount = incrementAttemptCount(email);

        // 조회한 코드와 사용자가 입력한 코드가 일치한 지 검증
        if(!foundCode.toString().equals(authCode)) {
            // 인증 코드를 틀린 경우
            if(attemptCount >= 3){
                // 최대 시도 횟수 초과 시 해당 이메일 인증 차단
                blockUser(email);
                throw new CommonException(ErrorCode.BAD_REQUEST);
                // throw new CommonException(ErrorCode.ACCOUNT_LOCKED, "현재 인증 이메일 발송이 차단된 이메일입니다.");
            }
            // 인증 횟수 차감하여 프론트로 메시지 전송
            int remainingAttempt = 3 - attemptCount;
            throw new CommonException(ErrorCode.BAD_REQUEST);
            /*throw new CommonException(ErrorCode.INVALID_AUTH_CODE,
                    (String.format("인증코드가 틀렸습니다. 인증 기회는 %d회 남았습니다.", remainingAttempt)));*/
        }

        log.info("이메일 인증 성공!, email: {}", email);

        // 인증 완료 했기 때문에, redis에 있는 인증 관련 데이터를 삭제하자.
        redisTemplate.delete(key);

        return new CommonResDto(HttpStatus.OK, "인증되었습니다.", true);
    }

    /**
     *
     * @param email
     * @return
     */
    // 인증번호를 3회 이상 발송시킨 이메일인지 확인 여부
    private boolean isBlocked(String email) {
        // redis key 생성
        String key = VERIFICATION_BLOCK_KEY + email;
        // 현재 해당 이메일의 값이 존재하는지 -> 있으면 block 상태
        return redisTemplate.hasKey(key);
    }

    /**
     *
     * @param email
     */
    // 인증번호를 30분동안 3회이상 발송하지 못하게 하기 위한 로직
    private void blockUser(String email) {
        // redis key 생성
        String key = VERIFICATION_BLOCK_KEY + email;
        // 30분동안 해당 이메일의 key 값이 살아있도록 설정
        redisTemplate.opsForValue().set(key, "blocked", Duration.ofMinutes(30));
    }

    /**
     *
     * @param email
     * @return
     */
    // 이메일 발송을 요청하게 되면, redis에 있는 발송횟수 값을 하나 늘림.
    private int incrementAttemptCount(String email) {

        // redis key 생성
        String key = VERIFICATION_ATTEMPT_KEY + email;
        // redis에 있는 해당 email의 값 확인
        Object obj = redisTemplate.opsForValue().get(key);

        // 발송 횟수를 하나 늘려서 다시 redis에 저장
        int count = (obj != null) ? Integer.parseInt(obj.toString()) + 1 : 1;
        redisTemplate.opsForValue().set(key, String.valueOf(count), Duration.ofMinutes(1));

        return count;
    }

    /**
     *
     * @param email
     * @param occasion  --> 회원가입 또는 개인정보 변경 여부
     * @return
     */
    // 인증코드 전송 및 redis에 해당 키값 저장을 담당하는 메소드
    private String sendEmailAuthCode(String email, String occasion) {
        String authNum;
        // 이메일 전송만을 담당하는 객체를 이용해서 이메일 로직 작성.
        try {
            // 개인정보 변경용 이메일 전송
            if (occasion.equals("MODIFY")) {
                authNum = mailSenderService.sendAuthCode(email);
            }
            else if (occasion.equals("FORGET")) {
                authNum = mailSenderService.sendAuthCodeForget(email);
            }
            else if (occasion.equals("NEW")) {
                authNum = mailSenderService.sendNewPasswordForget(email);
                log.info(authNum);
            } else {
                authNum = "";
                throw new CommonException(ErrorCode.BAD_REQUEST);
            }
        } catch (MessagingException e) {
            log.info(e.getMessage());
            throw new CommonException(ErrorCode.FILE_SERVER_ERROR);
        }

        // 인증 코드를 redis에 저장하자
        String key = VERIFICATION_CODE_KEY + email;
        // 인증코드의 유효 시간은 5분으로 지정
        redisTemplate.opsForValue().set(key, authNum, Duration.ofMinutes(5));
        return authNum;
    }
}
