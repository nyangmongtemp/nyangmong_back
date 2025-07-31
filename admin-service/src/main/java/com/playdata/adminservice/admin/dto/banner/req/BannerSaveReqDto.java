package com.playdata.adminservice.admin.dto.banner.req;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@Schema(description = "배너 생성 요청 DTO")
public class BannerSaveReqDto {

    @NotBlank
    @Schema(description = "배너 제목", example = "배너 생성 예시")
    private String title;

    @Schema(hidden = true)
    private String thumbnailImage;

    public void setThumbnailImage(String thumbnailImage) {
        this.thumbnailImage = thumbnailImage;
    }
}
