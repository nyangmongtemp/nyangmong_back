package com.playdata.mapservice.map.service;

import org.locationtech.proj4j.*;

public class Epsg5174ToWgs84Converter {
    
    // EPSG5174 좌표를 표준 위도, 경도로 변환해주는 메소드
    public static ProjCoordinate convertEPSG5174ToWGS84(double x, double y) {
        CRSFactory crsFactory = new CRSFactory();
        CoordinateReferenceSystem epsg5174 = crsFactory.createFromName("EPSG:5174");
        CoordinateReferenceSystem wgs84 = crsFactory.createFromName("EPSG:4326");

        CoordinateTransformFactory ctFactory = new CoordinateTransformFactory();
        CoordinateTransform transform = ctFactory.createTransform(epsg5174, wgs84);

        ProjCoordinate srcCoord = new ProjCoordinate(x, y);
        ProjCoordinate destCoord = new ProjCoordinate();

        transform.transform(srcCoord, destCoord);

        return destCoord;
    }

}
