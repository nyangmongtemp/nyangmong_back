package com.playdata.animalboardservice.controller;

import com.playdata.animalboardservice.common.auth.TokenUserInfo;
import com.playdata.animalboardservice.common.enumeration.SwaggerExampleConstants;
import com.playdata.animalboardservice.common.exception.CommonException;
import com.playdata.animalboardservice.dto.SearchDto;
import com.playdata.animalboardservice.dto.req.AnimalInsertRequestDto;
import com.playdata.animalboardservice.dto.req.AnimalUpdateRequestDto;
import com.playdata.animalboardservice.dto.req.ReservationReqDto;
import com.playdata.animalboardservice.dto.res.AnimalDetailResDto;
import com.playdata.animalboardservice.dto.res.AnimalListResDto;
import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

// Swagger 전용 인터페이스를 하나 선언해서 비즈니스 로직 vs 문서화 로직을 분리
// 컨트롤러는 본연의 역할에만 집중
@Tag(name = "유기동물/분양 게시판(AnimalBoard)", description = "유기동물 / 분양동물 CRUD 관리하는 API")
public interface AnimalBoardControllerDocs {

    @Operation(summary = "분양동물 목록 조회 (검색, 페이징)",
            description = """
                분양 게시물의 목록 정보를 조회합니다.
                
                ## 인증
                - 로그인 하지 않은 사용자도 조회 가능합니다.
            """)
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200", description = "조회 성공", content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = AnimalListResDto.class),
                    examples = @ExampleObject(value = SwaggerExampleConstants.ANIMAL_LIST_RESPONSE)
            ))
    })
    ResponseEntity<Page<AnimalListResDto>> getAnimalList(@Parameter(description = "검색 조건") SearchDto searchDto, @Parameter(description = "페이지 및 정렬 정보") Pageable pageable);

    @Operation(summary = "분양 게시물 상세 조회",
            description = """
                게시물 ID를 기반으로 분양 게시물의 상세 정보를 조회합니다.
                
                ## 인증
                - 로그인 하지 않은 사용자도 조회 가능합니다.
            """)
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200", description = "조회 성공", content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = AnimalDetailResDto.class),
                    examples = @ExampleObject(value = SwaggerExampleConstants.ANIMAL_DETAIL_RESPONSE)
            )),
            @ApiResponse(
                    responseCode = "404", description = "존재하지 않는 게시물 ID", content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = CommonException.class),
                    examples = @ExampleObject(value = SwaggerExampleConstants.ANIMAL_DETAIL_EXCEPTION)
            ))
    })
    ResponseEntity<AnimalDetailResDto> getAnimal(@PathVariable Long postId,
            @RequestHeader(value = "Authorization", required = false) String authHeader,
            HttpServletRequest request
    );


    ResponseEntity<AnimalInsertRequestDto> createAnimal(
            @AuthenticationPrincipal TokenUserInfo userInfo,
            @RequestPart("animalRequest") @Valid AnimalInsertRequestDto animalRequestDto,
            @RequestPart(value = "thumbnailImage") MultipartFile thumbnailImage);

    ResponseEntity<Void> updateAnimal(@PathVariable Long postId,
            @AuthenticationPrincipal TokenUserInfo userInfo,
            @RequestPart("animalRequest") @Valid AnimalUpdateRequestDto animalRequestDto,
            @RequestPart(value = "thumbnailImage") MultipartFile thumbnailImage);

    ResponseEntity<Void> deleteAnimal(@PathVariable Long postId, @AuthenticationPrincipal TokenUserInfo userInfo);

    ResponseEntity<?> reservationStatusAnimal(@PathVariable Long postId,
            @AuthenticationPrincipal TokenUserInfo userInfo,
            @RequestBody @Valid ReservationReqDto reservationReqDto);

}
