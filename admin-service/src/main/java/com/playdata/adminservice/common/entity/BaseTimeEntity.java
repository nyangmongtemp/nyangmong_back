package com.playdata.adminservice.common.entity;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;

import java.time.LocalDateTime;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Getter
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public class BaseTimeEntity {

    @CreatedDate
    @Column(name = "create_at", updatable = false)
    private LocalDateTime createAt; // 생성 일시 (자동 관리)

    @LastModifiedDate
    @Column(name = "update_at")
    private LocalDateTime updateAt; // 수정 일시 (자동 관리)
}