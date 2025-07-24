package com.playdata.adminservice.admin.dto.res;

import com.playdata.adminservice.admin.entity.Admin;
import com.playdata.adminservice.admin.entity.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class AdminListResDto {

    private Long adminId;
    private String email;
    private String name;
    private String phone;
    private String password;
    private Role role;
    private boolean active;
    private boolean isFirst;

    @Builder
    public AdminListResDto(Admin admin){
        this.adminId = admin.getAdminId();
        this.email = admin.getEmail();
        this.name = admin.getName();
        this.phone = admin.getPhone();
        this.password = admin.getPassword();
        this.role = admin.getRole();
        this.active = admin.isActive();
        this.isFirst = admin.getIsFirst();
    }



}
