package com.playdata.mapservice.map.dto.CultureDetail.PetStyle.res;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class PetStyleListResDto {

    private Long id;
    private String roadAddress;  // 도로명주소명
    private String fullAddress;  // 지번주소
    private String facilityName;  // 시설명

}
