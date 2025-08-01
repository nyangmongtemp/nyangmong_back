package com.playdata.festivalservice.controller;

import com.playdata.festivalservice.common.enumeration.SwaggerExampleConstants;
import com.playdata.festivalservice.common.exception.CommonException;
import com.playdata.festivalservice.dto.FestivalResponseDto;
import com.playdata.festivalservice.dto.FestivalSearchDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;

@Tag(name = "행사 게시판(Festival)", description = "행사 게시판 관리하는 API")
public interface FestivalControllerDocs {

    @Operation(summary = "행사 목록 조회 (검색, 페이징)", description = "행사 게시물의 목록 정보를 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200", description = "조회 성공", content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = FestivalSearchDto.class),
                    examples = @ExampleObject(value = SwaggerExampleConstants.TERMS_LIST_RESPONSE)
            )),
            @ApiResponse(
                    responseCode = "400", description = "잘못된 요청", content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = CommonException.class),
                    examples = @ExampleObject(value = SwaggerExampleConstants.COMMON_BAD_REQUEST)
            ))
    })
    ResponseEntity<Page<FestivalResponseDto>> getFestivalList(@ParameterObject FestivalSearchDto festivalSearchDto,
            @ParameterObject @PageableDefault(size = 10, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable);

}
