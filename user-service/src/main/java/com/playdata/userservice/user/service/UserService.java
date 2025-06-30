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

        if (!byEmail.isPresent()) {
            return null;
        }

        if(!byEmail.get().getPassword().equals(userLoginReqDto.getPassword())) {
            return UserLoginResDto.builder()
                    .email(userLoginReqDto.getEmail())
                    .Logged(false)
                    .build();
        }

        return UserLoginResDto.builder()
                .email(userLoginReqDto.getEmail())
                .Logged(true)
                .build();
    }
}
