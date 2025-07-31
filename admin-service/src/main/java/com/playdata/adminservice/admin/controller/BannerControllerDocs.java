package com.playdata.adminservice.admin.controller;

import com.playdata.adminservice.admin.dto.banner.req.BannerModiReqDto;
import com.playdata.adminservice.admin.dto.banner.req.BannerSaveReqDto;
import com.playdata.adminservice.admin.dto.banner.req.OrderModiReqDto;
import com.playdata.adminservice.admin.dto.banner.res.BannerListResDto;
import com.playdata.adminservice.admin.dto.banner.res.BannerSaveResDto;
import com.playdata.adminservice.common.auth.TokenAdminInfo;
import com.playdata.adminservice.common.dto.CommonResDto;
import com.playdata.adminservice.common.enumeration.BannerSwaggerExample;
import com.playdata.adminservice.common.exception.CommonException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Tag(name = "배너 관리 (관리자 기능)", description = "메인화면에 노출된 배너 CRUD 관리하는 API")
public interface BannerControllerDocs {

    @Operation(summary = "배너 생성",
            description = """
               배너를 생성합니다.

               ## 인증
               - 로그인 해야 생성 가능합니다.
               - BOSS, CONTENT 권한만 생성이 가능합니다.
           """)
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201", description = "생성 성공", content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = BannerSaveResDto.class),
                    examples = @ExampleObject(value = BannerSwaggerExample.BANNER_CREATE_RESPONSE)
            )),
            @ApiResponse(
                    responseCode = "500", description = "파일 에러", content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = CommonException.class),
                    examples = @ExampleObject(value = BannerSwaggerExample.FILE_INVALID_ERROR)
            ))
    })
    ResponseEntity<CommonResDto> createBanner(@Parameter(hidden = true) @AuthenticationPrincipal TokenAdminInfo adminInfo,
                                                     @Parameter(
                                                             name = "banner",
                                                             description = "배너 JSON 데이터",
                                                             required = true,
                                                             content = @Content(mediaType = "application/json", schema = @Schema(implementation = BannerSaveReqDto.class))
                                                     )
                                                     @RequestPart("banner") @Valid BannerSaveReqDto animalRequestDto,
                                                     @Parameter(
                                                             name = "thumbnailImage",
                                                             description = "썸네일 이미지 파일",
                                                             required = true,
                                                             content = {
                                                                     @Content(mediaType = "image/jpeg"),
                                                                     @Content(mediaType = "image/png"),
                                                                     @Content(mediaType = "image/gif"),
                                                                     @Content(mediaType = "image/bmp"),
                                                                     @Content(mediaType = "image/webp")
                                                             }
                                                     )
                                                     @RequestPart("thumbnailImage") MultipartFile thumbnailImage);


    
    @Operation(summary = "배너 수정",
            description = """
               배너를 수정합니다.

               ## 인증
               - 로그인 해야 수정 가능합니다.
               - BOSS, CONTENT 권한만 수정이 가능합니다.
           """)
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200", description = "수정 성공", content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = BannerSaveResDto.class),
                    examples = @ExampleObject(value = BannerSwaggerExample.BANNER_UPDATE_RESPONSE)
            )),
            @ApiResponse(
                    responseCode = "400", description = "유효하지 않은 배너", content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = CommonException.class),
                    examples = @ExampleObject(value = BannerSwaggerExample.BANNER_BAD_REQUEST)
            )),
            @ApiResponse(
                    responseCode = "400", description = "기본 배너는 수정 불가", content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = CommonException.class),
                    examples = @ExampleObject(value = BannerSwaggerExample.BANNER_DEFAULT)
            )),
            @ApiResponse(
                    responseCode = "500", description = "파일 에러", content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = CommonException.class),
                    examples = @ExampleObject(value = BannerSwaggerExample.FILE_INVALID_ERROR)
            ))
    })
    ResponseEntity<CommonResDto> updateBanner(@Parameter(hidden = true) @AuthenticationPrincipal TokenAdminInfo adminInfo,
                                              @Parameter(
                                                      name = "banner",
                                                      description = "배너 JSON 데이터",
                                                      required = true,
                                                      content = @Content(mediaType = "application/json", schema = @Schema(implementation = BannerModiReqDto.class))
                                              )
                                              @RequestPart("banner") @Valid BannerModiReqDto animalRequestDto,
                                              @Parameter(
                                                      name = "thumbnailImage",
                                                      description = "썸네일 이미지 파일",
                                                      required = false,
                                                      content = {
                                                              @Content(mediaType = "image/jpeg"),
                                                              @Content(mediaType = "image/png"),
                                                              @Content(mediaType = "image/gif"),
                                                              @Content(mediaType = "image/bmp"),
                                                              @Content(mediaType = "image/webp")
                                                      }
                                              )
                                              @RequestPart("thumbnailImage") MultipartFile thumbnailImage);



    @Operation(summary = "배너 삭제",
            description = """
               배너를 삭제합니다.

               ## 인증
               - 로그인 해야 삭제 가능합니다.
               - BOSS, CONTENT 권한만 삭제가 가능합니다.
           """)
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200", description = "삭제 성공", content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = CommonException.class),
                    examples = @ExampleObject(value = BannerSwaggerExample.BANNER_DELETE_RESPONSE)
            )),
            @ApiResponse(
                    responseCode = "400", description = "유효하지 않은 배너", content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = CommonException.class),
                    examples = @ExampleObject(value = BannerSwaggerExample.BANNER_BAD_REQUEST)
            )),
            @ApiResponse(
                    responseCode = "400", description = "기본 배너는 수정 불가", content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = CommonException.class),
                    examples = @ExampleObject(value = BannerSwaggerExample.BANNER_DEFAULT)
            ))
    })
    ResponseEntity<CommonResDto> deleteBanner(@Parameter(hidden = true) @AuthenticationPrincipal TokenAdminInfo adminInfo,
                                                     @PathVariable(name = "id") Long bannerId);


    @Operation(summary = "배너 순서 수정",
            description = """
               배너들의 순서를 수정합니다.

               ## 인증
               - 로그인 해야 순서 수정이 가능합니다.
               - BOSS, CONTENT 권한만 순서 수정이 가능합니다.
           """)
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200", description = "수정 성공", content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = CommonException.class),
                    examples = @ExampleObject(value = BannerSwaggerExample.BANNER_ORDER_UPDATE)
            )),
            @ApiResponse(
                    responseCode = "400", description = "유효하지 않은 배너", content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = CommonException.class),
                    examples = @ExampleObject(value = BannerSwaggerExample.BANNER_BAD_REQUEST)
            )),
            @ApiResponse(
                    responseCode = "400", description = "노출 최대 개수 초과", content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = CommonException.class),
                    examples = @ExampleObject(value = BannerSwaggerExample.ORDER_OVER)
            )),
            @ApiResponse(
                    responseCode = "400", description = "순서 수정 요청 배너 개수 이상", content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = CommonException.class),
                    examples = @ExampleObject(value = BannerSwaggerExample.ORDER_NON)
            )),
            @ApiResponse(
                    responseCode = "400", description = "유효하지 않은 배너", content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = CommonException.class),
                    examples = @ExampleObject(value = BannerSwaggerExample.ORDER_BAD_REQUEST)
            ))

    })
    ResponseEntity<CommonResDto> changeOrders(@Parameter(hidden = true) @AuthenticationPrincipal TokenAdminInfo adminInfo,
                                              @RequestBody @Valid List<OrderModiReqDto> reqDtoList);


    @Operation(summary = "배너 노출 등록",
            description = """
               배너를 노출 시킵니다.

               ## 인증
               - 로그인 해야 노출이 가능합니다.
               - BOSS, CONTENT 권한만 노출이 가능합니다.
           """)
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200", description = "노출 등록 성공", content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = CommonException.class),
                    examples = @ExampleObject(value = BannerSwaggerExample.BANNER_ORDER_ENROLL)
            )),
            @ApiResponse(
                    responseCode = "400", description = "유효하지 않은 배너", content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = CommonException.class),
                    examples = @ExampleObject(value = BannerSwaggerExample.BANNER_BAD_REQUEST)
            )),
            @ApiResponse(
                    responseCode = "400", description = "기타 에러", content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = CommonException.class),
                    examples = @ExampleObject(value = BannerSwaggerExample.BAD_REQUEST_ETC)
            ))
    })
    ResponseEntity<CommonResDto> enrollOrder(@PathVariable(name = "id") Long bannerId);


    @Operation(summary = "배너 노출 해제",
            description = """
               배너를 노출 해체 시킵니다.

               ## 인증
               - 로그인 해야 노출 해제가 가능합니다.
               - BOSS, CONTENT 권한만 노출 해제가 가능합니다.
           """)
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200", description = "노출 해제 성공", content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = CommonException.class),
                    examples = @ExampleObject(value = BannerSwaggerExample.BANNER_ORDER_CANCEL)
            )),
            @ApiResponse(
                    responseCode = "400", description = "유효하지 않은 배너", content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = CommonException.class),
                    examples = @ExampleObject(value = BannerSwaggerExample.BANNER_BAD_REQUEST)
            )),
            @ApiResponse(
                    responseCode = "400", description = "기타 에러", content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = CommonException.class),
                    examples = @ExampleObject(value = BannerSwaggerExample.BAD_REQUEST_ETC)
            ))
    })
    ResponseEntity<CommonResDto> cancelOrder(@PathVariable(name = "id") Long bannerId);


    @Operation(summary = "노출 배너 조회",
            description = """
               노출된 배너를 조회합니다.

               ## 인증
               - 로그인 해야 조회가 가능합니다.
               - BOSS, CONTENT 권한만 조회가 가능합니다.
           """)
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200", description = "조회 성공", content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = BannerListResDto.class),
                    examples = @ExampleObject(value = BannerSwaggerExample.BANNER_EXPOSED)
            ))
    })
    ResponseEntity<CommonResDto> getExposedList();


    @Operation(summary = "배너 상세 조회",
            description = """
               배너의 상세 정보를 조회합니다.

               ## 인증
               - 로그인 해야 상세 조회가 가능합니다.
               - BOSS, CONTENT 권한만 상세 조회가 가능합니다.
           """)
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200", description = "상세 조회 성공", content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = BannerSaveResDto.class),
                    examples = @ExampleObject(value = BannerSwaggerExample.BANNER_CREATE_RESPONSE)
            )),
            @ApiResponse(
                    responseCode = "400", description = "유효하지 않은 배너", content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = CommonException.class),
                    examples = @ExampleObject(value = BannerSwaggerExample.BANNER_BAD_REQUEST)
            ))
    })
    ResponseEntity<CommonResDto> getDetail(@PathVariable(name = "id") Long bannerId);


    @Operation(summary = "비노출 배너 조회",
            description = """
               노출된 배너를 조회합니다.

               ## 인증
               - 로그인 해야 조회가 가능합니다.
               - BOSS, CONTENT 권한만 조회가 가능합니다.
           """)
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200", description = "조회 성공", content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = CommonException.class),
                    examples = @ExampleObject(value = BannerSwaggerExample.BANNER_UNEXPOSED)
            ))
    })
    ResponseEntity<CommonResDto> getUnorderedList(@Parameter(
                                                          name = "keyword",
                                                          description = "검색하고자 하는 배너의 이름",
                                                          required = false
                                                  )
                                                  @RequestParam(value = "keyword", required = false) String keyword,
                                                  @RequestParam(value = "page", defaultValue = "0") int page);


    @Operation(summary = "배너 노출 개수 수정",
            description = """
               배너 노출 개수를 수정합니다.

               ## 인증
               - 로그인 해야 수정이 가능합니다.
               - BOSS, CONTENT 권한만 수정이 가능합니다.
           """)
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200", description = "수정 성공", content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = CommonException.class),
                    examples = @ExampleObject(value = BannerSwaggerExample.ORDER_UPDATE)
            ))
    })
    ResponseEntity<CommonResDto> updateLimit(@Parameter(
            name = "count",
            description = "노출시키고자 하는 배너의 개수 (3개 이상)",
            required = true
    )@PathVariable(name = "count") @Min(3) Integer count);


    @Operation(summary = "배너 노출 개수 조회",
            description = """
               배너의 노출 개수를 조회합니다.

               ## 인증
               - 로그인 해야 조회가 가능합니다.
               - BOSS, CONTENT 권한만 조회가 가능합니다.
           """)
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200", description = "조회 성공", content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = CommonException.class),
                    examples = @ExampleObject(value = BannerSwaggerExample.ORDER_GET)
            ))
    })
    ResponseEntity<Long> getLimit();
}
