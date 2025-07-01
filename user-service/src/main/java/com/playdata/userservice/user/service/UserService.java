package com.playdata.userservice.user.service;

import com.playdata.userservice.user.dto.UserLoginReqDto;
import com.playdata.userservice.user.dto.UserLoginResDto;
import com.playdata.userservice.user.entity.User;
import com.playdata.userservice.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {

    private final UserRepository userRepository;

    public UserLoginResDto login(UserLoginReqDto userLoginReqDto) {

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
}
