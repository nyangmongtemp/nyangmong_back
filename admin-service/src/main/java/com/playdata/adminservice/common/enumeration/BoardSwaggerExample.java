package com.playdata.adminservice.common.enumeration;

public class BoardSwaggerExample {

    public static final String ADMIN_LIST = """
            {
                 "content": [
                     {
                         "createAt": "2025-07-29T12:35:44.377957",
                         "updateAt": "2025-07-29T15:16:27.006088",
                         "postId": 6,
                         "title": "title",
                         "content": "content",
                         "nickname": "nickname",
                         "thumbnailImage": "5dd3e623-ec86-4a86-9bf9-44ee862859b3_스크린샷 2025-07-01 145315.png",
                         "category": "QUESTION",
                         "viewCount": 1
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
                 "last": true,
                 "totalPages": 1,
                 "totalElements": 4,
                 "size": 10,
                 "number": 0,
                 "sort": [],
                 "first": true,
                 "numberOfElements": 4,
                 "empty": false
             }
            """;

    public static final String ADMIN_ANIMAL_LIST = """
            {
                "statusCode": 200,
                "statusMessage": "목록 조회",
                "result": {
                    "content": [
                        {
                            "postId": 2,
                            "userId": 5,
                            "thumbnailImage": "a239e05d-18c1-4000-a590-85d045c717f7_스크린샷 2025-07-02 150849.png",
                            "title": "title",
                            "content": "content",
                            "viewCount": 0,
                            "petCategory": "고양이",
                            "petKind": "믹스",
                            "age": "1",
                            "vaccine": "",
                            "sexCode": "F",
                            "neuterYn": "N",
                            "address": "신림동",
                            "fee": 32,
                            "active": true,
                            "reservationStatus": "A",
                            "createAt": "2025-08-01T17:47:52.000033",
                            "nickname": "nickname",
                            "likeCount": null,
                            "commentCount": null
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
                    "last": true,
                    "totalPages": 1,
                    "totalElements": 2,
                    "size": 10,
                    "number": 0,
                    "sort": [],
                    "first": true,
                    "numberOfElements": 2,
                    "empty": false
                }
            }
            """;

    public static final String BOARD_DETAIL = """
            {
                "statusCode": 200,
                "statusMessage": "소개 게시물 조회 성공",
                "result": {
                    "postid": 6,
                    "category": "QUESTION",
                    "userid": 5,
                    "thumbnailimage": "5dd3e623-ec86-4a86-9bf9-44ee862859b3_스크린샷 2025-07-01 145315.png",
                    "content": "content",
                    "createdat": "2025-07-29T12:35:44.377957",
                    "updatedat": "2025-07-29T15:16:27.006088",
                    "viewcount": 1,
                    "nickname": "nickname",
                    "title": "title"
                }
            }
            """;

    public static final String BOARD_ANIMAL_DETAIL = """
            {
                "statusCode": 200,
                "statusMessage": "상세 조회",
                "result": {
                    "createAt": "2025-07-30T12:30:53.776332",
                    "updateAt": "2025-08-01T16:43:39.091157",
                    "postId": 1,
                    "userId": 5,
                    "nickname": "nickname",
                    "thumbnailImage": "d965bf97-807c-412b-8bb5-ff3dfe5b5cb4_스크린샷 2025-07-01 094852.png",
                    "title": "title",
                    "content": "title",
                    "viewCount": 3,
                    "petCategory": "고양이",
                    "petKind": "믹스",
                    "age": "1",
                    "vaccine": "ㅁㄴㅇㅁㄴㅇ",
                    "sexCode": "M",
                    "neuterYn": "N",
                    "address": "강남구",
                    "fee": 20,
                    "active": true,
                    "reservationStatus": "A"
                }
            }
            """;

    public static final String BOARD_DELETE = """
            
            """;

    public static final String FORBIDDEN = """
            {
                "code": "AUTHORITY-003",
                "message": "접근권한이 없는 관리자입니다."
            }
            """;

    public static final String DATE_NOT_FOUND = """
            {
                "code": "GET-002",
                "message": "요청 데이터가 존재하지 않습니다."
            }
            """;
}
