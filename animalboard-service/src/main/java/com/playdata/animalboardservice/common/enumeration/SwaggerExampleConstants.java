package com.playdata.animalboardservice.common.enumeration;

/**
 * Swagger example Json 상수 관리
 */
public class SwaggerExampleConstants {
    /**
     * [분양 동물] 목록 조회 Response Json
     */
    public static final String ANIMAL_LIST_RESPONSE = """
    {
    "statusCode": 200,
    "statusMessage": "목록 조회",
    "result": {
        "content": [
            {
                "postId": 1,
                "userId": 1,
                "thumbnailImage": "c3ea4d74-a8fc-480f-be64-c06efd0822a8_main_view_capture.png",
                "title": "제목",
                "content": "내용",
                "viewCount": 2,
                "petCategory": "강아지",
                "petKind": "믹스견",
                "age": "2살",
                "vaccine": "1차접종 완료, 2차접종 준비중",
                "sexCode": "M",
                "neuterYn": "N",
                "address": "서울시 서초구",
                "fee": 20000,
                "active": true,
                "likeCount": 0,
                "commentCount": 0
            },
            {
                "postId": 1,
                "userId": 1,
                "thumbnailImage": "c3ea4d74-a8fc-480f-be64-c06efd0822a8_main_view_capture.png",
                "title": "제목",
                "content": "내용",
                "viewCount": 2,
                "petCategory": "강아지",
                "petKind": "믹스견",
                "age": "2살",
                "vaccine": "1차접종 완료, 2차접종 준비중",
                "sexCode": "M",
                "neuterYn": "N",
                "address": "서울시 서초구",
                "fee": 20000,
                "active": true,
                "likeCount": 0,
                "commentCount": 0
            },
            {
                "postId": 1,
                "userId": 1,
                "thumbnailImage": "c3ea4d74-a8fc-480f-be64-c06efd0822a8_main_view_capture.png",
                "title": "제목",
                "content": "내용",
                "viewCount": 2,
                "petCategory": "강아지",
                "petKind": "믹스견",
                "age": "2살",
                "vaccine": "1차접종 완료, 2차접종 준비중",
                "sexCode": "M",
                "neuterYn": "N",
                "address": "서울시 서초구",
                "fee": 20000,
                "active": true,
                "likeCount": 0,
                "commentCount": 0
            },
            {
                "postId": 1,
                "userId": 1,
                "thumbnailImage": "c3ea4d74-a8fc-480f-be64-c06efd0822a8_main_view_capture.png",
                "title": "제목",
                "content": "내용",
                "viewCount": 2,
                "petCategory": "강아지",
                "petKind": "믹스견",
                "age": "2살",
                "vaccine": "1차접종 완료, 2차접종 준비중",
                "sexCode": "M",
                "neuterYn": "N",
                "address": "서울시 서초구",
                "fee": 20000,
                "active": true,
                "likeCount": 0,
                "commentCount": 0
            },
            {
                "postId": 1,
                "userId": 1,
                "thumbnailImage": "c3ea4d74-a8fc-480f-be64-c06efd0822a8_main_view_capture.png",
                "title": "제목",
                "content": "내용",
                "viewCount": 2,
                "petCategory": "강아지",
                "petKind": "믹스견",
                "age": "2살",
                "vaccine": "1차접종 완료, 2차접종 준비중",
                "sexCode": "M",
                "neuterYn": "N",
                "address": "서울시 서초구",
                "fee": 20000,
                "active": true,
                "likeCount": 0,
                "commentCount": 0
            }
        ],
        "pageable": {
            "pageNumber": 0,
            "pageSize": 10,
            "sort": [],
            "offset": 0,
            "unpaged": false,
            "paged": true
        },
        "totalPages": 1,
        "totalElements": 5,
        "last": true,
        "size": 10,
        "number": 0,
        "sort": [],
        "numberOfElements": 5,
        "first": true,
        "empty": false
    }""";

    /**
     * [분양 동물] 상세 조회 Response Json
     */
    public static final String ANIMAL_DETAIL_RESPONSE = """
    {
        "statusCode": 200,
        "statusMessage": "상세 조회",
        "result": {
            "createAt": "2025-07-30T17:37:56.562375",
            "updateAt": "2025-07-31T11:02:44.078296",
            "postId": 1,
            "userId": 1,
            "nickname": "등록자",
            "thumbnailImage": "c3ea4d74-a8fc-480f-be64-c06efd0822a8_main_view_capture.png",
            "title": "제목",
            "content": "내용",
            "viewCount": 2,
            "petCategory": "강아지",
            "petKind": "믹스견",
            "age": "2살",
            "vaccine": "1차접종 완료, 2차접종 준비중",
            "sexCode": "M",
            "neuterYn": "N",
            "address": "서울시 서초구",
            "fee": 20000,
            "active": true,
            "reservationStatus": "A"
        }
    }""";

    /**
     * [분양 동물] 등록 성공 Response Json
     */
    public static final String CREATE_ANIMAL_SUCCESS = """
    {
        "statusCode": 201,
        "statusMessage": "등록 완료",
        "result": {
            "createAt": "2025-07-31T12:24:27.91562",
            "updateAt": "2025-07-31T12:24:27.91562",
            "postId": 26,
            "userId": 1,
            "nickname": "테스터1",
            "thumbnailImage": "a5f91b6d-d927-4918-a59f-222cabeb9536_main_view_capture.png",
            "title": "우리아이 분양합니다.",
            "content": "우리아이 태어난지 30일 되었습니다.",
            "viewCount": 0,
            "petCategory": "강아지",
            "petKind": "비숑",
            "age": "30일",
            "vaccine": "안맞았어요",
            "sexCode": "M",
            "neuterYn": "N",
            "address": "강남구",
            "fee": 0,
            "active": true,
            "reservationStatus": "A"
        }
    }""";

    /**
     * [분양 동물] 수정 성공 Response Json
     */
    public static final String UPDATE_ANIMAL_SUCCESS = """
    {
        "statusCode": 200,
        "statusMessage": "수정 완료",
        "result": {
            "createAt": "2025-07-31T13:10:48.847387",
            "updateAt": "2025-07-31T13:11:10.363339",
            "postId": 27,
            "userId": 1,
            "nickname": "테스터1",
            "thumbnailImage": "d04d7964-943a-4baa-a3a4-01fe6a1523d7_main_view_capture.png",
            "title": "제목 입력",
            "content": "내용 입력",
            "viewCount": 0,
            "petCategory": "강아지",
            "petKind": "믹스견",
            "age": "2살",
            "vaccine": "1처완료",
            "sexCode": "M",
            "neuterYn": "N",
            "address": "서초구",
            "fee": 20000,
            "active": true,
            "reservationStatus": "A"
        }
    }""";

    /**
     * [분양 동물] 삭제 성공 Response Json
     */
    public static final String DELETE_ANIMAL_SUCCESS = """
    {
        "statusCode": 200,
        "statusMessage": "삭제 완료",
        "result": {
            "createAt": "2025-07-31T13:10:48.847387",
            "updateAt": "2025-07-31T13:46:11.01962",
            "postId": 27,
            "userId": 1,
            "nickname": "테스터1",
            "thumbnailImage": "d04d7964-943a-4baa-a3a4-01fe6a1523d7_main_view_capture.png",
            "title": "제목 입력",
            "content": "내용 입력",
            "viewCount": 0,
            "petCategory": "강아지",
            "petKind": "믹스견",
            "age": "2살",
            "vaccine": "1처완료",
            "sexCode": "M",
            "neuterYn": "N",
            "address": "서초구",
            "fee": 20000,
            "active": false,
            "reservationStatus": "A"
        }
    }""";

    /**
     * [분양 동물] 예약상태 변경 성공 Response Json
     */
    public static final String CHANGE_ANIMAL_SUCCESS = """
    {
        "statusCode": 200,
        "statusMessage": "분양동물 예약상태 변경",
        "result": {
            "createAt": "2025-07-31T12:24:27.91562",
            "updateAt": "2025-07-31T12:51:04.101321",
            "postId": 26,
            "userId": 1,
            "nickname": "테스터1",
            "thumbnailImage": "a5f91b6d-d927-4918-a59f-222cabeb9536_main_view_capture.png",
            "title": "우리아이 분양합니다.",
            "content": "우리아이 태어난지 30일 되었습니다.",
            "viewCount": 1,
            "petCategory": "강아지",
            "petKind": "비숑",
            "age": "30일",
            "vaccine": "안맞았어요",
            "sexCode": "M",
            "neuterYn": "N",
            "address": "강남구",
            "fee": 0,
            "active": true,
            "reservationStatus": "A"
        }
    }""";

    /**
     * [공통] 없는데이터 Exception Response Json
     */
    public static final String COMMON_DETAIL_EXCEPTION = """
    {
        "code": "GET-002",
        "message": "요청 데이터가 존재하지 않습니다."
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
     * [공통] 토큰 요청(비로그인) Exception Response Json
     */
    public static final String COMMON_EXPIRED_TOKEN = """
    {
        "message": "Authorization header is missing or invalid"
    }""" ;
}