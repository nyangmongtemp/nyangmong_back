package com.playdata.animalboardservice.dto.req;

import com.playdata.animalboardservice.entity.Animal;
import com.playdata.animalboardservice.entity.NeuterYn;
import com.playdata.animalboardservice.entity.SexCode;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

/**
 * 분양동물 수정 Request DTO
 */
@Getter
public class AnimalUpdateRequestDto {

    private String thumbnailImage; // 썸넹일 이미지
    @NotBlank
    private String title; // 제목
    @NotBlank
    private String content; // 내용
    @NotBlank
    private String petCategory; // 강아지, 고양이, 기타
    @NotBlank
    private String petKind; // 세부 분류 ( 믹스종 등)
    @NotBlank
    private String age; // 나이
    private String vaccine; // 백신접종여부
    @NotNull
    private SexCode sexCode; // 성별
    @NotNull
    private NeuterYn neuterYn; // 중성화여부
    @NotBlank
    private String address; // 주소
    private String fee; // 책임비

}
