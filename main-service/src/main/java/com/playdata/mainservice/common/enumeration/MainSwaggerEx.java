package com.playdata.mainservice.common.enumeration;

public class MainSwaggerEx {

    public static final String LIKE_CREATE = """
            {
                "statusCode": 201,
                "statusMessage": "좋아요가 생성됨",
                "result": true
            }
            """;


    public static final String COMMENT_CREATE = """
            {
                "statusCode": 201,
                "statusMessage": "댓글이 생성됨",
                "result": {
                    "userId": 1,
                    "commentId": 32,
                    "nickname": "대전왕자",
                    "contentId": 1,
                    "category": "ADOPT",
                    "content": "댓글 생성 예시",
                    "createAt": "2025-07-31T15:44:22.606908",
                    "profileImage": "3fe0d921-2a68-4199-8b1d-5ddd8dd5c163_profile.jpg",
                    "likeCount": null,
                    "hidden": false,
                    "reply": false
                }
            }
            """;

    public static final String COMMENT_DELETE = """
            {
                "statusCode": 200,
                "statusMessage": "댓글이 삭제됨",
                "result": true
            }
            """;

    public static final String COMMENT_MODIFY = """
            {
                "statusCode": 200,
                "statusMessage": "댓글 내용이 수정되었습니다.",
                "result": {
                    "userId": 1,
                    "commentId": 30,
                    "nickname": "대전왕자",
                    "contentId": 1,
                    "category": "QUESTION",
                    "content": "수정할 댓글 내용입니다.",
                    "createAt": "2025-07-30T12:18:48.003915",
                    "profileImage": "3fe0d921-2a68-4199-8b1d-5ddd8dd5c163_profile.jpg",
                    "likeCount": null,
                    "hidden": false,
                    "reply": true
                }
            }
            """;

    public static final String REPLY_CREATE = """
            {
                "statusCode": 201,
                "statusMessage": "대댓글이 생성되었습니다.",
                "result": {
                    "replyId": 36,
                    "content": "대댓글을 달기 예시",
                    "commentId": 30,
                    "createAt": "2025-07-31T16:04:52.091763",
                    "userId": 1,
                    "nickname": "대전왕자",
                    "profileImage": "3fe0d921-2a68-4199-8b1d-5ddd8dd5c163_profile.jpg",
                    "likeCount": 0
                }
            }
            """;


    public static final String REPLY_DELETE = """
            {
                "statusCode": 200,
                "statusMessage": "대댓글이 삭제됨",
                "result": true
            }
            """;

    public static final String POST_DETAIL = """
            {
                "statusCode": 200,
                "statusMessage": "해당 게시물의 좋아요, 댓글 개수 리턴",
                "result": {
                    "contentId": 1,
                    "category": "adopt",
                    "commentCount": 1,
                    "likeCount": 1
                }
            }
            """;

    public static final String COMMENT_DETAIL = """
            {
                "statusCode": 200,
                "statusMessage": "해당 게시물의 모든 댓글 정보 조회",
                "result": {
                    "content": [
                        {
                            "userId": 1,
                            "commentId": 32,
                            "nickname": "대전왕자",
                            "contentId": 1,
                            "category": "ADOPT",
                            "content": "댓글 생성 예시",
                            "createAt": "2025-07-31T15:44:22.606908",
                            "profileImage": "3fe0d921-2a68-4199-8b1d-5ddd8dd5c163_profile.jpg",
                            "likeCount": null,
                            "hidden": false,
                            "reply": false
                        }
                    ],
                    "pageable": {
                        "pageNumber": 0,
                        "pageSize": 10,
                        "sort": [
                            {
                                "direction": "ASC",
                                "property": "createAt",
                                "ignoreCase": false,
                                "nullHandling": "NATIVE",
                                "ascending": true,
                                "descending": false
                            }
                        ],
                        "offset": 0,
                        "paged": true,
                        "unpaged": false
                    },
                    "totalPages": 1,
                    "totalElements": 1,
                    "last": true,
                    "size": 10,
                    "number": 0,
                    "sort": [
                        {
                            "direction": "ASC",
                            "property": "createAt",
                            "ignoreCase": false,
                            "nullHandling": "NATIVE",
                            "ascending": true,
                            "descending": false
                        }
                    ],
                    "first": true,
                    "numberOfElements": 1,
                    "empty": false
                }
            }
            """;

    public static final String REPLY_DETAIL = """
            {
                "statusCode": 200,
                "statusMessage": "해당 댓글의 모든 대댓글 찾음",
                "result": [
                    {
                        "replyId": 15,
                        "content": "답글도 작성해본다!",
                        "commentId": 26,
                        "createAt": "2025-07-30T12:14:02.216309",
                        "userId": 1,
                        "nickname": "대전왕자",
                        "profileImage": "3fe0d921-2a68-4199-8b1d-5ddd8dd5c163_profile.jpg",
                        "likeCount": 0
                    },
                    {
                        "replyId": 16,
                        "content": "답글도 작성해본다!",
                        "commentId": 26,
                        "createAt": "2025-07-30T12:14:07.53046",
                        "userId": 1,
                        "nickname": "대전왕자",
                        "profileImage": "3fe0d921-2a68-4199-8b1d-5ddd8dd5c163_profile.jpg",
                        "likeCount": 0
                    }
                ]
            }
            """;

    public static final String MY_COMMENT = """
            {
                "statusCode": 200,
                "statusMessage": "사용자의 모든 댓글 정보 조회",
                "result": {
                    "content": [
                        {
                            "userId": 1,
                            "commentId": 32,
                            "nickname": "대전왕자",
                            "contentId": 1,
                            "category": "ADOPT",
                            "content": "댓글 생성 예시",
                            "createAt": "2025-07-31T15:44:22.606908",
                            "profileImage": "3fe0d921-2a68-4199-8b1d-5ddd8dd5c163_profile.jpg",
                            "likeCount": null,
                            "hidden": false,
                            "reply": false
                        },
                        {
                            "userId": 1,
                            "commentId": 31,
                            "nickname": "대전왕자",
                            "contentId": 3,
                            "category": "ADOPT",
                            "content": "댓글 달기",
                            "createAt": "2025-07-31T14:15:57.494937",
                            "profileImage": "3fe0d921-2a68-4199-8b1d-5ddd8dd5c163_profile.jpg",
                            "likeCount": null,
                            "hidden": false,
                            "reply": false
                        },
                        {
                            "userId": 1,
                            "commentId": 30,
                            "nickname": "대전왕자",
                            "contentId": 1,
                            "category": "QUESTION",
                            "content": "수정할 댓글 내용입니다.",
                            "createAt": "2025-07-30T12:18:48.003915",
                            "profileImage": "3fe0d921-2a68-4199-8b1d-5ddd8dd5c163_profile.jpg",
                            "likeCount": null,
                            "hidden": false,
                            "reply": true
                        },
                        {
                            "userId": 1,
                            "commentId": 29,
                            "nickname": "대전왕자",
                            "contentId": 3,
                            "category": "ADOPT",
                            "content": "대댓글을 만들어보자",
                            "createAt": "2025-07-30T12:15:48.385828",
                            "profileImage": "3fe0d921-2a68-4199-8b1d-5ddd8dd5c163_profile.jpg",
                            "likeCount": null,
                            "hidden": false,
                            "reply": true
                        },
                        {
                            "userId": 1,
                            "commentId": 26,
                            "nickname": "대전왕자",
                            "contentId": 1,
                            "category": "QUESTION",
                            "content": "댓글도 작성해본다!",
                            "createAt": "2025-07-30T12:13:56.121705",
                            "profileImage": "3fe0d921-2a68-4199-8b1d-5ddd8dd5c163_profile.jpg",
                            "likeCount": null,
                            "hidden": false,
                            "reply": true
                        }
                    ],
                    "pageable": {
                        "pageNumber": 0,
                        "pageSize": 10,
                        "sort": [
                            {
                                "direction": "DESC",
                                "property": "commentId",
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
                    "totalPages": 1,
                    "totalElements": 5,
                    "last": true,
                    "size": 10,
                    "number": 0,
                    "sort": [
                        {
                            "direction": "DESC",
                            "property": "commentId",
                            "ignoreCase": false,
                            "nullHandling": "NATIVE",
                            "ascending": false,
                            "descending": true
                        }
                    ],
                    "first": true,
                    "numberOfElements": 5,
                    "empty": false
                }
            }
            """;

    public static final String CAN_SEE = """
            {
                "statusCode": 200,
                "result": true
            }
            """;


    public static final String LIKED = """
            {
                "statusCode": 200,
                "statusMessage": "해당 게시물의 사용자의 좋아요 정보",
                "result": true
            }
            """;




    public static final String INTERNAL_SERVER_ERROR = """
            {
                "code": "DEFAULT-008",
                "message": "댓글 생성 중에 에러가 발생하였습니다. or "
            }""";

    public static final String NO_COMMENT = """
            {
                "code": "DEFAULT-004",
                "message": "해당 댓글이 존재하지 않습니다. or 해당 댓글이 비공개 댓글이 아닙니다."
            }""";

    public static final String NO_DELETE_PERMISSION = """
            {
                "code": "AUTHORITY-006",
                "message": "삭제 권한이 없습니다. or 수정 권한이 없습니다. or 열람 권한이 없습니다."
            }""";

    public static final String NO_REPLY = """
            {
                "code": "DEFAULT-004",
                "message": "해당 대댓글이 존재하지 않습니다."
            }""";

}
