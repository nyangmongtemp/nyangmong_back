package com.playdata.animalboardservice.dto.res;

import com.playdata.animalboardservice.entity.NeuterYn;
import com.playdata.animalboardservice.entity.SexCode;
import com.playdata.animalboardservice.entity.StrayAnimal;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class StrayAnimalListResDto {

    private String desertionNo; // 유기동물 고유번호, PK 역할
    private String upKindNm; // 축종 이름 (예: 개, 고양이)
    private String kindNm; // 품종 이름
    private String age; // 나이 정보
    private String popfile1; // 대표 이미지 URL
    private SexCode sexCd; // 성별 코드 (M: 수컷, F: 암컷, Q: 미상)
    private String careTel; // 보호소 전화번호
    private String careAddr; // 보호소 주소
    private String happenDt; // 유기발생 날짜
    private NeuterYn neuterYn; // 중성화 여부 (Y: 예, N: 아니오, U: 미상)

}
