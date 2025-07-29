package com.playdata.mapservice.map.entity.CultureDetail;

import com.playdata.mapservice.map.dto.CultureDetail.PetStyle.res.PetStyleDetailResDto;
import com.playdata.mapservice.map.dto.CultureDetail.PetStyle.res.PetStyleListResDto;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Entity
@Table(name = "pet_art")
public class Art {

    // 데이터에 pk 값이 없어서 임의로 추가함.
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String facilityName;  // 시설명
    private String categoryOne;  // 카테고리 1명
    private String categoryTwo;  // 카테고리 2명
    // 동물병원, 동물약국, 문예회관, 미술관, 미용, 박물관, 반려동물용품, 식당
    // 여행지, 위탁관리, 카페, 호텔, 펜션
    private String categoryThree;  
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
    private String petRestrict;  // 반려동물 제한사항내용
    private String inPlace;  // 반려동물의 내부장소동반가능여부
    private String outPlace;  // 반려동물의 외부장소동반가능여부
    private String infoDesc;  // 시설정보설명
    private String extraFee;  // 반려동물동반추가요금값
    private LocalDate lastUpdate;  // 데이터의 최종수정일자


    // 목록 조회 시 사용되는 dto 변환 메소드
    public PetStyleListResDto toPetStyleListResDto() {
        return PetStyleListResDto.builder()
                .id(this.id)
                .facilityName(this.facilityName)
                .fullAddress(this.fullAddress)
                .roadAddress(this.roadAddress)
                .build();
    }

    // 상세 조회 시 사용되는 dto 변환 메소드
    public PetStyleDetailResDto toPetStyleDetailResDto() {
        return PetStyleDetailResDto.builder()
                .id(this.id)
                .facilityName(this.facilityName)
                .fullAddress(this.fullAddress)
                .roadAddress(this.roadAddress)
                .mapx(this.mapy)
                .mapy(this.mapx)
                .zipNo(this.zipNo)
                .telNum(this.telNum)
                .url(this.url)
                .restInfo(this.restInfo)
                .operTime(this.operTime)
                .parking(this.parking)
                .price(this.price)
                .petWith(this.petWith)
                .petInfo(this.petInfo)
                .petSize(this.petSize)
                .petRestrict(this.petRestrict)
                .inPlace(this.inPlace)
                .outPlace(this.outPlace)
                .infoDesc(this.infoDesc)
                .extraFee(this.extraFee)
                .lastUpdate(this.lastUpdate)
                .build();
    }
}
