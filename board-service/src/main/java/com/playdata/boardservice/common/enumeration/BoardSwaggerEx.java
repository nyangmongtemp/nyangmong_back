package com.playdata.boardservice.common.enumeration;

public class BoardSwaggerEx {


    public static final String BOARD_CREATE = """
            {
                "statusCode": 201,
                "statusMessage": "게시물 등록 성공",
                "result": 1
            }
            """;

    public static final String BOARD_UPDATE = """
            {
                "statusCode": 200
            }
            """;

    public static final String BOARD_DELETE = """
            {
                "statusCode": 200
            }
            """;
    
    public static final String BOARD_LIST = """
            {
                "content": [
                    {
                        "postid": 2,
                        "category": "INTRODUCTION",
                        "userid": 2,
                        "thumbnailimage": "your_image.jpg",
                        "content": "<p>게시물 내용</p>",
                        "createdat": "2025-07-30T16:44:38.873477",
                        "updatedat": "2025-08-01T11:06:56.943304",
                        "viewcount": 4,
                        "nickname": "쾌도홍길동",
                        "title": "게시물 제목",
                        "likeCount": 2,
                        "commentCount": 1
                    }
                ],
                "pageable": {
                    "pageNumber": 0,
                    "pageSize": 10,
                    "sort": [],
                    "offset": 0,
                    "paged": true,
                    "unpaged": false
                },
                "last": true,
                "totalElements": 1,
                "totalPages": 1,
                "size": 10,
                "number": 0,
                "sort": [],
                "first": true,
                "numberOfElements": 1,
                "empty": false
            }
            """;


    public static final String BOARD_DETAIL = """
            {
                "statusCode": 200,
                "statusMessage": "소개 게시물 조회 성공",
                "result": {
                    "postid": 2,
                    "category": "INTRODUCTION",
                    "userid": 2,
                    "thumbnailimage": "your_image.jpg",
                    "content": "<p>게시물 내용</p>",
                    "createdat": "2025-07-30T16:44:38.873477",
                    "updatedat": "2025-08-01T16:44:47.5541864",
                    "viewcount": 5,
                    "nickname": "쾌도홍길동",
                    "title": "게시물제목"
                }
            }
            """;

    public static final String RECENT_POST_LIST = """
            {
                "content": [
                    {
                        "postid": 2,
                        "category": "FREE",
                        "userid": 2,
                        "thumbnailimage": "your_image.jpg",
                        "content": "<p>게시물 내용</p>",
                        "createdat": "2025-07-30T16:44:38.873477",
                        "updatedat": "2025-08-01T11:06:56.943304",
                        "viewcount": 4,
                        "nickname": "쾌도홍길동",
                        "title": "게시물 제목",
                        "likeCount": 2,
                        "commentCount": 1
                    },
                    {
                        "postid": 4,
                        "category": "QUESTION",
                        "userid": 2,
                        "thumbnailimage": "your_image.jpg",
                        "content": "<p>게시물 내용</p>",
                        "createdat": "2025-07-30T16:44:38.873477",
                        "updatedat": "2025-08-01T11:06:56.943304",
                        "viewcount": 4,
                        "nickname": "쾌도홍길동",
                        "title": "게시물 제목",
                        "likeCount": 2,
                        "commentCount": 1
                    }
                ],
                "pageable": {
                    "pageNumber": 0,
                    "pageSize": 10,
                    "sort": [],
                    "offset": 0,
                    "paged": true,
                    "unpaged": false
                },
                "last": true,
                "totalElements": 1,
                "totalPages": 1,
                "size": 10,
                "number": 0,
                "sort": [],
                "first": true,
                "numberOfElements": 1,
                "empty": false
            }
            """;


    public static final String POPULAR_INTRO = """
            [
                {
                    "postId": 2,
                    "thumbnailImage": "f25fc652-1e4a-4a61-a4c3-52887a50f88c_2d7de0e3-c347-4c85-9442-9a1bf82cf47f_profile.jpg",
                    "viewCount": 5,
                    "nickname": "<script>이은혁",
                    "likeCount": 2,
                    "commentCount": 1
                },
                {
                    "postId": 3,
                    "thumbnailImage": "c7880915-cd0f-4ddb-86e9-2d5a42ee041b_32be3167-1826-4547-a443-1f73d18f1e31_profile.jpg",
                    "viewCount": 1,
                    "nickname": "왕자",
                    "likeCount": 1,
                    "commentCount": 0
                },
                {
                    "postId": 4,
                    "thumbnailImage": "60331456-f613-4a4c-95b8-523a16329daa_00459414-f69e-4612-9a87-e2cba14f9607_profile.jpg",
                    "viewCount": 1,
                    "nickname": "왕자",
                    "likeCount": 1,
                    "commentCount": 0
                }
            ]
            """;

    public static final String POPULAR = """
            [
                {
                    "createAt": "2025-08-01T17:00:09.141268",
                    "updateAt": "2025-08-01T17:01:09.247674",
                    "postId": 5,
                    "title": "후기 게시물 예시1",
                    "content": "<p>후기 게시물 예시1</p>",
                    "nickname": "왕자",
                    "thumbnailImage": null,
                    "category": "REVIEW",
                    "viewCount": 1
                },
                {
                    "createAt": "2025-08-01T17:00:14.931608",
                    "updateAt": "2025-08-01T17:01:07.419304",
                    "postId": 6,
                    "title": "후기 게시물 예시2",
                    "content": "<p>후기 게시물 예시2</p>",
                    "nickname": "왕자",
                    "thumbnailImage": null,
                    "category": "REVIEW",
                    "viewCount": 1
                },
                {
                    "createAt": "2025-08-01T17:00:22.402902",
                    "updateAt": "2025-08-01T17:01:05.79623",
                    "postId": 7,
                    "title": "후기 게시물 예시 3",
                    "content": "<p>후기 게시물 예시 3</p>",
                    "nickname": "왕자",
                    "thumbnailImage": null,
                    "category": "REVIEW",
                    "viewCount": 1
                },
                {
                    "createAt": "2025-08-01T17:00:29.691512",
                    "updateAt": "2025-08-01T17:01:00.492995",
                    "postId": 8,
                    "title": "후기 게시물 예시 4",
                    "content": "<p>후기 게시물 예시 4</p>",
                    "nickname": "왕자",
                    "thumbnailImage": null,
                    "category": "REVIEW",
                    "viewCount": 1
                },
                {
                    "createAt": "2025-08-01T17:00:36.700085",
                    "updateAt": "2025-08-01T17:01:04.147164",
                    "postId": 9,
                    "title": "후기 게시물 예시 5",
                    "content": "<p>후기 게시물 예시 5</p>",
                    "nickname": "왕자",
                    "thumbnailImage": null,
                    "category": "REVIEW",
                    "viewCount": 1
                },
                {
                    "createAt": "2025-08-01T17:00:58.487525",
                    "updateAt": "2025-08-01T17:01:02.715202",
                    "postId": 10,
                    "title": "후기 게시물 예시 6",
                    "content": "<p>후기 게시물 예시 6</p>",
                    "nickname": "왕자",
                    "thumbnailImage": null,
                    "category": "REVIEW",
                    "viewCount": 1
                }
            ]
            """;


    public static final String MINE = """
            {
                "statusCode": 200,
                "statusMessage": "내 정보 게시물 모두 찾음",
                "result": {
                    "content": [
                        {
                            "postid": 10,
                            "category": "REVIEW",
                            "userid": 1,
                            "thumbnailimage": null,
                            "content": "<p>후기 게시물 예시 6</p>",
                            "createdat": "2025-08-01T17:00:58.487525",
                            "updatedat": "2025-08-01T17:01:02.715202",
                            "viewcount": 1,
                            "nickname": "왕자",
                            "title": "후기 게시물 예시 6",
                            "likeCount": 0,
                            "commentCount": 0
                        },
                        {
                            "postid": 9,
                            "category": "REVIEW",
                            "userid": 1,
                            "thumbnailimage": null,
                            "content": "<p>후기 게시물 예시 5</p>",
                            "createdat": "2025-08-01T17:00:36.700085",
                            "updatedat": "2025-08-01T17:01:04.147164",
                            "viewcount": 1,
                            "nickname": "왕자",
                            "title": "후기 게시물 예시 5",
                            "likeCount": 0,
                            "commentCount": 0
                        },
                        {
                            "postid": 8,
                            "category": "REVIEW",
                            "userid": 1,
                            "thumbnailimage": null,
                            "content": "<p>후기 게시물 예시 4</p>",
                            "createdat": "2025-08-01T17:00:29.691512",
                            "updatedat": "2025-08-01T17:01:00.492995",
                            "viewcount": 1,
                            "nickname": "왕자",
                            "title": "후기 게시물 예시 4",
                            "likeCount": 0,
                            "commentCount": 0
                        },
                        {
                            "postid": 7,
                            "category": "REVIEW",
                            "userid": 1,
                            "thumbnailimage": null,
                            "content": "<p>후기 게시물 예시 3</p>",
                            "createdat": "2025-08-01T17:00:22.402902",
                            "updatedat": "2025-08-01T17:01:05.79623",
                            "viewcount": 1,
                            "nickname": "왕자",
                            "title": "후기 게시물 예시 3",
                            "likeCount": 0,
                            "commentCount": 0
                        },
                        {
                            "postid": 6,
                            "category": "REVIEW",
                            "userid": 1,
                            "thumbnailimage": null,
                            "content": "<p>후기 게시물 예시2</p>",
                            "createdat": "2025-08-01T17:00:14.931608",
                            "updatedat": "2025-08-01T17:01:07.419304",
                            "viewcount": 1,
                            "nickname": "왕자",
                            "title": "후기 게시물 예시2",
                            "likeCount": 0,
                            "commentCount": 0
                        },
                        {
                            "postid": 5,
                            "category": "REVIEW",
                            "userid": 1,
                            "thumbnailimage": null,
                            "content": "<p>후기 게시물 예시1</p>",
                            "createdat": "2025-08-01T17:00:09.141268",
                            "updatedat": "2025-08-01T17:01:09.247674",
                            "viewcount": 1,
                            "nickname": "왕자",
                            "title": "후기 게시물 예시1",
                            "likeCount": 0,
                            "commentCount": 0
                        }
                    ],
                    "pageable": {
                        "pageNumber": 0,
                        "pageSize": 10,
                        "sort": [
                            {
                                "direction": "DESC",
                                "property": "postId",
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
                    "last": true,
                    "totalElements": 6,
                    "totalPages": 1,
                    "size": 10,
                    "number": 0,
                    "sort": [
                        {
                            "direction": "DESC",
                            "property": "postId",
                            "ignoreCase": false,
                            "nullHandling": "NATIVE",
                            "ascending": false,
                            "descending": true
                        }
                    ],
                    "first": true,
                    "numberOfElements": 6,
                    "empty": false
                }
            }
            """;





    public static final String EMPTY_FILE = """
            {
                "code": "FILE-003",
                "message": "이미지는 필수입니다."
            }
            """;

    public static final String DUPLICATED_DATA = """
            {
                "code": "PARAM-006",
                "message": "이미 존재하는 이메일입니다."
            }
            """;

    public static final String FILE_INVALID_ERROR = """
            {
                "code": "FILE-008",
                "message": "업로드한 파일은 이미지가 아닙니다. (contentType 검사 실패)."
            }
            """;

    public static final String ACCOUNT_NOT_FOUND = """
            {
                "code": "ACCOUNT-004",
                "message": "회원가입이 되지 않은 이메일입니다."
            }
            """;

    public static final String ACCOUNT_DISABLED = """
            {
                "code": "ACCOUNT-006",
                "message": "계정이 비활성화 되었습니다."
            }
            """;

    public static final String INVALID_PASSWORD = """
            {
                "code": "ACCOUNT-007",
                "message": "비밀번호 오류!"
            }
            """;

    public static final String ACCOUNT_LOCKED = """
            {
                "code": "ACCOUNT-005",
                "message": "계정이 잠겼습니다."
            }
            """;

    public static final String EXPIRED_AUTH_CODE = """
            {
                "code": "ACCOUNT-010",
                "message": "인증코드가 만료되었습니다."
            }
            """;


    public static final String AUTH_DISALLOWED = """
            {
                "code": "ACCOUNT-014",
                "message": "이메일 인증 코드 발급 회수 초과, 30분동안 발급 불가"
            }
            """;


    public static final String INVALID_AUTH_CODE = """
            {
                "code": "ACCOUNT-009",
                "message": "인증코드가 틀렸습니다. 인증 기회는 3회 남았습니다."
            }
            """;

    public static final String INTERNAL_SERVER_ERROR = """
            {
                "code": "DEFAULT-008",
                "message": "서버에서 오류가 발생했습니다. or "
            }
            """;

    public static final String MAIL_SERVER_ERROR = """
            {
                "code": "MAIL-001",
                "message": "메일 서버에 오류가 발생했습니다."
            }
            """;

    public static final String BAD_REQUEST = """
            {
                "code": "DEFAULT-001",
                "message": "잘못된 요청입니다."
            }
            """;

    public static final String DATA_NOT_FOUND = """
            {
                "code": "DEFAULT-004",
                "message": "게시물을 찾을 수 없습니다."
            }
            """;
    
    public static final String UNAUTHORIZED = """
            {
                "code": "DEFAULT-002",
                "message": "게시물 수정 및 삭제 권한이 없습니다."
            }
            """;


}
