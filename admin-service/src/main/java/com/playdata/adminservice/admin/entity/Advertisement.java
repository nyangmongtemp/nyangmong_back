package com.playdata.adminservice.admin.entity;

import com.playdata.adminservice.common.entity.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Table(name = "advertisements")
@Getter
@NoArgsConstructor
public class Advertisement extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "banner_id")
    private Long id;

    private String thumbnailImage;

    private String title;

    private String description;

    private Boolean active;

    private Integer orderNum;

    private LocalDate startDate;

    private LocalDate endDate;

    public void update(String title, String description, Boolean active, Integer orderNum,
                       String thumbnailImage, LocalDate startDate, LocalDate endDate) {
        this.title = title;
        this.description = description;
        this.active = active;
        this.orderNum = orderNum;
        this.thumbnailImage = thumbnailImage;
        this.startDate = startDate;
        this.endDate = endDate;
    }
}