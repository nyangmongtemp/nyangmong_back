package com.playdata.animalboardservice.dto.req;

import com.playdata.animalboardservice.entity.Animal;
import com.playdata.animalboardservice.entity.NeuterYn;
import com.playdata.animalboardservice.entity.SexCode;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

/**
 * 분양동물 등록 Request DTO
 */
@Getter
@Schema(description = "분양게시물 등록 요청 DTO")
public class AnimalInsertRequestDto {

    @Schema(hidden = true)
    private Long userId; // 사용자 id 값
    @Schema(hidden = true)
    private String thumbnailImage; // 썸넹일 이미지
    @Schema(hidden = true)
    private String nickname; // 닉네임
    @NotBlank
    @Schema(description = "분양 제목", example = "우리아이 분양합니다.")
    private String title; // 제목
    @NotBlank
    @Schema(description = "분양 내용", example = "우리아이 태어난지 30일 되었습니다.")
    private String content; // 내용
    @NotBlank
    @Schema(description = "품종", example = "강아지")
    private String petCategory; // 강아지, 고양이, 기타
    @NotBlank
    @Schema(description = "세부 분류", example = "비숑")
    private String petKind; // 세부 분류 ( 믹스종 등)
    @NotBlank
    @Schema(description = "나이", example = "30일")
    private String age; // 나이
    @Schema(description = "백신 접종 여부", example = "안맞았어요")
    private String vaccine; // 백신접종여부
    @NotNull
    @Schema(description = "성별", example = "M")
    private SexCode sexCode; // 성별
    @NotNull
    @Schema(description = "중성화 여부", example = "N")
    private NeuterYn neuterYn; // 중성화여부
    @NotBlank
    @Schema(description = "주소", example = "강남구")
    private String address; // 주소
    @NotNull
    @Schema(description = "책임비", example = "0")
    private Integer fee; // 책임비

    public Animal toEntity(Long userId, String newThumbnailImage, String nickname) {
        return Animal.builder()
                .userId(userId)
                .thumbnailImage(newThumbnailImage)
                .nickname(nickname)
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
