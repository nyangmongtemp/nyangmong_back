package com.playdata.adminservice.admin.dto.req;

import com.playdata.adminservice.admin.entity.AdminLog;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class AdminLogReqDto {
    private Long adminId;
    private Long userId;
    private String ip;

    public AdminLog toEntity() {
        return AdminLog.builder()
                .adminId(adminId)
                .userId(userId)
                .adminIp(ip)
                .build();
    }
}
