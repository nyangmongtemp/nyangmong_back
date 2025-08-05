package com.playdata.userservice.user.dto.req;

import com.playdata.userservice.common.util.HtmlSanitizer;
import com.playdata.userservice.user.entity.User;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@Schema(description = "회원가입 DTO")
public class UserSaveReqDto {

    @Email
    @Schema(description = "이메일", example = "example@example.com")
    private String email;

    @NotBlank(message = "비밀번호는 필수입니다.")
    @Size(min = 8, message = "비밀번호는 최소 8자 이상이어야 합니다.")
    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\W).+$",
            message = "비밀번호는 영문 대소문자 및 특수문자를 각각 1개 이상 포함해야 합니다.")
    @Schema(description = "비밀번호", example = "Abcdefgh1!")
    private String password;

    @NotNull
    @Schema(description = "이름", example = "홍길동")
    private String userName;

    @Schema(description = "닉네임", example = "의적홍길동")
    private String nickname;



    public User toEntity(String encodedPassword, String profileImagePath, HtmlSanitizer plainTextPolicy) {
        if(StringUtils.isBlank(nickname)){
            nickname = userName;
        }
        return User.builder()
                .email(email)
                .password(encodedPassword)
                .userName(plainTextPolicy.sanitizeText(userName))
                .profileImage(profileImagePath)
                .nickname(plainTextPolicy.sanitizeText(nickname))
                .active(true)
                .passwordFaultCount(0)
                .pauseCount(0)
                .passwordUpdatedAt(LocalDateTime.now())
                .grade(0L)
                .build();
    }

}
