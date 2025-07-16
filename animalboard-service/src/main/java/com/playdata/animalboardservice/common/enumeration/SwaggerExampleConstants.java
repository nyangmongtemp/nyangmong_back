package com.playdata.animalboardservice.common.enumeration;

/**
 * Swagger example Json 상수 관리
 */
public class SwaggerExampleConstants {
    /**
     * [분양 동물] 목록 조회 Response Json
     */
    public static final String ANIMAL_LIST_RESPONSE = "{\n" +
            "\"content\": [\n" +
            "{\n" +
            "\"postId\": 14,\n" +
            "\"userId\": 1,\n" +
            "\"thumbnailImage\": \"7d9356ea-420e-4087-995b-1ee2065d196b_main_view_capture.png\",\n" +
            "\"title\": \"ㄴㅇㄹㅁㄴㅇㄹㅁㅇㄴㄹ\",\n" +
            "\"content\": \"<p>ㅇ</p><p>&nbsp;</p><p>ㅇ</p><p>&nbsp;</p><p>ㅇ</p><p>ㅇ</p><p>&nbsp;</p><p>ㅇ</p><p>&nbsp;</p><p>ㅇ</p><p>&nbsp;</p><p>ㅇ</p><p>ㅇ</p><p>&nbsp;</p><p>ㅇ</p><p>&nbsp;</p><p>ㅇ</p><p>&nbsp;</p><p>ㅇ</p><p>ㅇ</p><p>&nbsp;</p><p>ㅇ</p><p>ㅇ</p><p>&nbsp;</p><p>ㅇ</p><p>&nbsp;</p><p>ㅇ</p><p>ㅇ</p>\",\n" +
            "\"viewCount\": 1,\n" +
            "\"petCategory\": \"강아지\",\n" +
            "\"petKind\": \"ㅁㅇㄴㄹㅁㅇㄹ\",\n" +
            "\"age\": \"ㄴㅇㄹㅇㄹ\",\n" +
            "\"vaccine\": \"\",\n" +
            "\"sexCode\": \"M\",\n" +
            "\"neuterYn\": \"Y\",\n" +
            "\"address\": \"강원도\",\n" +
            "\"fee\": 20000,\n" +
            "\"active\": true\n" +
            "},\n" +
            "{\n" +
            "\"postId\": 13,\n" +
            "\"userId\": 2,\n" +
            "\"thumbnailImage\": \"3b629402-bee8-4ee8-823f-228c2ddcfbb7_춘식이.jpg\",\n" +
            "\"title\": \"2살 믹스견 분양합니다111\",\n" +
            "\"content\": \"우리강아지 분양합니다\",\n" +
            "\"viewCount\": 1,\n" +
            "\"petCategory\": \"강아지\",\n" +
            "\"petKind\": \"믹스견\",\n" +
            "\"age\": \"2살\",\n" +
            "\"vaccine\": \"1차접종 완료, 2차접종 준비중\",\n" +
            "\"sexCode\": \"M\",\n" +
            "\"neuterYn\": \"N\",\n" +
            "\"address\": \"서울시 서초구\",\n" +
            "\"fee\": 20000,\n" +
            "\"active\": true\n" +
            "}\n" +
            "],\n" +
            "\"pageable\": {\n" +
            "\"pageNumber\": 0,\n" +
            "\"pageSize\": 10,\n" +
            "\"sort\": [],\n" +
            "\"offset\": 0,\n" +
            "\"unpaged\": false,\n" +
            "\"paged\": true\n" +
            "},\n" +
            "\"totalPages\": 2,\n" +
            "\"totalElements\": 13,\n" +
            "\"last\": false,\n" +
            "\"size\": 10,\n" +
            "\"number\": 0,\n" +
            "\"sort\": [],\n" +
            "\"numberOfElements\": 10,\n" +
            "\"first\": true,\n" +
            "\"empty\": false\n" +
            "}";

    /**
     * [분양 동물] 상세 조회 Response Json
     */
    public static final String ANIMAL_DETAIL_RESPONSE = "{\n" +
            "\"createAt\": \"2025-07-08T10:30:12.7888\",\n" +
            "\"updateAt\": null,\n" +
            "\"postId\": 1,\n" +
            "\"userId\": 2,\n" +
            "\"thumbnailImage\": \"656c15d9-0fee-4e31-8eb2-8eab55c2a29c_춘식이.jpg\",\n" +
            "\"nickName\": \"테스터\",\n" +
            "\"title\": \"2살 믹스견 분양합니다\",\n" +
            "\"content\": \"우리강아지 분양합니다\",\n" +
            "\"viewCount\": 8,\n" +
            "\"petCategory\": \"강아지\",\n" +
            "\"petKind\": \"믹스견\",\n" +
            "\"age\": \"2살\",\n" +
            "\"vaccine\": \"1차접종 완료, 2차접종 준비중\",\n" +
            "\"sexCode\": \"M\",\n" +
            "\"neuterYn\": \"N\",\n" +
            "\"address\": \"서울시 서초구\",\n" +
            "\"fee\": 200000,\n" +
            "\"active\": true,\n" +
            "\"reservationStatus\": \"R\"\n" +
            "}";

    /**
     * [공통] 없는데이터 Exception Response Json
     */
    public static final String ANIMAL_DETAIL_EXCEPTION = "{\n" +
            "\"code\": \"GET-002\",\n" +
            "\"message\": \"요청 데이터가 존재하지 않습니다.\"\n" +
            "}";
}