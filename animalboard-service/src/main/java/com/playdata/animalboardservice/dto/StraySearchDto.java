package com.playdata.animalboardservice.dto;

import com.playdata.animalboardservice.entity.SexCode;
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
public class StraySearchDto {
    // 통합 검색어 (나이, 품종, 보호소명 등 여러 필드에 적용됨)
    @Schema(description = "검색 키워드", defaultValue = "")
    private String searchWord;
    // 축종 (개/고양이 등) 필터링용
    @Schema(description = "축종 (개/고양이/기타)", defaultValue = "")
    private String upKindNm;
    // 보호소 주소 필터링용
    @Schema(description = "주소", defaultValue = "")
    private String careAddr;
    // 성별 (M,F,Q)
    @Schema(description = "성별 (M,F,Q)", defaultValue = "")
    private SexCode sexCode;
}
