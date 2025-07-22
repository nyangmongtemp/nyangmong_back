package com.playdata.adminservice.admin.entity;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Entity
@Table(name = "advertisement_count")
@Getter
@NoArgsConstructor
public class AdvertisementCount {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ad_num_id")
    private Long ad_num_id;

    @Column(name = "ad_num")
    private  int add_num;

}
