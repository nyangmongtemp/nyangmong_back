package com.playdata.animalboardservice.dto;

import com.playdata.animalboardservice.entity.NeuterYn;
import com.playdata.animalboardservice.entity.SexCode;
import com.playdata.animalboardservice.entity.StrayAnimal;
import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import lombok.Builder;
import lombok.Getter;

@Getter
public class StrayAnimalResDto {

    private String desertionNo; // 유기동물 고유번호, PK 역할
    private String rfidCd; // 내장 칩 RFID 코드 (없을 수 있음)
    private String happenDt; // 유기 발생 날짜 (YYYYMMDD 형식)
    private String happenPlace; // 유기 발생 장소
    private String upKindNm; // 축종 이름 (예: 개, 고양이)
    private String kindNm; // 품종 이름 (예: 믹스견)
    private String colorCd; // 털색 정보
    private String age; // 나이 정보
    private String weight; // 체중 (예: "3Kg")
    private String popfile1; // 대표 이미지 URL
    private String popfile2; // 보조 이미지 URL
    private SexCode sexCd; // 성별 코드 (M: 수컷, F: 암컷, Q: 미상
    private NeuterYn neuterYn; // 중성화 여부 (Y: 예, N: 아니오, U: 미상)
    private String specialMark; // 특이사항
    private String careNm; // 보호소 이름
    private String careTel; // 보호소 전화번호
    private String careAddr; // 보호소 주소
    private String careOwnerNm; // 보호 책임자명
    private String orgNm; // 보호소 관할 지자체 이름
    private String etcBigo; // 기타 비고

    @Builder
    public StrayAnimalResDto(StrayAnimal strayAnimal) {
        this.desertionNo = strayAnimal.getDesertionNo();
        this.rfidCd = strayAnimal.getRfidCd();
        this.happenDt = strayAnimal.getHappenDt();
        this.happenPlace = strayAnimal.getHappenPlace();
        this.upKindNm = strayAnimal.getUpKindNm();
        this.kindNm = strayAnimal.getKindNm();
        this.colorCd = strayAnimal.getColorCd();
        this.age = strayAnimal.getAge();
        this.weight = strayAnimal.getWeight();
        this.popfile1 = strayAnimal.getPopfile1();
        this.popfile2 = strayAnimal.getPopfile2();
        this.sexCd = strayAnimal.getSexCd();
        this.neuterYn = strayAnimal.getNeuterYn();
        this.specialMark = strayAnimal.getSpecialMark();
        this.careNm = strayAnimal.getCareNm();
        this.careTel = strayAnimal.getCareTel();
        this.careAddr = strayAnimal.getCareAddr();
        this.careOwnerNm = strayAnimal.getCareOwnerNm();
        this.orgNm = strayAnimal.getOrgNm();
        this.etcBigo = strayAnimal.getEtcBigo();
    }
}
