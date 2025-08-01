package com.playdata.userservice.common.enumeration;

public class UserSwaggerEx {


    public static final String USER_CREATE = """
            {
                "statusCode": 201,
                "statusMessage": "회원가입에 성공하였습니다",
                "result": true
            }
            """;


    public static final String USER_LOGIN = """
            {
                "statusCode": 200,
                "statusMessage": "로그인에 성공하였습니다.",
                "result": {
                    "email": "example@example.com",
                    "nickname": "닉네임",
                    "profileImage": "profile.jpg",
                    "token": "your_Access_token"
                }
            }
            """;


    public static final String SEND_VERIFY_CODE = """
            {
                "statusCode": 200,
                "statusMessage": "회원가입 인증코드가 이메일로 발송되었습니다."
            }
            """;


    public static final String VERIFY_CODE = """
            {
                "statusCode": 200,
                "statusMessage": "인증되었습니다.",
                "result": true
            }
            """;


    public static final String TRUE = """
            {
                true
            }
            """;

    public static final String EMAIL_VERIFY_CODE = """
            {
                "statusCode": 200,
                "statusMessage": "회원정보 수정 인증코드가 이메일로 발송되었습니다."
            }
            """;

    public static final String MODIFY_NEW_PASSWORD = """
            {
                "statusCode": 200,
                "statusMessage": "비밀번호가 변경되었습니다. 다시 로그인 해주세요",
                "result": true
            }
            """;

    public static final String FORGET_PASSWORD_REQ = """
            {
                "statusCode": 200,
                "statusMessage": "인증 이메일이 전송됨"
            }
            """;

    public static final String NEW_PASSWORD = """
            {
                "statusCode": 200,
                "statusMessage": "임시 비밀번호 발급 완료"
            }
            """;

    public static final String MY_PAGE = """
            {
                "statusCode": 200,
                "statusMessage": "해당 유저의 정보를 찾음.",
                "result": {
                    "email": "moon111@naver.com",
                    "userName": "문동주",
                    "nickname": "왕자",
                    "createAt": "2025-07-30T11:02:08.051328",
                    "profileImage": "3fe0d921-2a68-4199-8b1d-5ddd8dd5c163_profile.jpg"
                }
            }
            """;

    public static final String RESIGN = """
            {
                "statusCode": 200,
                "statusMessage": "회원 탈퇴가 정상적으로 진행되었습니다.",
                "result": null
            }
            """;


    public static final String SEARCH = """
            {
                "statusCode": 200,
                "statusMessage": "검색된 회원 목록 조회",
                "result": [
                    {
                        "userId": 4,
                        "username": "문동주",
                        "nickname": "대전왕자",
                        "createAt": "2025-08-01T12:08:21.063145",
                        "profileImage": "default_user.png"
                    }
                ]
            }
            """;

    public static final String MY_CHAT = """
            {
                "statusCode": 200,
                "statusMessage": "사용자의 채팅방 모두 조회됨.",
                "result": [
                    {
                        "chatId": 11,
                        "userId1": 2,
                        "userId2": 1,
                        "createAt": "2025-07-30T12:43:11.140555",
                        "updateAt": "2025-07-30T12:43:11.140555",
                        "nickname1": "<script>이은혁",
                        "nickname2": "왕자",
                        "requestNickname": "왕자",
                        "message": {
                            "senderId": 2,
                            "receiverId": 1,
                            "messageId": 189,
                            "content": "잘 보내지는듯?",
                            "createAt": "2025-07-30T12:47:43.686528",
                            "active": true,
                            "chatId": 11,
                            "readed": true,
                            "nickname1": "<script>이은혁",
                            "nickname2": "왕자",
                            "requestNickname": "왕자"
                        }
                    },
                    {
                        "chatId": 12,
                        "userId1": 3,
                        "userId2": 1,
                        "createAt": "2025-08-01T14:22:30.01449",
                        "updateAt": "2025-08-01T14:22:30.01449",
                        "nickname1": "김친절",
                        "nickname2": "왕자",
                        "requestNickname": "왕자",
                        "message": {
                            "senderId": 1,
                            "receiverId": 3,
                            "messageId": 190,
                            "content": "헬로",
                            "createAt": "2025-08-01T14:22:30.103413",
                            "active": true,
                            "chatId": 12,
                            "readed": false,
                            "nickname1": "김친절",
                            "nickname2": "왕자",
                            "requestNickname": "왕자"
                        }
                    },
                    {
                        "chatId": 13,
                        "userId1": 4,
                        "userId2": 1,
                        "createAt": "2025-08-01T14:22:41.778765",
                        "updateAt": "2025-08-01T14:22:41.778765",
                        "nickname1": "대전왕자",
                        "nickname2": "왕자",
                        "requestNickname": "왕자",
                        "message": {
                            "senderId": 1,
                            "receiverId": 4,
                            "messageId": 192,
                            "content": "하이하이하이하이",
                            "createAt": "2025-08-01T14:23:27.884343",
                            "active": true,
                            "chatId": 13,
                            "readed": false,
                            "nickname1": "대전왕자",
                            "nickname2": "왕자",
                            "requestNickname": "왕자"
                        }
                    }
                ]
            }
            """;


    public static final String FIRST_CHAT= """
            {
                "statusCode": 200,
                "statusMessage": "생성된 채팅방이 없습니다.",
                "result": null
            }
            """;

    public static final String CLEAR_CHAT = """
            {
                "statusCode": 200,
                "statusMessage": "해당 채팅방 삭제됨.",
                "result": true
            }
            """;

    public static final String CHAT_LIST = """
            {
                "statusCode": 200,
                "statusMessage": "채팅방의 7일간 메시지 조회됨.",
                "result": [
                    {
                        "senderId": 1,
                        "receiverId": 2,
                        "messageId": 187,
                        "content": "쪽지를 보내본다!",
                        "createAt": "2025-07-30T12:43:11.285477",
                        "active": true,
                        "chatId": 11,
                        "readed": true,
                        "nickname1": "<script>이은혁",
                        "nickname2": "왕자",
                        "requestNickname": "왕자"
                    },
                    {
                        "senderId": 2,
                        "receiverId": 1,
                        "messageId": 188,
                        "content": "q보낸다!",
                        "createAt": "2025-07-30T12:47:39.522451",
                        "active": true,
                        "chatId": 11,
                        "readed": true,
                        "nickname1": "<script>이은혁",
                        "nickname2": "왕자",
                        "requestNickname": "왕자"
                    },
                    {
                        "senderId": 2,
                        "receiverId": 1,
                        "messageId": 189,
                        "content": "잘 보내지는듯?",
                        "createAt": "2025-07-30T12:47:43.686528",
                        "active": true,
                        "chatId": 11,
                        "readed": true,
                        "nickname1": "<script>이은혁",
                        "nickname2": "왕자",
                        "requestNickname": "왕자"
                    },
                    {
                        "senderId": 1,
                        "receiverId": 2,
                        "messageId": 193,
                        "content": "헤이헤이",
                        "createAt": "2025-08-01T14:35:06.000592",
                        "active": true,
                        "chatId": 11,
                        "readed": false,
                        "nickname1": "<script>이은혁",
                        "nickname2": "왕자",
                        "requestNickname": "왕자"
                    }
                ]
            }
            """;


    public static final String SEND_MESSAGE = """
            {
                "statusCode": 201,
                "statusMessage": "메시지 전송됨",
                "result": {
                    "senderId": 1,
                    "receiverId": 2,
                    "messageId": 194,
                    "content": "임시 메시지 전송 예시",
                    "createAt": "2025-08-01T14:38:07.091432",
                    "active": true,
                    "chatId": 11,
                    "readed": false,
                    "nickname1": "<script>이은혁",
                    "nickname2": "왕자",
                    "requestNickname": "왕자"
                }
            }
            """;


    public static final String INFORM_CREATE = """
            {
                "statusCode": 201,
                "statusMessage": "고객문의 생성됨",
                "result": {
                    "informId": 15,
                    "userId": 1,
                    "adminId": null,
                    "title": "임시 문의 제목",
                    "content": "임시 문의 내용",
                    "reply": null,
                    "answered": false,
                    "nickname": "왕자",
                    "updateAt": "2025-08-01T14:42:12.421136"
                }
            }
            """;


    public static final String INFORM_MODIFY = """
            {
                "statusCode": 200,
                "statusMessage": "문의 수정됨",
                "result": {
                    "informId": 15,
                    "userId": 1,
                    "adminId": null,
                    "title": "수정된 제목",
                    "content": "수정된 내용",
                    "reply": null,
                    "answered": false,
                    "nickname": "왕자",
                    "updateAt": "2025-08-01T14:46:02.956365"
                }
            }
            """;


    public static final String DELETE_INFORM = """
            {
                "statusCode": 200,
                "statusMessage": "문의 삭제됨.",
                "result": true
            }
            """;
    
    public static final String INFORM_LIST = """
            {
                "statusCode": 200,
                "statusMessage": "문의글들 조회됨",
                "result": {
                    "content": [
                        {
                            "informId": 16,
                            "userId": 1,
                            "title": "임시 문의 제목",
                            "answered": false,
                            "createAt": "2025-08-01T15:08:52.395056",
                            "updateAt": "2025-08-01T15:08:52.395056",
                            "nickname": "왕자"
                        },
                        {
                            "informId": 15,
                            "userId": 1,
                            "title": "수정된 제목",
                            "answered": false,
                            "createAt": "2025-08-01T14:42:12.421136",
                            "updateAt": "2025-08-01T14:46:02.956365",
                            "nickname": "왕자"
                        },
                        {
                            "informId": 14,
                            "userId": 1,
                            "title": "gldgldlgld",
                            "answered": false,
                            "createAt": "2025-08-01T11:18:55.162176",
                            "updateAt": "2025-08-01T11:18:55.162176",
                            "nickname": "왕자"
                        }
                    ],
                    "pageable": {
                        "pageNumber": 0,
                        "pageSize": 5,
                        "sort": [
                            {
                                "direction": "DESC",
                                "property": "createAt",
                                "ignoreCase": false,
                                "nullHandling": "NATIVE",
                                "descending": true,
                                "ascending": false
                            }
                        ],
                        "offset": 0,
                        "paged": true,
                        "unpaged": false
                    },
                    "last": true,
                    "totalElements": 3,
                    "totalPages": 1,
                    "size": 5,
                    "number": 0,
                    "sort": [
                        {
                            "direction": "DESC",
                            "property": "createAt",
                            "ignoreCase": false,
                            "nullHandling": "NATIVE",
                            "descending": true,
                            "ascending": false
                        }
                    ],
                    "first": true,
                    "numberOfElements": 3,
                    "empty": false
                }
            }
            """;
    
    

    public static final String REPORT = """
            {
                "statusCode": 201,
                "statusMessage": "신고 생성됨.",
                "result": true
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
                "message": "소셜 로그인한 회원은 임시비밀번호 발급 불가 or 유효하지 않은 회원
                or 수정 권한이 없음. or 삭제 권한이 없음 or 잘못된 요청값입니다."
            }
            """;

    public static final String NOT_FOUND = """
            {
                "code": "DEFAULT-004",
                "message": "리소스를 찾을 수 없습니다."
            }
            """;

}
