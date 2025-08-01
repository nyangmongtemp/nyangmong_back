package com.playdata.festivalservice.common.enumeration;

/**
 * Swagger example Json 상수 관리
 */
public class SwaggerExampleConstants {

    /**
     * [행사] 목록 조회 Response Json
     */
    public static final String TERMS_LIST_RESPONSE = """
    {
        "content": [
            {
                "festivalId": 1,
                "title": "2025 K렙타일페어 52 in 부산",
                "location": "벡스코",
                "festivalDate": "2025.08.02. (토) ~ 2025.08.03. (일)",
                "imagePath": "/Users/ubing/Desktop/nyangmong/images/crawling/common?type=ofullfill&size=174x250&quality=85&direct=true&src=https%3A%2F%2Fcsearch-phinf.pstatic.net%2F20250709_212%2F17520478790964zbpw_JPEG%2FSvQDkryjFN.jpg",
                "money": "",
                "url": "https://bighornmania.cafe24.com/",
                "reservationDate": "2025.07.08. (화) ~ 2025.07.31. (목)",
                "description": null,
                "festivalTime": "",
                "addr": "부산 해운대구 APEC로 55"
            },
            {
                "festivalId": 2,
                "title": "2025 여수 펫친소",
                "location": "여수엑스포",
                "festivalDate": "2025.08.08. (금) ~ 2025.08.10. (일)",
                "imagePath": "/Users/ubing/Desktop/nyangmong/images/crawling/common?type=ofullfill&size=174x250&quality=85&direct=true&src=https%3A%2F%2Fcsearch-phinf.pstatic.net%2F20250707_197%2F17518719569920MNN0_PNG%2FHoNm3l1Shk.png",
                "money": "■사전등록시 무료입장 (~2025.08.7까지)■현장등록 : 1인 5,000원■무료입장 : 2010년생 이후 출생자(16세 이하), 1960년생 이전 출생자(65세 이상) 경로 우대자, 장애인(복지...",
                "url": "https://petchinso.kr/",
                "reservationDate": "2025.06.01. (일) ~ 2025.08.07. (목)",
                "description": null,
                "festivalTime": "10:00 ~ 18:00",
                "addr": "전남 여수시 박람회길 1"
            }
        ],
        "pageable": {
            "pageNumber": 0,
            "pageSize": 10,
            "sort": [
                {
                    "direction": "DESC",
                    "property": "createdAt",
                    "ignoreCase": false,
                    "nullHandling": "NATIVE",
                    "ascending": false,
                    "descending": true
                }
            ],
            "offset": 0,
            "paged": true,
            "unpaged": false
        },
        "totalPages": 3,
        "totalElements": 27,
        "last": false,
        "size": 10,
        "number": 0,
        "sort": [
            {
                "direction": "DESC",
                "property": "createdAt",
                "ignoreCase": false,
                "nullHandling": "NATIVE",
                "ascending": false,
                "descending": true
            }
        ],
        "numberOfElements": 10,
        "first": true,
        "empty": false
    }""";

    /**
     * [공통] 잘못된 요청 Exception Response Json
     */
    public static final String COMMON_BAD_REQUEST = """
    {
        "code": "DEFAULT-001",
        "message": "잘못된 요청입니다."
    }""" ;

    /**
     * [공통] 없는데이터 Exception Response Json
     */
    public static final String COMMON_DETAIL_EXCEPTION = """
    {
        "code": "GET-002",
        "message": "요청 데이터가 존재하지 않습니다."
    }""";
}