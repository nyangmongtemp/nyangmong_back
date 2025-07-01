package com.playdata.userservice.user.service;

import com.playdata.userservice.common.dto.CommonResDto;
import com.playdata.userservice.user.dto.UserLoginReqDto;
import com.playdata.userservice.user.dto.UserLoginResDto;
import com.playdata.userservice.user.dto.UserSaveReqDto;
import com.playdata.userservice.user.entity.User;
import com.playdata.userservice.user.repository.UserRepository;
import jakarta.mail.MessagingException;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.mail.javamail.JavaMailSender;
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

    private final MailSenderService mailSenderService;

    // 비밀번호 인코딩용
    private final PasswordEncoder passwordEncoder;
    
    private final UserRepository userRepository;

    private final RedisTemplate<String, Object> redisTemplate;

    // Redis key 상수
    private static final String VERIFICATION_CODE_KEY = "email_verify:code:";
    private static final String VERIFICATION_ATTEMPT_KEY = "email_verify:attempt:";
    private static final String VERIFICATION_BLOCK_KEY = "email_verify:block:";
    
    // 이미지 저장 경로 --> 추후에 yml에 있는 주소를 s3 주소로 바꿀 것
    @Value("${imagePath.url}")
    private String profileImageSaveUrl;

    // 임시 로그인 확인  --> 추후 삭제 예정
    public UserLoginResDto tempLogin(UserLoginReqDto userLoginReqDto) {

        Optional<User> byEmail =
                userRepository.findByEmail(userLoginReqDto.getEmail());
        
        // 해당 이메일이 DB에 없는 경우
        if (!byEmail.isPresent()) {
            return null;
        }
        
        // 입력한 비밀번호가 틀린 경우
        if(!byEmail.get().getPassword().equals(userLoginReqDto.getPassword())) {
            return UserLoginResDto.builder()
                    .email(userLoginReqDto.getEmail())
                    // 로그인 결과를 false로
                    .Logged(false)
                    .build();
        }

        // 로그인 성공
        return UserLoginResDto.builder()
                .email(userLoginReqDto.getEmail())
                // 로그인 결과를 true로
                .role("USER")
                .Logged(true)
                .build();
    }
    
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
        
        MultipartFile imageFile = userSaveReqDto.getProfileImage();
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
            }
        }
        // DB에 저장을 위해 패스워드 인코딩
        String encodedPassword = passwordEncoder.encode(password);
        // 부가적인 정보를 담아서 User를 DB에 저장
        User createdUser = userSaveReqDto.toEntity(encodedPassword, profileImagePath);
        User saved = userRepository.save(createdUser);

        CommonResDto resDto = new CommonResDto(HttpStatus.CREATED, "회원가입에 성공하였습니다", true);
        return resDto;
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
        }
        // 로그인 성공
        return new CommonResDto(HttpStatus.OK, "로그인에 성공하였습니다.", true);
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
        redisTemplate.opsForValue().set(key, authNum, Duration.ofMinutes(2));

        // 나중에 더미데이터를 편하게 넣기 위해서 인증번호를 로그로 남기기 위함
        // 실제 서비스에서는 아래의 return문에 authNum을 삭제해야함.
        return new CommonResDto(HttpStatus.OK, "회원가입 인증코드가 이메일로 발송되었습니다.", authNum);

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
}
