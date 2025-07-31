package com.playdata.adminservice.admin.dto.banner.req;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@Schema(description = "배너 수정 요청 DTO")
public class BannerModiReqDto {

    @NotNull
    @Schema(description = "배너 id", example = "1")
    private Long bannerId;

    @Schema(description = "배너 제목", example = "배너 수정 예시")
    private String title;

    @Schema(hidden = true)
    private String thumbnailImage;

    public void setThumbnailImage(String thumbnailImage) {
        this.thumbnailImage = thumbnailImage;
    }

}
