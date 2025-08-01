package com.playdata.userservice.user.dto.inform.req;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@Schema(description = "문의 생성 요청 DTO")
public class InformReqDto {

    @NotBlank
    @Schema(description = "문의 제목", example = "문의 생성 예시 제목")
    private String title;
    
    @NotBlank
    @Schema(description = "문의 내용", example = "문의 생성 예시 내용")
    private String content;
}
