package com.playdata.userservice.user.dto;

import com.playdata.userservice.user.entity.User;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
public class UserSaveReqDto {

    private String email;

    private String password;

    private String userName;

    private MultipartFile profileImage;

    private String phone;

    private String nickname;


    public User toEntity(String encodedPassword, String profileImagePath) {
        return User.builder()
                .email(email)
                .password(encodedPassword)
                .userName(userName)
                .profileImage(profileImagePath)
                .phone(phone)
                .nickname(nickname)
                .active(true)
                .reportCount(0)
                .passwordFaultCount(0)
                .pauseCount(0)
                .passwordUpdatedAt(LocalDateTime.now())
                .grade(0L)
                .build();
    }

}
