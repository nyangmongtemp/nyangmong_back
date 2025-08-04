package com.playdata.adminservice.admin.dto.board;

import com.playdata.adminservice.admin.entity.SexCode;
import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AnimalSearchDto {
    // 통합 검색어 (나이, 품종, 보호소명 등 여러 필드에 적용됨)
    private String searchWord;
    // 축종 (개/고양이 등) 필터링용
    private String petCategory;
    // 보호소 주소 필터링용
    private String address;
    // 성별 (M,F,Q)
    private SexCode sex;
}
