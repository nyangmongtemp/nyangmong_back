package com.playdata.mapservice.map.dto.map.req;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@Schema(description = "문화시설 정보 검색 DTO")
public class MapSearchReqDto {

    @Schema(description = "지역", example = "1")
    private String region;

    @Schema(description = "카테고리", example = "12")
    private String contentType;
    
}
