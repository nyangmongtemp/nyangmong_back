package com.playdata.mapservice.map.service;

import org.locationtech.proj4j.*;

public class Epsg5174ToWgs84Converter {
    
    // EPSG5174 좌표를 표준 위도, 경도로 변환해주는 메소드
    public static ProjCoordinate convertEPSG5174ToWGS84(double x, double y) {
        CRSFactory crsFactory = new CRSFactory();

        // EPSG:5174 정의 (TM 중부 좌표계)
        String epsg5174Params = "+proj=tmerc +lat_0=38 +lon_0=127 +k=1.0 "
                + "+x_0=200000 +y_0=500000 +ellps=GRS80 +units=m +no_defs";
        CoordinateReferenceSystem epsg5174 = crsFactory.createFromParameters("EPSG:5174", epsg5174Params);

        // EPSG:3857 (Web Mercator)
        CoordinateReferenceSystem epsg3857 = crsFactory.createFromName("EPSG:3857");

        // EPSG:4326 (WGS84, 위도/경도)
        CoordinateReferenceSystem wgs84 = crsFactory.createFromName("EPSG:4326");

        CoordinateTransformFactory ctFactory = new CoordinateTransformFactory();

        // 1단계: EPSG:5174 → EPSG:3857
        CoordinateTransform to3857 = ctFactory.createTransform(epsg5174, epsg3857);
        ProjCoordinate input = new ProjCoordinate(x, y);
        ProjCoordinate coord3857 = new ProjCoordinate();
        to3857.transform(input, coord3857);

        // 2단계: EPSG:3857 → WGS84
        CoordinateTransform toWGS84 = ctFactory.createTransform(epsg3857, wgs84);
        ProjCoordinate finalCoord = new ProjCoordinate();
        toWGS84.transform(coord3857, finalCoord);

        return finalCoord; // .x = longitude, .y = latitude
    }

}
