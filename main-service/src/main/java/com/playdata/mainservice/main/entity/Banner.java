package com.playdata.mainservice.main.entity;

import com.playdata.mainservice.common.entity.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
// 임의로 지정한 테이블 이름 -> 추후에 모든 서비스의 테이블 이름을 통일할 것!
@Table(name = "tbl_banner")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Banner extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long bannerId;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private Long adminId;

    @Column(nullable = false)
    private String thumbnailImage;

    @Column(nullable = false)
    private Integer order;

    @Column(nullable = false)
    private boolean active;



}
