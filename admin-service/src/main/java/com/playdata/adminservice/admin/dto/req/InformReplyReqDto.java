package com.playdata.adminservice.admin.dto.req;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

/**
 * 문의 수정 DTO
 */
@Getter
@Schema(description = "문의 수정 요청 DTO")
public class InformReplyReqDto {

    @NotBlank
    @Schema(description = "답변", example = "답변내용")
    private String reply;

}