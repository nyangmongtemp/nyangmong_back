package com.playdata.adminservice.admin.dto.req;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserSearchDto {

    @Schema(description = "검색 키워드", defaultValue = "")
    private String keyword;
    @Schema(description = "신고 횟수 (true-신고횟수존재, false-전체검색)", defaultValue = "false")
    private Boolean report;
    @Schema(description = "계정 활셩화 여부 (빈공간-전체, 0-활성화, 1-비활성화)", defaultValue = "")
    private Boolean active;

}
