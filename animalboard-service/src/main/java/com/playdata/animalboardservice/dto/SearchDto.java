package com.playdata.animalboardservice.dto;

import com.playdata.animalboardservice.entity.SexCode;
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
public class SearchDto {
    // 통합 검색어 (나이, 품종, 보호소명 등 여러 필드에 적용됨)
    private String searchWord;
    // 축종 (개/고양이 등) 필터링용
    private String upKindNm;
    // 보호소 주소 필터링용
    private String careAddr;
    // 성별 (M,F,Q)
    private SexCode sexCode;
}
