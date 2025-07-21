package com.playdata.adminservice.admin.entity;

import com.playdata.adminservice.common.entity.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;


@Entity
@Table(name = "advertisements")
@Getter
@NoArgsConstructor
public class Advertisement extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "advertisement_id")
    private Long id;

    @Column(nullable = false, length = 500)
    private String thumbnailImage;

    @Column(nullable = false, length = 100)
    private String title;

    @Column(nullable = false, length = 1000)
    private String description;



    @Setter
    @Column(nullable = false)
    private Boolean active = true;

    @Setter
    private Integer orderNum;

    @Column(nullable = false)
    private LocalDate startDate;

    @Column(nullable = false)
    private LocalDate endDate;

    @Column(name = "link_url")
    private String linkUrl;

    public void update(String title, String description, Boolean active, Integer orderNum,
                       String thumbnailImage, LocalDate startDate, LocalDate endDate, String linkUrl) {
        this.title = title;
        this.description = description;
        this.active = active;
        this.orderNum = orderNum;
        this.thumbnailImage = thumbnailImage;
        this.startDate = startDate;
        this.endDate = endDate;
        this.linkUrl = linkUrl;
    }

    @Builder
    public Advertisement(String thumbnailImage, String title, String description,
                         Boolean active, Integer orderNum, LocalDate startDate, LocalDate endDate, String linkUrl) {
        this.thumbnailImage = thumbnailImage;
        this.title = title;
        this.description = description;
        this.active = active;
        this.orderNum = orderNum;
        this.startDate = startDate;
        this.endDate = endDate;
        this.linkUrl = linkUrl;

    }

}