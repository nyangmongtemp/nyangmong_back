package com.playdata.schedulerservice.api.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Entity
@Table(name = "pet_culture")
public class PetCulture {

    // 데이터에 pk 값이 없어서 임의로 추가함.
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String facilityName;  // 시설명
    private String categoryOne;  // 카테고리 1명
    private String categoryTwo;  // 카테고리 2명
    private String categoryThree;  // 카테고리 3명
    private String sido;    // 시도명
    private String sigungu;  // 시군구명
    private String legalDong;  // 법정동명
    private String liName;  // 리명
    private String lnbrNo;  // 번지번호
    private String roadName;  // 도로명
    private String buildingNo;  // 건물번호
    private Double mapx;   // 위치위도
    private Double mapy;  // 위치경도
    private String zipNo;  // 우편번호
    private String roadAddress;  // 도로명주소명
    private String fullAddress;  // 지번주소
    private String telNum;  // 전화번호

    @Column(length = 1024)   // 들어오는 url길이가 너무 긴 경우가 발생함.
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

    
    // 디테일 테이블로의 삽입을 위한 Map 변환 메소드
    public Map<String, Object> convertToParamMap() {
        Map<String, Object> map = new HashMap<>();
        map.put("facilityName", this.facilityName);
        map.put("categoryOne", this.categoryOne);
        map.put("categoryTwo", this.categoryTwo);
        map.put("categoryThree", this.categoryThree);
        map.put("sido", this.sido);
        map.put("sigungu", this.sigungu);
        map.put("legalDong", this.legalDong);
        map.put("liName", this.liName);
        map.put("lnbrNo", this.lnbrNo);
        map.put("roadName", this.roadName);
        map.put("buildingNo", this.buildingNo);
        map.put("mapx", this.mapx);
        map.put("mapy", this.mapy);
        map.put("zipNo", this.zipNo);
        map.put("roadAddress", this.roadAddress);
        map.put("fullAddress", this.fullAddress);
        map.put("telNum", this.telNum);
        map.put("url", this.url);
        map.put("restInfo", this.restInfo);
        map.put("operTime", this.operTime);
        map.put("parking", this.parking);
        map.put("price", this.price);
        map.put("petWith", this.petWith);
        map.put("petInfo", this.petInfo);
        map.put("petSize", this.petSize);
        map.put("petRestrict", this.petRestrict);
        map.put("inPlace", this.inPlace);
        map.put("outPlace", this.outPlace);
        map.put("infoDesc", this.infoDesc);
        map.put("extraFee", this.extraFee);
        map.put("lastUpdate", this.lastUpdate);
        return map;
    }


}
