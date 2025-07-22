package com.playdata.adminservice.admin.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "advertisement_count")
@Getter
@Setter
@NoArgsConstructor
@ToString
public class AdvertisementCount {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ad_num_id") // DB 컬럼은 그대로 유지
    private Long adNumId;

    @Column(name = "ad_num")
    private int adNum;

    @Builder
    public AdvertisementCount(int adNum) {
        this.adNum = adNum;
    }
}