package com.playdata.adminservice.admin.dto.banner.res;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class BannerSaveResDto {

    private Long bannerId;
    private String title;
    private String thumbnailImage;
    private Long adminId;
    private Integer order;
    private boolean basic;

}
