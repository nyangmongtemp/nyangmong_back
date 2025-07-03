package com.playdata.animalboardservice.dto;

import com.playdata.animalboardservice.entity.StrayAnimal;
import com.playdata.animalboardservice.entity.StrayAnimal.SexCode;
import lombok.Builder;
import lombok.Getter;

@Getter
public class StrayAnimalResDto {

    private String desertionNo; // 유기동물 고유번호, PK 역할
    private String upKindNm; // 축종 이름 (예: 개, 고양이)
    private String kindNm; // 품종 이름
    private String age; // 나이 정보
    private String popfile1; // 대표 이미지 URL
    private SexCode sexCd; // 성별 코드 (M: 수컷, F: 암컷, Q: 미상)
    private String careTel; // 보호소 전화번호
    private String careAddr; // 보호소 주소

    @Builder
    public StrayAnimalResDto(StrayAnimal strayAnimal) {
        this.desertionNo = strayAnimal.getDesertionNo();
        this.upKindNm = strayAnimal.getUpKindNm();
        this.kindNm = strayAnimal.getKindNm();
        this.age = strayAnimal.getAge();
        this.popfile1 = strayAnimal.getPopfile1();
        this.sexCd = strayAnimal.getSexCd();
        this.careTel = strayAnimal.getCareTel();
        this.careAddr = strayAnimal.getCareAddr();
    }

}
