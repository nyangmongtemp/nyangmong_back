package com.playdata.adminservice.common.auth;

import com.playdata.adminservice.admin.entity.Role;
import lombok.*;

@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TokenAdminInfo {

    private String email;
    private Role role;
    private Long adminId;
}
