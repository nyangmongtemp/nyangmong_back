package com.playdata.animalboardservice.common.entity;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;

import java.time.LocalDateTime;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

@Getter
@MappedSuperclass
public class BaseTimeEntity {

    @CreatedDate
    @Column(name = "create_at", updatable = false)
    private LocalDateTime createAt; // 생성 일시 (자동 관리)

    @LastModifiedDate
    @Column(name = "update_at")
    private LocalDateTime updateAt; // 수정 일시 (자동 관리)
}