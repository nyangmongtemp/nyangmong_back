package com.playdata.animalboardservice.common.enumeration;

/**
 * Swagger example Json 상수 관리
 */
public class SwaggerExampleConstants {

    /**
     * [분양 동물] 상세 조회 Response Json
     */
    public static final String ANIMAL_DETAIL_RESPONSE = "{\n" +
                "\"createAt\": \"2025-07-08T10:30:12.7888\",\n" +
                "\"updateAt\": null,\n" +
                "\"postId\": 1,\n" +
                "\"userId\": 2,\n" +
                "\"thumbnailImage\": \"656c15d9-0fee-4e31-8eb2-8eab55c2a29c_춘식이.jpg\",\n" +
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
                "\"reservationStatus\": \"R\"\n"+
            "}";

    /**
     * [공통] 없는데이터 Exception Response Json
     */
    public static final String ANIMAL_DETAIL_EXCEPTION = "{\n" +
                "\"code\": \"GET-002\",\n" +
                "\"message\": \"요청 데이터가 존재하지 않습니다.\"\n" +
            "}";

}
