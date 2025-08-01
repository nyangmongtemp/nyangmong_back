package com.playdata.adminservice.common.enumeration;

/**
 * Swagger example Json 상수 관리
 */
public class SwaggerExampleConstants {

    /**
     * [약관 / 방침 / QNA] 목록 조회 Response Json
     */
    public static final String TERMS_LIST_RESPONSE = """
    {
        "statusCode": 200,
        "statusMessage": "목록 조회",
        "result": {
            "content": [
                {
                    "termsId": 5,
                    "title": "제목",
                    "content": "내용",
                    "adminName": "관리자이름",
                    "createAt": "2020-01-01T00:00:00.000000",
                    "updateAt": "2020-01-01T00:00:00.000000"
                },
                {
                    "termsId": 4,
                    "title": "제목",
                    "content": "내용",
                    "adminName": "관리자이름",
                    "createAt": "2020-01-01T00:00:00.000000",
                    "updateAt": "2020-01-01T00:00:00.000000"
                },
                {
                    "termsId": 3,
                    "title": "제목",
                    "content": "내용",
                    "adminName": "관리자이름",
                    "createAt": "2020-01-01T00:00:00.000000",
                    "updateAt": "2020-01-01T00:00:00.000000"
                },
                {
                    "termsId": 2,
                    "title": "제목",
                    "content": "내용",
                    "adminName": "관리자이름",
                    "createAt": "2020-01-01T00:00:00.000000",
                    "updateAt": "2020-01-01T00:00:00.000000"
                },
                {
                    "termsId": 1,
                    "title": "제목",
                    "content": "내용",
                    "adminName": "관리자이름",
                    "createAt": "2020-01-01T00:00:00.000000",
                    "updateAt": "2020-01-01T00:00:00.000000"
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
            "totalElements": 6,
            "last": true,
            "size": 10,
            "number": 0,
            "sort": [],
            "numberOfElements": 6,
            "first": true,
            "empty": false
        }
    }
    """;

    /**
     * [약관 / 방침 / QNA] 상세 조회 Response Json
     */
    public static final String TERMS_DETAIL_RESPONSE = """
    {
        "statusCode": 200,
        "statusMessage": "상세 조회",
        "result": {
            "title": "제목",
            "content": "<p>내용</p>",
            "adminName": "등록한 관리자 이름",
            "createAt": "2020-01-01T00:00:00.000000",
            "updateAt": "2020-01-01T00:00:00.000000"
        }
    }
    """;

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

    /**
     * [공통] 등록 완료 Response Json
     */
    public static final String CREATE_TERMS_SUCCESS = """
    {
        "statusCode": 201,
        "statusMessage": "등록 완료",
        "result": {
            "createAt": "2025-07-30T20:11:41.124507",
            "updateAt": "2025-07-30T20:11:41.124507",
            "termsId": 11,
            "adminId": 1,
            "title": "제목",
            "content": "내용",
            "category": "TERMS",
            "active": true,
            "adminName": null
        }
    }""";

    /**
     * [공통] 수정 완료 Response Json
     */
    public static final String UPDATE_TERMS_SUCCESS = """
    {
        "statusCode": 200,
        "statusMessage": "수정 완료",
            "result": {
            "createAt": "2025-07-26T11:33:50.507393",
            "updateAt": "2025-07-30T20:09:52.974359",
            "termsId": 1,
            "adminId": 1,
            "title": "제목",
            "content": "내용",
            "category": "TERMS",
            "active": true,
            "adminName": null
        }
    }""";

    /**
     * [공통] 삭제 완료 Response Json
     */
    public static final String DELETE_TERMS_SUCCESS = """
    {
        "statusCode": 200,
        "statusMessage": "삭제 완료",
            "result": {
            "createAt": "2025-07-26T11:29:38.380821",
            "updateAt": "2025-07-30T20:05:23.423013",
            "termsId": 1,
            "adminId": 1,
            "title": "제목",
            "content": "내용",
            "category": "TERMS",
            "active": false,
            "adminName": null
        }
    }""";

    /**
     * [공통] 마지막 글 NULL 일시 조회 Response Json
     */
    public static final String LAST_TERMS_NULL_SUCCESS = """
    {
        "statusCode": 200,
        "statusMessage": "등록된 약관이 없습니다.",
        "result": null
    }""";

    /**
     * [공통] 마지막 글 조회 Response Json
     */
    public static final String LAST_TERMS_SUCCESS = """
    {
        "statusCode": 200,
        "statusMessage": "약관 마지막 게시글 조회",
        "result": {
            "id": 1,
            "title": "제목",
            "content": "내용",
            "adminName": "총관리자",
            "createAt": "2025-07-26T11:29:38.380821",
            "updateAt": "2025-07-30T20:05:23.423013"
        }
    }""";

    /**
     * [사용자] 목록 조회 Response Json
     */
    public static final String USER_LIST_RESPONSE = """
    {
        "statusCode": 200,
        "statusMessage": "목록 조회",
        "result": {
            "content": [
                {
                    "userId": 1,
                    "userName": "테스터1",
                    "email": "test@test.com",
                    "nickname": "테스터1",
                    "active": true,
                    "pauseCount": 0,
                    "createAt": "2025-07-31T14:35:15.816936",
                    "reportCount": 0
                },
                {
                    "userId": 2,
                    "userName": "테스터2",
                    "email": "test2@test.com",
                    "nickname": "테스터2",
                    "active": true,
                    "pauseCount": 0,
                    "createAt": "2025-07-31T14:35:46.019624",
                    "reportCount": 0
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
            "totalPages": 1,
            "totalElements": 2,
            "last": true,
            "size": 10,
            "number": 0,
            "sort": [],
            "numberOfElements": 2,
            "first": true,
            "empty": false
        }
    }""";

    /**
     * [사용자] 상세 조회 Response Json
     */
    public static final String USER_DETAIL_RESPONSE = """
    {
        "statusCode": 200,
        "statusMessage": "상세 조회",
        "result": {
            "userName": "테스터1",
            "email": "test@test.com",
            "nickname": "테스터1",
            "address": null,
            "phone": null,
            "socialId": null,
            "socialProvider": null,
            "active": true,
            "reportCount": 0,
            "pauseCount": 0
        }
    }""";

    /**
     * [사용자] 신고 목록 조회 Response Json
     */
    public static final String USER_REPORT_LIST_RESPONSE = """
    {
        "statusCode": 200,
        "statusMessage": "신고내역 목록 조회",
        "result": [
            {
                "reportId": 1,
                "content": "fdafdf",
                "category": "COMMENT",
                "createAt": "2025-07-31T17:03:46.889282",
                "accusedUserId": 1,
                "reportUserName": "테스터2",
                "reportUserEmail": "test2@test.com",
                "accuseUserName": "테스터1",
                "accuseUserEmail": "test@test.com"
            }
        ]
    }""";

    /**
     * [사용자] 신고 확인 Response Json
     */
    public static final String USER_REPORT_TREAT_RESPONSE = """
    {
        "statusCode": 200,
        "statusMessage": "신고내역 확인처리",
        "result": {
            "createAt": "2025-07-31T17:03:46.889282",
            "updateAt": "2025-07-31T17:04:48.954847",
            "reportId": 1,
            "reportUserId": 2,
            "accusedUserId": 1,
            "adminId": 1,
            "content": "fdafdf",
            "category": "COMMENT",
            "treat": true,
            "reportUserName": null,
            "reportUserEmail": null,
            "accuseUserName": null,
            "accuseUserEmail": null
        }
    }""";

    /**
     * [사용자] 신고 확인 Response Json
     */
    public static final String USER_REPORT_RESPONSE = """
    {
        "statusCode": 200,
        "statusMessage": "사용자 정지",
        "result": {
        "reports": [
            {
                "createAt": "2025-07-31T17:13:08.573432",
                "updateAt": "2025-07-31T17:13:32.656076",
                "reportId": 4,
                "reportUserId": 2,
                "accusedUserId": 1,
                "adminId": 1,
                "content": "Af\\n\\ndf\\n\\naf\\naed\\nf\\nadf\\nd",
                "category": "COMMENT",
                "treat": true,
                "reportUserName": null,
                "reportUserEmail": null,
                "accuseUserName": null,
                "accuseUserEmail": null
            }
        ],
            "user": {
                "createAt": "2025-07-31T14:35:15.816936",
                "updateAt": "2025-07-31T17:13:32.671676",
                "userId": 1,
                "userName": "테스터1",
                "email": "test@test.com",
                "password": "$2a$10$4LXE7JoifOTsW4q/NGUsZ.kOMTn7pz0MnH0bHQjPyf7ZAT2Kwvceq",
                "profileImage": "default_user.png",
                "nickname": "테스터1",
                "address": null,
                "phone": null,
                "socialId": null,
                "grade": 0,
                "socialProvider": null,
                "active": false,
                "passwordUpdatedAt": "2025-07-31T14:35:15.779416",
                "passwordFaultCount": 0,
                "pauseCount": 1,
                "releaseAt": "2025-08-30T17:13:32.654336",
                "reportCount": 0
            }
        }
    }""";

    /**
     * [사용자] 목록 조회 Response Json
     */
    public static final String ADMIN_LOG_LIST_RESPONSE = """
    {
        "statusCode": 200,
        "statusMessage": "목록 조회",
        "result": {
        "content": [
            {
                "logId": 1,
                "adminId": 1,
                "userId": 1,
                "adminIp": "192.168.0.155",
                "userName": "테스터1",
                "userEmail": "test@test.com",
                "userNickName": "테스터1",
                "adminName": "총관리자",
                "createAt": "2025-07-31T16:37:22.699758"
            },
            {
                "logId": 2,
                "adminId": 1,
                "userId": 1,
                "adminIp": "192.168.0.155",
                "userName": "테스터1",
                "userEmail": "test@test.com",
                "userNickName": "테스터1",
                "adminName": "총관리자",
                "createAt": "2025-07-31T16:50:22.589318"
            },
            {
                "logId": 3,
                "adminId": 1,
                "userId": 1,
                "adminIp": "192.168.0.155",
                "userName": "테스터1",
                "userEmail": "test@test.com",
                "userNickName": "테스터1",
                "adminName": "총관리자",
                "createAt": "2025-07-31T17:21:44.884364"
            },
            {
                "logId": 4,
                "adminId": 1,
                "userId": 2,
                "adminIp": "192.168.0.155",
                "userName": "테스터2",
                "userEmail": "test2@test.com",
                "userNickName": "테스터2",
                "adminName": "총관리자",
                "createAt": "2025-07-31T17:21:48.570728"
            },
            {
                "logId": 5,
                "adminId": 1,
                "userId": 1,
                "adminIp": "192.168.0.155",
                "userName": "테스터1",
                "userEmail": "test@test.com",
                "userNickName": "테스터1",
                "adminName": "총관리자",
                "createAt": "2025-07-31T17:21:51.670787"
            },
            {
                "logId": 6,
                "adminId": 1,
                "userId": 2,
                "adminIp": "192.168.0.155",
                "userName": "테스터2",
                "userEmail": "test2@test.com",
                "userNickName": "테스터2",
                "adminName": "총관리자",
                "createAt": "2025-07-31T17:21:55.462232"
            },
            {
                "logId": 7,
                "adminId": 1,
                "userId": 1,
                "adminIp": "192.168.0.155",
                "userName": "테스터1",
                "userEmail": "test@test.com",
                "userNickName": "테스터1",
                "adminName": "총관리자",
                "createAt": "2025-07-31T17:21:58.02837"
            },
            {
                "logId": 8,
                "adminId": 1,
                "userId": 2,
                "adminIp": "192.168.0.155",
                "userName": "테스터2",
                "userEmail": "test2@test.com",
                "userNickName": "테스터2",
                "adminName": "총관리자",
                "createAt": "2025-07-31T17:22:00.659286"
            },
            {
                "logId": 9,
                "adminId": 1,
                "userId": 2,
                "adminIp": "192.168.0.155",
                "userName": "테스터2",
                "userEmail": "test2@test.com",
                "userNickName": "테스터2",
                "adminName": "총관리자",
                "createAt": "2025-07-31T17:22:01.390493"
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
            "totalElements": 9,
            "last": true,
            "size": 10,
            "number": 0,
            "sort": [],
            "numberOfElements": 9,
            "first": true,
            "empty": false
        }
    }""";

    /**
     * [문의] 목록 조회 Response Json
     */
    public static final String INFORM_LIST_RESPONSE = """
    {
        "statusCode": 200,
        "statusMessage": "목록 조회",
        "result": {
        "content": [
            {
                "informId": 1,
                "title": "문의문의문의문의",
                "answered": true,
                "createAt": "2025-08-01T09:47:01.20162",
                "userName": "테스터2",
                "userEmail": "test2@test.com"
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
            "totalElements": 1,
            "last": true,
            "size": 10,
            "number": 0,
            "sort": [],
            "numberOfElements": 1,
            "first": true,
            "empty": false
        }
    }""";

    /**
     * [문의] 상세 조회 Response Json
     */
    public static final String INFORM_DETAIL_RESPONSE = """
    {
        "statusCode": 200,
        "statusMessage": "상세 조회",
        "result": {
            "title": "문의문의문의문의",
            "content": "내용내용내용내용내용내용",
            "reply": "asfadfsdfasdfadfadsfadf",
            "answered": true,
            "createAt": "2025-08-01T09:47:01.20162",
            "userName": "테스터2",
            "userEmail": "test2@test.com",
            "updateAt": "2025-08-01T11:19:38.897368",
            "adminName": "총관리자"
        }
    }""";

    /**
     * [문의] 답변 성공 Response Json
     */
    public static final String INFORM_REPLY_RESPONSE = """
    {
        "statusCode": 200,
        "statusMessage": "답변 등록",
        "result": {
            "createAt": "2025-08-01T09:47:01.20162",
            "updateAt": "2025-08-01T12:45:02.64143",
            "informId": 1,
            "userId": 2,
            "adminId": 1,
            "title": "문의문의문의문의",
            "content": "내용내용내용내용내용내용",
            "active": true,
            "answered": true,
            "reply": "3131",
            "userName": null,
            "userEmail": null,
            "adminName": null
        }
    }""";
}