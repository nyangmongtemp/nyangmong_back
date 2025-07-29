package com.playdata.adminservice.admin.dto.banner.req;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class BannerSaveReqDto {

    @NotBlank
    private String title;

    private String thumbnailImage;

    public void setThumbnailImage(String thumbnailImage) {
        this.thumbnailImage = thumbnailImage;
    }
}
