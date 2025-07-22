package com.playdata.mapservice.map.dto.map.req;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class MapSearchReqDto {

    private String region;

    private String contentType;

}
