package com.playdata.mainservice.main.dto.res;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class BannerListResDto {

    private Long bannerId;
    private Integer order;
    private String title;
    private Long adminId;
    private boolean basic;
    private String image;

}
