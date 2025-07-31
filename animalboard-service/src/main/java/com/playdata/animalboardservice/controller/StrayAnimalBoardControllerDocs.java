package com.playdata.animalboardservice.controller;

import com.playdata.animalboardservice.common.dto.CommonResDto;
import com.playdata.animalboardservice.common.enumeration.SwaggerExampleConstants;
import com.playdata.animalboardservice.common.exception.CommonException;
import com.playdata.animalboardservice.dto.StraySearchDto;
import com.playdata.animalboardservice.dto.res.StrayAnimalDetailResDto;
import com.playdata.animalboardservice.dto.res.StrayAnimalListResDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;

@Tag(name = "유기동물 게시판(AnimalBoard)", description = "유기동물 목록 / 상세 / 메인 관리하는 API")
public interface StrayAnimalBoardControllerDocs {

    @Operation(summary = "유기동물 목록 조회 (검색, 페이징)", description = "유기동물 목록 정보를 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200", description = "조회 성공", content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = StrayAnimalListResDto.class),
                    examples = @ExampleObject(value = SwaggerExampleConstants.STRAY_ANIMAL_LIST_RESPONSE)
            ))
    })
    ResponseEntity<CommonResDto> findStrayAnimalList(@ParameterObject StraySearchDto straySearchDto, @ParameterObject Pageable pageable);

    @Operation(summary = "약관/방침/QNA 상세 조회", description = "약관/방침/QNA 게시물의 상세 정보를 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200", description = "조회 성공", content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = StrayAnimalDetailResDto.class),
                    examples = @ExampleObject(value = SwaggerExampleConstants.STRAY_ANIMAL_DETAIL_RESPONSE)
            )),
            @ApiResponse(
                    responseCode = "404", description = "없는 데이터", content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = CommonException.class),
                    examples = @ExampleObject(value = SwaggerExampleConstants.COMMON_DETAIL_EXCEPTION)
            ))
    })
    ResponseEntity<CommonResDto> getAnimalBoard(@PathVariable String desertionNo);

    @Operation(summary = "유기동물 메인 목록 조회", description = "유기동물 메인 목록 정보를 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200", description = "조회 성공", content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = StrayAnimalListResDto.class),
                    examples = @ExampleObject(value = SwaggerExampleConstants.STRAY_ANIMAL_MAIN_LIST_RESPONSE)
            ))
    })
    ResponseEntity<CommonResDto> findStrayAnimalMainList();

}
