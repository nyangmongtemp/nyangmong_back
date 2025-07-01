package com.playdata.userservice.user.dto;

import com.playdata.userservice.user.entity.User;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import jakarta.validation.constraints.Pattern;
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

    @NotBlank(message = "비밀번호는 필수입니다.")
    @Size(min = 8, message = "비밀번호는 최소 8자 이상이어야 합니다.")
    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\W).+$",
            message = "비밀번호는 영문 대소문자 및 특수문자를 각각 1개 이상 포함해야 합니다.")
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
