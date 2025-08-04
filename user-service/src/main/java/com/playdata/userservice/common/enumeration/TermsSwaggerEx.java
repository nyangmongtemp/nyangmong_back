package com.playdata.userservice.common.enumeration;

public class TermsSwaggerEx {

    public static final String TERMS_LIST = """
            {
                "statusCode": 200,
                "statusMessage": "목록 조회",
                "result": {
                    "content": [
                        {
                            "termsId": 1,
                            "title": "이용약관",
                            "content": "<h1>이용약관!</h1><p>&nbsp;</p><p>여러분들은 모두 형식에 맞게 글들을 작성해야 합니다</p><p>&nbsp;</p><p>&nbsp;</p>",
                            "createAt": "2025-07-28T11:34:36.513857",
                            "updateAt": "2025-07-28T12:11:58.098756"
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
                                "ascending": false,
                                "descending": true
                            }
                        ],
                        "offset": 0,
                        "paged": true,
                        "unpaged": false
                    },
                    "last": true,
                    "totalElements": 1,
                    "totalPages": 1,
                    "size": 5,
                    "number": 0,
                    "sort": [
                        {
                            "direction": "DESC",
                            "property": "createAt",
                            "ignoreCase": false,
                            "nullHandling": "NATIVE",
                            "ascending": false,
                            "descending": true
                        }
                    ],
                    "first": true,
                    "numberOfElements": 1,
                    "empty": false
                }
            }
            """;


    public static final String TERMS_DETAIL = """
            {
                "statusCode": 200,
                "statusMessage": "상세 조회",
                "result": {
                    "title": "개인정보처리방침.v2",
                    "content": "<p>개인정보처리방침 버전 투입니다.개인정보처리방침 버전 투입니다.</p>",
                    "createAt": "2025-07-28T12:15:03.425839",
                    "updateAt": "2025-07-28T12:15:03.425839"
                }
            }
            """;


    public static final String LAST_POST = """
            {
                "statusCode": 200,
                "statusMessage": "약관 마지막 게시글 조회",
                "result": {
                    "id": 7,
                    "title": "개인정보처리방침",
                    "content": "<p>개인정보처리방침</p>",
                    "createAt": "2025-07-28T12:20:14.872034",
                    "updateAt": "2025-07-28T12:20:14.872034"
                }
            }
            """;




    public static final String BAD_REQUEST = """
            {
                "code": "DEFAULT-001",
                "message": "잘못된 요청값입니다."
            }
            """;




}
