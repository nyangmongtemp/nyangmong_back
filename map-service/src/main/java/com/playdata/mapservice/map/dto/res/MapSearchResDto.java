package com.playdata.mapservice.map.dto.res;

import com.playdata.mapservice.map.entity.ContentType;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class MapSearchResDto {

    private Long mapId;

    private String addr1;

    private String contentType;

    private String addressCode;

    private String title;

    private String mapx;

    private String mapy;
}
