package com.playdata.adminservice.admin.service;

import com.playdata.adminservice.admin.dto.req.AdminLoginReqDto;
import com.playdata.adminservice.admin.dto.req.AdminSaveReqDto;
import com.playdata.adminservice.admin.dto.res.AdminEmailAuthResDto;
import com.playdata.adminservice.admin.dto.res.AdminLoginResDto;
import com.playdata.adminservice.admin.entity.Admin;
import com.playdata.adminservice.admin.repository.AdminRepository;
import com.playdata.adminservice.common.auth.JwtTokenProvider;
import com.playdata.adminservice.common.dto.CommonResDto;
import com.playdata.adminservice.common.enumeration.ErrorCode;
import com.playdata.adminservice.common.exception.CommonException;
import jakarta.mail.MessagingException;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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


    // 회원가입 (총 관리자 DB 넣기용)
    public CommonResDto create(AdminSaveReqDto adminSaveReqDto) {

        String email = adminSaveReqDto.getEmail();
        Optional<Admin> findAdmin = adminRepository.findByEmail(email);

        if (findAdmin.isPresent()) {
            throw new CommonException(ErrorCode.DUPLICATED_DATA, "이미 존재하는 이메일입니다.");
        }
        // 이메일 중복 검증 후
        // 비밀번호 인코딩
        String password = adminSaveReqDto.getPassword();

        // DB에 저장을 위해 패스워드 인코딩
        String encodedPassword = passwordEncoder.encode(password);

        // 부가적인 정보를 담아서 admin을 DB에 저장
        Admin createdAdmin = adminSaveReqDto.toEntity(encodedPassword);
        // DB에 저장
        adminRepository.save(createdAdmin);

        CommonResDto resDto = new CommonResDto(HttpStatus.CREATED, "회원가입에 성공하였습니다", true);
        return resDto;

    }


    // 로그인
    public CommonResDto login(AdminLoginReqDto adminLoginReqDto) {

        // Admin email 조회
        Optional<Admin> findAdmin = adminRepository.findByEmail(adminLoginReqDto.getEmail());

        if(!findAdmin.isPresent()) { // email 정보가 없다면 회원가입 x
            throw new EntityNotFoundException("회원가입이 되지 않은 이메일입니다.");
        } else  {
            // 위 findAdmin 에서 조회를 하고 꺼내서 아래 인코딩된 비밀번호를 찾아야함
            Admin foundAdmin = findAdmin.get();
            String password = adminLoginReqDto.getPassword();

            // 탈퇴한 관리자면 에러
            if (!findAdmin.get().isActive()) {
                throw new CommonException(ErrorCode.ACCOUNT_DISABLED);
            }
            // 비밀번호가 일치 하지 않는 경우
            // password = 날 것의 비밀번호, foundAdmin.getPassword() = 인코딩된 비밀번호
            if(!passwordEncoder.matches(password, foundAdmin.getPassword())) {
                throw new CommonException(ErrorCode.INVALID_PASSWORD);
            } else {
                return sendVerifyEmailCode(adminLoginReqDto.getEmail());
            }
        }
    }

    // 이메일 인증번호 검증 로직
    public CommonResDto verifyCode(@Valid AdminEmailAuthResDto authResDto) {
        String email = authResDto.getEmail();
        String authCode = authResDto.getAuthCode();

        // redis에 저장된 인증 코드 조회
        String key = VERIFICATION_CODE_KEY + email;
        Object foundCode = redisTemplate.opsForValue().get(key);
        // 인증 코드 유효시간이 만료된 경우
        if(foundCode == null) {
            throw new CommonException(ErrorCode.BAD_REQUEST);
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
            }
//            // 인증 횟수 차감하여 프론트로 메시지 전송
//            int remainingAttempt = 3 - attemptCount;
            throw new CommonException(ErrorCode.BAD_REQUEST);
        }

        log.info("이메일 인증 성공!, email: {}", email);

        // 인증 완료 했기 때문에, redis에 있는 인증 관련 데이터를 삭제하자.
        redisTemplate.delete(key);

        // admin 정보 가져오기
        Admin admin = adminRepository.findByEmail(email).orElseThrow(() -> new CommonException(ErrorCode.BAD_REQUEST));

        // 토큰 생성
        String token = jwtTokenProvider.createToken(admin.getEmail(), admin.getRole(), admin.getAdminId());

        // token과 email을 화면단으로 리턴
        return new CommonResDto(HttpStatus.OK,
                "로그인에 성공하였습니다.",
                new AdminLoginResDto(admin.getEmail(), admin.getName(), admin.getRole(), token));
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


}
