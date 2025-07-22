package com.playdata.mapservice.map.dto.CultureDetail.PetStyle.res;

import jakarta.persistence.Column;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;

@Getter
@Builder
public class PetStyleDetailResDto {

    private Long id;
    private String facilityName;  // 시설명
    private Double mapx;   // 위치위도
    private Double mapy;  // 위치경도
    private String zipNo;  // 우편번호
    private String roadAddress;  // 도로명주소명
    private String fullAddress;  // 지번주소
    private String telNum;  // 전화번호
    private String url;  // 홈페이지 URL
    private String restInfo;  // 휴무일안내내용
    private String operTime;  // 운영시간
    private String parking;  // 주차가능여부
    private String price;  // 이용가격내용
    private String petWith;  // 반려동물가능여부
    private String petInfo;  // 반려동물정보내용
    private String petSize;  // 입장가능반려동물크기값
    private String petRestrict;  // 반려동물제한사항내용
    private String inPlace;  // 내부장소동반가능여부
    private String outPlace;  // 외부장소동반가능여부
    private String infoDesc;  // 시설정보설명
    private String extraFee;  // 반려동물동반추가요금값
    private LocalDate lastUpdate;  // 최종수정일자

}
