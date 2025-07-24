package com.playdata.adminservice.admin.dto.banner.res;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class BannerListResDto {

    private Long bannerId;
    private Integer order;
    private String title;
    private Long adminId;

}
