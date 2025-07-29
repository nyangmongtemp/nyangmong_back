package com.playdata.adminservice.admin.dto.banner.req;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class BannerModiReqDto {

    @NotNull
    private Long bannerId;

    private String title;

    private String thumbnailImage;

    public void setThumbnailImage(String thumbnailImage) {
        this.thumbnailImage = thumbnailImage;
    }

}
