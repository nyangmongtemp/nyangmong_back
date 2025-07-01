package com.playdata.userservice.user.service;

import com.playdata.userservice.common.auth.JwtTokenProvider;
import com.playdata.userservice.common.auth.TokenUserInfo;
import com.playdata.userservice.common.dto.CommonResDto;
import com.playdata.userservice.user.dto.*;
import com.playdata.userservice.user.entity.User;
import com.playdata.userservice.user.repository.UserRepository;
import jakarta.mail.MessagingException;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.time.Duration;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {
    
    // 이메일 전송 서비스
    private final MailSenderService mailSenderService;

    // 비밀번호 인코딩용
    private final PasswordEncoder passwordEncoder;
    
    private final UserRepository userRepository;

    // 로그인 토큰 발급용
    private final JwtTokenProvider jwtTokenProvider;

    // Redis 저장용 redisTemplate
    private final RedisTemplate<String, Object> redisTemplate;

    // Redis key 상수
    private static final String VERIFICATION_CODE_KEY = "email_verify:code:";
    private static final String VERIFICATION_ATTEMPT_KEY = "email_verify:attempt:";
    private static final String VERIFICATION_BLOCK_KEY = "email_verify:block:";
    
    // 이미지 저장 경로 --> 추후에 yml에 있는 주소를 s3 주소로 바꿀 것
    @Value("${imagePath.url}")
    private String profileImageSaveUrl;

    
    // 회원 가입
    public CommonResDto userCreate(UserSaveReqDto userSaveReqDto) {

        String email = userSaveReqDto.getEmail();
        Optional<User> foundByEmail = userRepository.findByEmail(email);
        // 회원가입을 요청한 이메일로 이미 가입한 회원정보가 있는 경우
        if (foundByEmail.isPresent()) {
            // 회원가입 실패
            throw new IllegalArgumentException("이미 존재하는 이메일입니다.");
        }

        // 이메일 유효성 검증을 통과한 경우

        // DB에 저장하기 위해 비밀번호 인코딩
        String password = userSaveReqDto.getPassword();

        // 이미지를 지정한 경로에 저장
        String profileImagePath = setProfileImage(userSaveReqDto.getProfileImage());
        
        // DB에 저장을 위해 패스워드 인코딩
        String encodedPassword = passwordEncoder.encode(password);
        // 부가적인 정보를 담아서 User를 DB에 저장
        User createdUser = userSaveReqDto.toEntity(encodedPassword, profileImagePath);
        userRepository.save(createdUser);

        CommonResDto resDto = new CommonResDto(HttpStatus.CREATED, "회원가입에 성공하였습니다", true);
        return resDto;
    }

    // 프로필 이미지를 저장하는 로직
    private String setProfileImage(MultipartFile imageFile) {
        String profileImagePath = null;

        // profile image 저장 경로
        if (imageFile != null && !imageFile.isEmpty()) {
            try {
                // 로컬 저장 경로 (예: C:/uploads/profile 또는 /home/user/images/profile)
                String originalFilename = imageFile.getOriginalFilename();
                String fileName = UUID.randomUUID() + "_" + originalFilename;

                File dir = new File(profileImageSaveUrl);
                if (!dir.exists()) dir.mkdirs(); // 디렉토리 없으면 생성

                File dest = new File(profileImageSaveUrl, fileName);
                imageFile.transferTo(dest);

                profileImagePath = fileName; // 저장된 상대 경로만 DB에 넣음
            } catch (IOException e) {
                // 저장 실패 처리
                e.printStackTrace();
                throw new RuntimeException("이미지 저장 중 오류가 발생하였습니다.");
            }
        }
        return profileImagePath;
    }

    // 로그인 로직
    public CommonResDto login(UserLoginReqDto userLoginReqDto) {

        Optional<User> foundUser = userRepository.findByEmail(userLoginReqDto.getEmail());
        // 로그인 요청을 보낸 이메일이 DB에 존재하지 않는 경우
        if(!foundUser.isPresent()) {
            throw new EntityNotFoundException("회원가입이 되지 않은 이메일입니다.");
        }
        else {
            String pw = foundUser.get().getPassword();

            // 비밀번호가 일치 하지 않는 경우
            if(!passwordEncoder.matches(userLoginReqDto.getPassword(), pw)) {
                throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
            }
            else{
                String token = jwtTokenProvider.createToken(foundUser.get().getEmail(), "USER");
                // 로그인 성공
                return new CommonResDto(HttpStatus.OK, "로그인에 성공하였습니다.", token);
            }
        }

    }

    // 이메일 인증번호 발송 로직
    public CommonResDto sendVerifyEmailCode(String email) {

        // 차단 상태 확인
        if(isBlocked(email)){
            throw new IllegalArgumentException("현재 인증코드 발송이 제한된 이메일입니다.");
        }
        Optional<User> foundEmail =
                userRepository.findByEmail(email);
        // 이미 존재하는 이메일인 경우 -> 회원가입 불가
        if (foundEmail.isPresent()) {
            // 이미 존재하는 이메일이라는 에러를 발생 -> controller가 이 에러를 처리
            throw new IllegalArgumentException("이미 존재하는 이메일입니다.");
        }

        String authNum = sendEmailAuthCode(email);

        // 나중에 더미데이터를 편하게 넣기 위해서 인증번호를 로그로 남기기 위함
        // 실제 서비스에서는 아래의 return문에 authNum을 삭제해야함.
        return new CommonResDto(HttpStatus.OK, "회원가입 인증코드가 이메일로 발송되었습니다.", authNum);

    }

    // 인증코드 전송 및 redis에 해당 키값 저장을 담당하는 메소드
    private String sendEmailAuthCode(String email) {
        String authNum;
        // 이메일 전송만을 담당하는 객체를 이용해서 이메일 로직 작성.
        try {
            authNum = mailSenderService.joinMain(email);
        } catch (MessagingException e) {
            log.info(e.getMessage());
            throw new RuntimeException("이메일 전송 과정 중 문제 발생");
        }

        // 인증 코드를 redis에 저장하자
        String key = VERIFICATION_CODE_KEY + email;
        redisTemplate.opsForValue().set(key, authNum, Duration.ofMinutes(5));
        return authNum;
    }

    // 인증번호를 3회 이상 발송시킨 이메일인지 확인 여부
    private boolean isBlocked(String email) {
        String key = VERIFICATION_BLOCK_KEY + email;
        return redisTemplate.hasKey(key);
    }
    
    // 인증번호를 30분동안 3회이상 발송하지 못하게 하기 위한 로직
    private void blockUser(String email) {

        String key = VERIFICATION_BLOCK_KEY + email;
        redisTemplate.opsForValue().set(key, "blocked", Duration.ofMinutes(30));

    }
    
    // 이메일 발송을 요청하게 되면, redis에 있는 발송횟수 값을 하나 늘림.
    private int incrementAttemptCount(String email) {

        String key = VERIFICATION_ATTEMPT_KEY + email;
        Object obj = redisTemplate.opsForValue().get(key);

        int count = (obj != null) ? Integer.parseInt(obj.toString()) + 1 : 1;
        redisTemplate.opsForValue().set(key, String.valueOf(count), Duration.ofMinutes(1));

        return count;
    }

    // 이메일로 발송된 인증코드 검증 로직
    public CommonResDto verifyEmailCode(UserEmailAuthResDto authResDto) {

        String email = authResDto.getEmail();
        String authCode = authResDto.getAuthCode();

        // redis에 저장된 인증 코드 조회
        String key = VERIFICATION_CODE_KEY + email;
        Object foundCode = redisTemplate.opsForValue().get(key);
        // 인증 코드 유효시간이 만료된 경우
        if(foundCode == null) {
            throw new IllegalArgumentException("인증 코드 유효시간이 만료되었습니다.");
        }

        // 인증 시도 횟수 증가
        int attemptCount = incrementAttemptCount(email);

        // 조회한 코드와 사용자가 입력한 코드가 일치한 지 검증
        if(!foundCode.toString().equals(authCode)) {
            // 인증 코드를 틀린 경우
            if(attemptCount >= 3){
                // 최대 시도 횟수 초과 시 해당 이메일 인증 차단
                blockUser(email);
                throw new IllegalArgumentException("30분동안 인증 코드 발송이 제한되었습니다.");
            }
            int remainingAttempt = 3 - attemptCount;
            throw new IllegalArgumentException(String.format("인증코드가 틀렸습니다. 인증 기회는 %d회 남았습니다.", remainingAttempt));
        }

        log.info("이메일 인증 성공!, email: {}", email);

        // 인증 완료 했기 때문에, redis에 있는 인증 관련 데이터를 삭제하자.
        redisTemplate.delete(key);

        return new CommonResDto(HttpStatus.OK, "인증되었습니다.", true);
    }

    // 프사, 닉네임, 주소, 전화번호를 변경하는 로직
    public void modiUserCommonInfo(TokenUserInfo userInfo, UserInfoModiReqDto modiDto) {

        String email = userInfo.getEmail();

        User foundUser = userRepository.findByEmail(email)
                .orElseThrow(() -> new EntityNotFoundException("존재하지 않는 사용자입니다."));

        String newProfileImage = setProfileImage(modiDto.getProfileImage());

        foundUser.modifyCommonUserInfo(modiDto, newProfileImage);

        userRepository.save(foundUser);
    }

    // 이메일 변경을 요청하여 검사 후 인증 이메일 전송 로직
    public CommonResDto modiUserEmail(String newEmail, TokenUserInfo userInfo) {

        Optional<User> byEmail = userRepository.findByEmail(userInfo.getEmail());
        // 이메일 변경 요청을 보낸 사용자가 DB에 이미 존재하는 경우
        if(byEmail.isPresent()) {
            throw new EntityNotFoundException("해당 이메일의 사용자가 이미 존재합니다.");
        }
        String authCode = sendEmailAuthCode(newEmail);

        // 나중에 더미데이터를 편하게 넣기 위해서 인증번호를 로그로 남기기 위함
        // 실제 서비스에서는 아래의 return문에 authNum을 삭제해야함.
        return new CommonResDto(HttpStatus.OK, "인증코드가 새로운 이메일로 발송되었습니다.", authCode);
    }

    // 이메일 변경 요청의 인증 여부를 확인하는 메소드
    // 완료되면 바로 이메일을 변경함.
    public CommonResDto verifyUserNewEmail(UserEmailAuthResDto authResDto, TokenUserInfo userInfo) {

        CommonResDto resDto = verifyEmailCode(authResDto);

        Optional<User> foundUser = userRepository.findByEmail(userInfo.getEmail());
        if(foundUser.isPresent()) {
            throw new EntityNotFoundException("이미 가입된 이메일입니다.");
        }
        User user = foundUser.get();
        user.modifyEmail(authResDto.getEmail());
        userRepository.save(user);

        return resDto;
    }

    // 비밀번호 변경 요청 인증 코드를 확인해주는 로직
    public CommonResDto sendEmailAuthCodeNewPw(String email) {

        Optional<User> byEmail = userRepository.findByEmail(email);
        // 비밀번호 변경 요청을 보낸 유저가 DB에 없는 경우
        if(!byEmail.isPresent()) {
            throw new EntityNotFoundException("해당 이메일의 사용자가 없습니다.");
        }
        String code = sendEmailAuthCode(email);

        // 실제 배포 환경에서는 인증 코드는 빼고 리턴해야 함.
        return new CommonResDto(HttpStatus.OK, "인증 코드가 이메일로 전송되었습니다.", code);
    }

    // 실제로 비밀번호를 변경해주는 로직
    public CommonResDto modifyNewPassword(String email, UserPasswordModiReqDto reqDto) {

        Optional<User> byEmail = userRepository.findByEmail(email);
        // 비밀번호를 변경하려는 유저가 DB에 없는 경우
        if(!byEmail.isPresent()) {
            throw new EntityNotFoundException("해당 이메일의 사용자가 없습니다.");
        }

        User user = byEmail.get();
        String newEncodedPassword = passwordEncoder.encode(reqDto.getPassword());
        user.modifyPassword(newEncodedPassword);
        userRepository.save(user);

        return new CommonResDto(HttpStatus.OK, "비밀번호가 변경되었습니다. 다시 로그인 해주세요", true);
    }
}
