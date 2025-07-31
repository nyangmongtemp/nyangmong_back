package com.playdata.mapservice.common.enumeration;

public class HospitalSwaggerEx {

    public static final String HOSPITAL_LIST = """
            {
                "statusCode": 200,
                "statusMessage": "해당 지역의 모든 동물병원 찾음",
                "result": [
                    {
                        "hospitalId": 75,
                        "approvalDate": "2025-04-08",
                        "hospitalName": "서정동물병원",
                        "fullAddress": "서울특별시 노원구 상계동 1025-20 오아시스빌딩"
                    },
                    {
                        "hospitalId": 314,
                        "approvalDate": "2024-05-03",
                        "hospitalName": "내곁에 동물병원",
                        "fullAddress": "서울특별시 노원구 상계동 95-3 노원 롯데캐슬 시그니처"
                    }
                ]
            }
            """;


    public static final String ADDRESS_REGION = """
            {
                "statusCode": 200,
                "statusMessage": "지역의 세부 지역 정보 리턴",
                "result": [
                    "동대문구",
                    "강남구",
                    "중랑구",
                    "은평구",
                    "성동구",
                    "성북구",
                    "종로구",
                    "영등포구",
                    "강동구",
                    "마포구",
                    "광진구",
                    "양천구",
                    "구로구",
                    "중구",
                    "용산구",
                    "노원구",
                    "관악구",
                    "서대문구",
                    "서초구",
                    "동작구",
                    "송파구",
                    "강서구",
                    "강북구",
                    "도봉구",
                    "금천구"
                ]
            }
            """;

    public static final String HOSPITAL_DETAIL = """
            {
                "statusCode": 200,
                "statusMessage": "해당 병원의 상세 정보 찾음",
                "result": {
                    "id": 1,
                    "serviceName": "동물병원",
                    "serviceId": "02_03_01_P",
                    "approvalDate": "2025-06-30",
                    "phoneNumber": "",
                    "siteArea": null,
                    "postalCode": "",
                    "fullAddress": "전라남도 무안군 무안읍 성남리 157-1 1층",
                    "roadAddress": "전라남도 무안군 무안읍 무안로 449, 1층",
                    "roadPostalCode": "58529",
                    "businessName": "왕실동물병원",
                    "lastModified": "2025-06-30T13:22:00",
                    "mapx": 126.47382236586581,
                    "mapy": 34.982113874138676
                }
            }
            """;
}
