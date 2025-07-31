package com.playdata.adminservice.common.enumeration;

public class BannerSwaggerExample {

    public static final String BANNER_CREATE_RESPONSE = """
            {
                "statusCode": 201,
                    "statusMessage": "배너 생성 예시",
                    "result": {
                        "bannerId": 1,
                        "title": "배너 생성 예시",
                        "thumbnailImage": "your_image_url.jpg",
                        "adminId": 1,
                        "order": null,
                        "basic": false
                    }
            }
            """;


    public static final String BANNER_UPDATE_RESPONSE = """
            {
                "statusCode": 200,
                "statusMessage": "배너 내용 수정됨",
                "result": {
                    "bannerId": 1,
                    "title": "배너 수정 예시",
                    "thumbnailImage": "your_image_url.jpg",
                    "adminId": 1,
                    "order": null,
                    "basic": false
                }
            }
            """;

    public static final String BANNER_DELETE_RESPONSE = """
            {
                "statusCode": 200,
                "statusMessage": "배너 삭제됨",
                "result": true
            }
            """;

    public static final String BANNER_ORDER_UPDATE = """
            {
                "statusCode": 200,
                "statusMessage": "배너의 순서 수정됨",
                "result": true
            }
            """;

    public static final String BANNER_ORDER_ENROLL = """
            {
                "statusCode": 200,
                "statusMessage": "노출할 배너로 등록됨",
                "result": true
            }
            """;
    
    public static final String BANNER_ORDER_CANCEL = """
            {
                "statusCode": 200,
                "statusMessage": "배너 노출 해제됨",
                "result": true
            }
            """;
    
    public static final String FILE_INVALID_ERROR = """
            {
                "code": "FILE-008",
                "message": "업로드한 파일은 이미지가 아닙니다. (contentType 검사 실패)."
            }
            """;

    public static final String BANNER_BAD_REQUEST = """
            {
                "code": "DEFAULT-001",
                "message": "해당 배너는 존재하지 않음."
            }""";

    public static final String BANNER_DEFAULT = """
            {
                "code": "DEFAULT-001",
                "message": "기본 배너입니다."
            }""";

    public static final String ORDER_OVER = """
            {
                "code": "DEFAULT-001",
                "message": "노출 최대 개수 초과"
            }""";

    public static final String ORDER_BAD_REQUEST = """
            {
                "code": "DEFAULT-001",
                "message": "순서 수정 요청 배너 개수 이상"
            }""";

    public static final String ORDER_NON = """
            {
                "code": "DEFAULT-001",
                "message": "순서가 없는 배너입니다."
            }""";

    public static final String BAD_REQUEST_ETC = """
            {
                "code": "DEFAULT-001",
                "message": "배너를 노출시킬 수 없습니다. or 배너가 이미 노출되어있지 않거나, 기본 배너입니다."
            }""";


    public static final String BANNER_EXPOSED = """
            {
                "statusCode": 200,
                "statusMessage": "노출시킬 배너 목록 조회",
                "result": [
                    {
                        "bannerId": 3,
                        "order": 1,
                        "title": "생성배너 1",
                        "adminId": 1,
                        "basic": false,
                        "image": "example.jpg"
                    },
                    {
                        "bannerId": 1,
                        "order": 2,
                        "title": "기본 배너 1",
                        "adminId": 1,
                        "basic": true,
                        "image": "example.jpg"
                        },
                    {
                        "bannerId": 2,
                        "order": 3,
                        "title": "기본 배너 2",
                        "adminId": 1,
                        "basic": true,
                        "image": "example.jpg"
                    }
                ]
            }
            """;

    public static final String BANNER_UNEXPOSED = """
            {
                "statusCode": 200,
                "statusMessage": "배너들 페이징 조회해옴",
                "result": [
                    {
                        "bannerId": 6,
                        "order": null,
                        "title": "생성배너 1",
                        "adminId": 1,
                        "basic": false,
                        "image": "example.jpg"
                    },
                    {
                        "bannerId": 5,
                        "order": null,
                        "title": "생성 배너 2",
                        "adminId": 1,
                        "basic": false,
                        "image": "example.jpg"
                        },
                    {
                        "bannerId": 4,
                        "order": null,
                        "title": "생성 배너 3",
                        "adminId": 1,
                        "basic": false,
                        "image": "example.jpg"
                    }
                ]
            }
            """;

    public static final String ORDER_UPDATE = """
            {
                "statusCode": 200,
                "statusMessage": "노출 배너 개수 갱신됨",
                "result": 6
            }
            """;

    public static final String ORDER_GET = """
            {
                "statusCode": 200,
                "result": 3
            }
            """;
}
