package com.playdata.adminservice.admin.dto.req;

import com.playdata.adminservice.admin.entity.Role;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class AdminRoleModifyReqDto {

    @NotBlank
    private Long adminId;

    @NotNull
    private Role role;

    private Boolean Active;

}
