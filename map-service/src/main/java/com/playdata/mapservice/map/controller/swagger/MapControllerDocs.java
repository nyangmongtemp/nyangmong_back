package com.playdata.mapservice.map.controller.swagger;

import com.playdata.mapservice.common.dto.CommonResDto;
import com.playdata.mapservice.common.enumeration.HospitalSwaggerEx;
import com.playdata.mapservice.common.enumeration.MapSwaggerEx;
import com.playdata.mapservice.common.exception.CommonException;
import com.playdata.mapservice.map.dto.map.req.MapSearchReqDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

@Tag(name = "문화 시설", description = "문화시설 정보를 조회하는 API")
public interface MapControllerDocs {

    @Operation(summary = "해당 지역의 문화시설 조회",
            description = """
               해당 시/도의 문화시설 목록을 조회합니다.
           """)
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200", description = "조회 성공", content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = CommonException.class),
                    examples = @ExampleObject(value = MapSwaggerEx.MAP_LIST)
            )),
            @ApiResponse(
                    responseCode = "403", description = "올바르지 않은 입력값"
            )
    })
    ResponseEntity<CommonResDto> findMap(@RequestBody MapSearchReqDto reqDto);

    @Operation(summary = "특정 문화시설 상세조회",
            description = """
               특정 문화시설의 상세정보를 조회합니다.
           """)
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200", description = "조회 성공", content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = CommonException.class),
                    examples = @ExampleObject(value = MapSwaggerEx.MAP_DETAIL)
            )),
            @ApiResponse(
                    responseCode = "400", description = "올바르지 않은 입력값", content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = CommonException.class),
                    examples = @ExampleObject(value = MapSwaggerEx.BAD_REQUEST)
            ))
    })
    ResponseEntity<CommonResDto> findMapDetail(@PathVariable(name = "id") Long mapId);

}
