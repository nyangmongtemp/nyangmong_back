package com.playdata.adminservice.admin.dto.req;

import com.playdata.adminservice.admin.entity.Admin;
import com.playdata.adminservice.admin.entity.Role;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AdminSaveReqDto {

    @Email
    @NotBlank
    private String email;

    @NotBlank
    private String phone;

    @NotBlank(message = "비밀번호는 필수입니다.")
    @Size(min = 8, message = "비밀번호는 최소 8자 이상이어야 합니다.")
    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\W).+$",
            message = "비밀번호는 영문 대소문자 및 특수문자를 각각 1개 이상 포함해야 합니다.")
    private String password;

    @NotNull
    private Role role;

    @NotNull
    private String name;

    public Admin toEntity(String encodedPassword) {
        return Admin.builder()
                .email(email)
                .phone(phone)
                .role(role)
                .name(name)
                .password(encodedPassword)
                .build();
    }
}
