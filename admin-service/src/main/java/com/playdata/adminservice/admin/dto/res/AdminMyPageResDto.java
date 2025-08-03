package com.playdata.adminservice.admin.dto.res;

import com.playdata.adminservice.admin.entity.Role;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class AdminMyPageResDto {

    private Long adminId;
    private String name;
    private String email;
    private LocalDateTime createAt;
    private LocalDateTime updateAt;
    private String phone;
    private Role role;


}
