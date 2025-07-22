package com.playdata.mapservice.map.dto.map.res;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class MapDetailResDto {

    private Long mapId;

    private String addr1;

    private String addr2;

    private String contentType;

    private String addressCode;

    private String image1;

    private String image2;

    private String tel;

    private String title;

    private String mapx;

    private String mapy;

}
