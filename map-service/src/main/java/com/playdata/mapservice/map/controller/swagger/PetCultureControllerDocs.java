package com.playdata.mapservice.map.controller.swagger;

import com.playdata.mapservice.common.dto.CommonResDto;
import com.playdata.mapservice.common.enumeration.HospitalSwaggerEx;
import com.playdata.mapservice.common.enumeration.PetCultureSwaggerEx;
import com.playdata.mapservice.common.exception.CommonException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;

public interface PetCultureControllerDocs {

    @Operation(summary = "시/도의 시/군/구 혹은 법정동명 조회",
            description = """
               해당 시/도의 시/군/구 혹은 법정동명을 조회합니다.
           """)
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200", description = "조회 성공", content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = CommonException.class),
                    examples = @ExampleObject(value = PetCultureSwaggerEx.CULTURE_REGION)
            )),
            @ApiResponse(
                    responseCode = "403", description = "올바르지 않은 입력값"
            )
    })
    ResponseEntity<CommonResDto> getRegionDetailList(
            @Parameter(
                    name = "addressCode",
                    description = "대한민구의 시/도 값 중 하나",
                    required = true
            )
            @PathVariable String addressCode,
            @Parameter(
                    name = "category",
                    description = "문화시설 카테고리",
                    required = true
            )
            @PathVariable String category);


    @Operation(summary = "시/군/구의 문화시설 목록 조회",
            description = """
               해당 시/군/구의 문화시설 목록을 조회합니다.
           """)
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200", description = "조회 성공", content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = CommonException.class),
                    examples = @ExampleObject(value = PetCultureSwaggerEx.CULTURE_LIST)
            )),
            @ApiResponse(
                    responseCode = "403", description = "올바르지 않은 입력값"
            )
    })
    ResponseEntity<CommonResDto> getList(
            @Parameter(
                    name = "addressCode",
                    description = "대한민구의 시/도 값 중 하나",
                    required = true
            )
            @PathVariable String addressCode,
            @Parameter(
                    name = "sigungu",
                    description = "addressCode에 해당하는 시/군/구 중 하나",
                    required = true
            )
            @PathVariable String sigungu,
            @Parameter(
                    name = "category",
                    description = "문화시설 카테고리",
                    required = true
            )
            @PathVariable String category);


    @Operation(summary = "특정 문화시설의 상세정보를 조회",
            description = """
               특정 문화시설의 상세정보를 조회합니다.
           """)
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200", description = "조회 성공", content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = CommonException.class),
                    examples = @ExampleObject(value = PetCultureSwaggerEx.CULTURE_DETAIL)
            )),
            @ApiResponse(
                    responseCode = "403", description = "올바르지 않은 입력값"
            )
    })
    ResponseEntity<CommonResDto> getDetail(@PathVariable Long id ,
                                           @Parameter(
                                                   name = "category",
                                                   description = "문화시설 카테고리",
                                                   required = true
                                           )
                                           @PathVariable String category);

}
