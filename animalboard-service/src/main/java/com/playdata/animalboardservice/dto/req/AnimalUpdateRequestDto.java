package com.playdata.animalboardservice.dto.req;

import com.playdata.animalboardservice.entity.Animal;
import com.playdata.animalboardservice.entity.NeuterYn;
import com.playdata.animalboardservice.entity.SexCode;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

/**
 * 분양동물 수정 Request DTO
 */
@Getter
public class AnimalUpdateRequestDto {

    @Schema(hidden = true)
    private String thumbnailImage; // 썸넹일 이미지
    @NotBlank
    @Schema(description = "제목", example = "제목 입력")
    private String title; // 제목
    @NotBlank
    @Schema(description = "내용", example = "내용 입력")
    private String content; // 내용
    @NotBlank
    @Schema(description = "품종", example = "강아지")
    private String petCategory; // 강아지, 고양이, 기타
    @NotBlank
    @Schema(description = "세부 분류", example = "믹스견")
    private String petKind; // 세부 분류 ( 믹스종 등)
    @NotBlank
    @Schema(description = "age", example = "2살")
    private String age; // 나이
    @Schema(description = "백신접종여부", example = "1처완료")
    private String vaccine; // 백신접종여부
    @NotNull
    @Schema(description = "성별", example = "M")
    private SexCode sexCode; // 성별
    @NotNull
    @Schema(description = "중성화여부", example = "N")
    private NeuterYn neuterYn; // 중성화여부
    @NotBlank
    @Schema(description = "주소", example = "서초구")
    private String address; // 주소
    @NotNull
    @Schema(description = "책임비", example = "20000")
    private Integer fee; // 책임비

}
