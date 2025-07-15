package com.playdata.adminservice.admin.dto.res;

import com.playdata.adminservice.admin.entity.Role;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class AdminLoginResDto {

    private String email;

    private String name;

    private Role role;


}
