package com.playdata.mapservice.common.enumeration;

public class MapSwaggerEx {

    public static final String MAP_LIST = """
            {
                "statusCode": 200,
                "statusMessage": "모든 맵 정보의 리스트 찾음",
                "result": [
                    {
                        "mapId": 1803,
                        "addr1": "서울특별시 성동구 고산자로 71 (성수동1가)",
                        "contentType": "TOURIST",
                        "addressCode": "SEOUL",
                        "title": "거꾸로하우스",
                        "mapx": "127.0361052512",
                        "mapy": "37.5457465853"
                    },
                    {
                        "mapId": 1904,
                        "addr1": "서울특별시 구로구 구로중앙로42길 27",
                        "contentType": "TOURIST",
                        "addressCode": "SEOUL",
                        "title": "구로구 반려견 놀이터",
                        "mapx": "126.8757403642",
                        "mapy": "37.5082258503"
                    },
                    {
                        "mapId": 1981,
                        "addr1": "서울특별시 종로구 낙산길 41",
                        "contentType": "TOURIST",
                        "addressCode": "SEOUL",
                        "title": "낙산공원",
                        "mapx": "127.0065125148",
                        "mapy": "37.5805725621"
                    },
                    {
                        "mapId": 1987,
                        "addr1": "서울특별시 중구 퇴계로34길 28 (필동2가)",
                        "contentType": "TOURIST",
                        "addressCode": "SEOUL",
                        "title": "남산골한옥마을",
                        "mapx": "126.9932865315",
                        "mapy": "37.5597775194"
                    },
                    {
                        "mapId": 1988,
                        "addr1": "서울특별시 중구 남산공원길 609 (예장동)",
                        "contentType": "TOURIST",
                        "addressCode": "SEOUL",
                        "title": "남산순환나들길",
                        "mapx": "126.9866844934",
                        "mapy": "37.5549112997"
                    }
                ]
            }
            """;

    public static final String MAP_DETAIL = """
            {
                "statusCode": 200,
                "statusMessage": "맵의 상세 정보 리스트 찾음",
                "result": {
                    "mapId": 2241,
                    "addr1": "경기도 포천시 이동면 도평리",
                    "addr2": "",
                    "addressCode": "31",
                    "contentType": "12",
                    "image1": "",
                    "image2": "",
                    "mapx": "127.3504217471",
                    "mapy": "38.0956743687",
                    "tel": "",
                    "title": "명성산",
                    "zipCode": "11103",
                    "contentId": "125469",
                    "createAt": "2025-07-17T16:38:38.392901",
                    "updateAt": "2025-07-17T16:38:38.392901"
                }
            }
            """;

    public static final String BAD_REQUEST = """
            {
                "code": "DEFAULT-001",
                "message": "잘못된 요청값입니다."
            }""";

}
