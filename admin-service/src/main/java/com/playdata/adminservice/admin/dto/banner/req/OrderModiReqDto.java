package com.playdata.adminservice.admin.dto.banner.req;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@Schema(description = "배너 순서 수정 요청 DTO")
public class OrderModiReqDto {

    @NotNull
    @Schema(description = "배너 id", example = "1")
    private Long bannerId;
    
    @NotNull
    @Schema(description = "배너 순서", example = "1")
    private Integer order;

}
