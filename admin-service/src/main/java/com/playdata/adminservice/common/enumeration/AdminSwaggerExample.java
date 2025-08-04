package com.playdata.adminservice.common.enumeration;

public class AdminSwaggerExample {

    public static final String ADMIN_CREATE = """
            {
                "statusCode": 201,
                "statusMessage": "관리자 생성에 성공하였습니다.",
                "result": true
            }
            """;

    public static final String ADMIN_LOGIN_EMAIL = """
        {
                 "statusCode": 200,
                 "statusMessage": "회원가입 인증코드가 이메일로 발송되었습니다.",
                 "result": "3025"
        }
        """;

    public static final String ADMIN_LOGIN = """
            {
                "statusCode": 200,
                "statusMessage": "로그인에 성공하였습니다.",
                "result": {
                    "email": "example@example.com",
                    "name": "your_name",
                    "role": "BOSS",
                    "token": "your_token",
                    "isFirst": false
                }
            }
            """;

    public static final String ADMIN_EMAIL_CODE = """
            {
            "statusCode": 200,
            "statusMessage": "인증코드가 새로운 이메일로 발송되었습니다.",
            "result": "1433"
            }
            """;

    public static final String ADMIN_EMAIL_VERIFY = """
            {
                "statusCode": 200,
                "statusMessage": "인증되었습니다.",
                "result": true
            }
            """;

    public static final String ADMIN_PASSWORD = """
            {
                "statusCode": 200,
                "statusMessage": "인증 코드가 이메일로 전송되었습니다.",
                "result": "7109"
            }
            """;

    public static final String ADMIN_PASSWORD_VERIFY = """
            {
                 "statusCode": 200,
                 "statusMessage": "인증되었습니다.",
                 "result": true
            }
            """;

    public static final String ADMIN_PASSWORD_MODIFY = """
            {
                "statusCode": 200,
                "statusMessage": "비밀번호를 변경 하였습니다. 다시 로그인 해주세요",
                "result": true
            }
            """;

    public static final String ADMIN_MODIFY = """
            {
                "statusCode": 200,
                "statusMessage": "수정에 성공하였습니다.",
                "result": true
            }
            """;

    public static final String ADMIN_LIST = """
            {
                "content": [
                    {
                        "adminId": 2,
                        "email": "example@example.com",
                        "name": "name",
                        "phone": "010-1234-1234",
                        "password": "INCODE_PASSWORD",
                        "role": "CUSTOMER",
                        "active": true,
                        "first": false
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
                "totalPages": 1,
                "totalElements": 6,
                "size": 10,
                "number": 0,
                "sort": [],
                "first": true,
                "numberOfElements": 6,
                "empty": false
            }
            """;

    public static final String ADMIN_ROLE_MODIFY = """
            {
                "statusCode": 200,
                "statusMessage": "권한/활성화 상태가 수정되었습니다.",
                "result": true
            }
            """;

    public static final String ADMIN_MYPAGE = """
            {
                "statusCode": 200,
                "statusMessage": "마이페이지 정보 응답",
                "result": {
                    "adminId": 1,
                    "name": "your_name",
                    "email": "example@example.com",
                    "createAt": "2025-07-21T17:26:11.003436",
                    "updateAt": "2025-07-24T00:28:53.200183",
                    "phone": "010-9534-3892",
                    "role": "BOSS"
                }
            }
            """;

    public static final String BAD_REQUEST = """
            {
                "code": "DEFAULT-001",
                "message": "잘못된 요청입니다."
            }
            """;

    public static final String DUPLICATED_DATA = """
            {
                "code": "PARAM-006",
                "message": "이미 존재하는 이메일입니다."
            }
            """;

    public static final String ACCOUNT_NOT_FOUND = """
            {
                "code": "ACCOUNT-004",
                "message": "아이디 혹은 패스워드를 다시 확인해 주세요."
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
                "message": "아이디 혹은 패스워드를 다시 확인해 주세요."
            }
            """;

    public static final String INVALID_PARAMETER = """
            {
                "code": "PARAM-001",
                "message": "입력 데이터가 올바르지 않습니다."
            }
            """;

    public static final String INVALID_AUTH_CODE = """
            {
                "code": "DEFAULT-001",
                "message": "잘못된 요청입니다."
            }
            """;

    public static final String FORBIDDEN = """
            {
                "code": "AUTHORITY-003",
                "message": "접근권한이 없는 관리자입니다."
            }
            """;

}
