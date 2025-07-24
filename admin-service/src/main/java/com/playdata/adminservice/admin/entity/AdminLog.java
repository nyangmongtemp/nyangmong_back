package com.playdata.adminservice.admin.entity;

import com.playdata.adminservice.admin.dto.req.AdminLogReqDto;
import com.playdata.adminservice.common.entity.BaseTimeEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(name = "tbl_log")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AdminLog extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long logId; // 로그 고유 ID (기본키, 자동 증가)

    @Column(nullable = false)
    private Long adminId; // 관리자 ID

    @Column(nullable = false)
    private Long userId; // 사용자 ID

    @Column(nullable = false)
    private String adminIp; // 접속한 관리자의 IP

}