package com.playdata.adminservice.admin.dto;

import com.playdata.adminservice.admin.entity.Role;
import lombok.Getter;

@Getter
public class AdminSearchDto {

    private Role role;

    private Boolean Active;

    private String name;

}
