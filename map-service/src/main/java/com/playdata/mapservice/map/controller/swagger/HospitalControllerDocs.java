package com.playdata.mapservice.map.controller.swagger;

import com.playdata.mapservice.common.dto.CommonResDto;
import com.playdata.mapservice.common.enumeration.HospitalSwaggerEx;
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

public interface HospitalControllerDocs {

    @Operation(summary = "세부 지역의 병원 조회",
            description = """
               해당 시/군/구의 동물병원 목록을 조회합니다.
           """)
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200", description = "조회 성공", content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = CommonException.class),
                    examples = @ExampleObject(value = HospitalSwaggerEx.HOSPITAL_LIST)
            )),
            @ApiResponse(
                    responseCode = "403", description = "올바르지 않은 입력값"
            )
    })
    ResponseEntity<CommonResDto> hospitalList(
            @Parameter(
                    name = "addressCode",
                    description = "대한민구의 시/도 값 중 하나",
                    required = true
            )
            @PathVariable(name = "addressCode") String addressCode,
            @Parameter(
                    name = "detailAddress",
                    description = "선택된 시/도의 시/군/구 중 하나",
                    required = true
            )
            @PathVariable(name = "detailAddress") String detailAddress);


    @Operation(summary = "시/도의 시/군/구 목록 조회",
            description = """
               해당 시/도의 시/군/구를 조회합니다.
           """)
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200", description = "조회 성공", content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = CommonException.class),
                    examples = @ExampleObject(value = HospitalSwaggerEx.ADDRESS_REGION)
            )),
            @ApiResponse(
                    responseCode = "403", description = "올바르지 않은 입력값"
            )
    })
    ResponseEntity<CommonResDto> regionCategory(
            @Parameter(
                    name = "addressCode",
                    description = "대한민구의 시/도 값 중 하나",
                    required = true
            )
            @PathVariable String addressCode);


    @Operation(summary = "동물병원 상세 조회",
            description = """
               해당 동물병원의 상세 정보를 조회합니다..
           """)
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200", description = "조회 성공", content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = CommonException.class),
                    examples = @ExampleObject(value = HospitalSwaggerEx.HOSPITAL_DETAIL)
            )),
            @ApiResponse(
                    responseCode = "403", description = "올바르지 않은 입력값"
            )
    })
    ResponseEntity<CommonResDto> hospitalDetail(@PathVariable(name = "id") Long hospitalId);

}
