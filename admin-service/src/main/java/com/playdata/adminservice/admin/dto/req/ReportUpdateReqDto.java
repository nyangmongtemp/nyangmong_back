package com.playdata.adminservice.admin.dto.req;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

@Getter
@Schema(description = "사용자 계정 정지 요청 DTO")
public class ReportUpdateReqDto {

    @Schema(description = "계정 정지 기간(1, 3, 7, 15, 30, 999-무기한)", example = "15")
    private int releaseAt;

}
