package com.playdata.animalboardservice.dto.req;

import com.playdata.animalboardservice.entity.Animal;
import com.playdata.animalboardservice.entity.NeuterYn;
import com.playdata.animalboardservice.entity.SexCode;
import lombok.Getter;

/**
 * 분양동물 등록 Request DTO
 */
@Getter
public class AnimalInsertRequestDto {

    private Long userId; // 사용자 id 값
    private String thumbnailImage; // 썸넹일 이미지
    private String title; // 제목
    private String content; // 내용
    private String petCategory; // 강아지, 고양이, 기타
    private String petKind; // 세부 분류 ( 믹스종 등)
    private String age; // 나이
    private String vaccine; // 백신접종여부
    private SexCode sexCode; // 성별
    private NeuterYn neuterYn; // 중성화여부
    private String address; // 주소
    private String fee; // 책임비

    public Animal toEntity(Long userId, String newThumbnailImage) {
        return Animal.builder()
                .userId(userId)
                .thumbnailImage(newThumbnailImage)
                .title(title)
                .content(content)
                .petCategory(petCategory)
                .petKind(petKind)
                .age(age)
                .vaccine(vaccine)
                .sexCode(sexCode)
                .neuterYn(neuterYn)
                .address(address)
                .fee(fee)
                .build();
    }
}
