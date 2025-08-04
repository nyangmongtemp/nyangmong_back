package com.playdata.adminservice.admin.dto.req;

import com.playdata.adminservice.admin.entity.Admin;
import com.playdata.adminservice.admin.entity.Role;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "관리자 등록 요청 DTO")
public class AdminSaveReqDto {

    @Email
    @NotBlank
    @Schema(description = "이메일", example = "example@example.com")
    private String email;

    @NotBlank
    @Schema(description = "전화번호", example = "010-1234-1234")
    private String phone;

    @NotBlank(message = "비밀번호는 필수입니다.")
    @Size(min = 8, message = "비밀번호는 최소 8자 이상이어야 합니다.")
    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\W).+$",
            message = "비밀번호는 영문 대소문자 및 특수문자를 각각 1개 이상 포함해야 합니다.")
    @Schema(description = "비밀번호", example = "@Admin12345")
    private String password;

    @NotNull
    @Schema(description = "관리자 권한", example = "CONTENT")
    private Role role;

    @NotNull
    @Schema(description = "이름", example = "홍길동")
    private String name;

    public Admin toEntity(PasswordEncoder encoder) {
        return Admin.builder()
                .email(email)
                .phone(phone)
                .role(role)
                .name(name)
                .password(encoder.encode(password))
                .build();
    }
}
