package com.playdata.mapservice.common.enumeration;

public class PetCultureSwaggerEx {

    public static final String CULTURE_REGION = """
            {
                "statusCode": 200,
                "statusMessage": "해당 지역의 시군구 정보 찾음",
                "result": [
                    "광산구",
                    "동구",
                    "서구",
                    "남구",
                    "북구"
                ]
            }
            """;

    public static final String CULTURE_LIST = """
            {
                "statusCode": 200,
                "statusMessage": "해당 지역의 미용실 정보 찾음",
                "result": [
                    {
                        "id": 77,
                        "roadAddress": "광주광역시 광산구 수완로106번길 50-14",
                        "fullAddress": "광주광역시 광산구 수완동 1293",
                        "facilityName": "꾸르커피"
                    },
                    {
                        "id": 91,
                        "roadAddress": "광주광역시 광산구 장신로64번길 9-14",
                        "fullAddress": "광주광역시 광산구 장덕동 1670",
                        "facilityName": "냐옹냐옹고양이까페"
                    },
                    {
                        "id": 110,
                        "roadAddress": "광주광역시 광산구 대산로 22",
                        "fullAddress": "광주광역시 광산구 대산동 300-1",
                        "facilityName": "눈보뛰"
                    },
                    {
                        "id": 114,
                        "roadAddress": "광주광역시 광산구 월계로 173",
                        "fullAddress": "광주광역시 광산구 월계동 890-5",
                        "facilityName": "다독트레이닝"
                    },
                    {
                        "id": 164,
                        "roadAddress": "광주광역시 광산구 수완로11번안길 15",
                        "fullAddress": "광주광역시 광산구 수완동 1521",
                        "facilityName": "독크루"
                    },
                    {
                        "id": 167,
                        "roadAddress": "광주광역시 광산구 첨단중앙로182번길 28",
                        "fullAddress": "광주광역시 광산구 쌍암동 664-5",
                        "facilityName": "동몽식탁"
                    },
                    {
                        "id": 336,
                        "roadAddress": "광주광역시 광산구 평동로 650",
                        "fullAddress": "광주광역시 광산구 용곡동 399-3",
                        "facilityName": "빈티지399"
                    },
                    {
                        "id": 428,
                        "roadAddress": "광주광역시 광산구 어등대로648번안길 47",
                        "fullAddress": "광주광역시 광산구 선암동 123",
                        "facilityName": "애니멀고파크 광주점"
                    },
                    {
                        "id": 511,
                        "roadAddress": "광주광역시 광산구 임방울대로800번길 11-6",
                        "fullAddress": "광주광역시 광산구 월계동 872-3",
                        "facilityName": "인디퍼피"
                    }
                ]
            }
            """;


    public static final String CULTURE_DETAIL = """
            {
                "statusCode": 200,
                "statusMessage": "해당 미용실의 상세 정보 찾음",
                "result": {
                    "id": 77,
                    "facilityName": "꾸르커피",
                    "mapx": 126.831323,
                    "mapy": 35.19260433,
                    "zipNo": "62248",
                    "roadAddress": "광주광역시 광산구 수완로106번길 50-14",
                    "fullAddress": "광주광역시 광산구 수완동 1293",
                    "telNum": "0629542677",
                    "url": "http://instagram.com/kkureu_coffee",
                    "restInfo": "매주 목요일",
                    "operTime": "금~수 13:00~22:00",
                    "parking": "N",
                    "price": "변동",
                    "petWith": "Y",
                    "petInfo": "해당없음",
                    "petSize": "모두 가능",
                    "petRestrict": "제한사항 없음",
                    "inPlace": "Y",
                    "outPlace": "N",
                    "infoDesc": "애견카페",
                    "extraFee": "없음",
                    "lastUpdate": null
                }
            }
            """;

}
