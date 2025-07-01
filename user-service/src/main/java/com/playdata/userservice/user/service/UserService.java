package com.playdata.userservice.user.service;

import com.playdata.userservice.user.dto.UserLoginReqDto;
import com.playdata.userservice.user.dto.UserLoginResDto;
import com.playdata.userservice.user.dto.UserSaveReqDto;
import com.playdata.userservice.user.entity.User;
import com.playdata.userservice.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {

    // 비밀번호 인코딩용
    private final PasswordEncoder passwordEncoder;
    
    private final UserRepository userRepository;
    
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
    public Object userCreate(UserSaveReqDto userSaveReqDto) {

        String email = userSaveReqDto.getEmail();
        Optional<User> foundByEmail = userRepository.findByEmail(email);
        // 회원가입을 요청한 이메일로 이미 가입한 회원정보가 있는 경우
        if (foundByEmail.isPresent()) {
            // 회원가입 실패
            return false;
        }

        // 이메일 유효성 검증을 통과한 경우

        // DB에 저장하기 위해 비밀번호 인코딩
        String password = userSaveReqDto.getPassword();
        if(password.length() < 8) {
            return false;
        }
        
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
                e.printStackTrace();
                return null; // 저장 실패 처리
            }
        }
        // DB에 저장을 위해 패스워드 인코딩
        String encodedPassword = passwordEncoder.encode(password);
        // 부가적인 정보를 담아서 User를 DB에 저장
        User createdUser = userSaveReqDto.toEntity(encodedPassword, profileImagePath);
        User saved = userRepository.save(createdUser);

        return true;
    }

    public boolean login(UserLoginReqDto userLoginReqDto) {

        Optional<User> foundUser = userRepository.findByEmail(userLoginReqDto.getEmail());


    }
}
