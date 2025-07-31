package com.playdata.adminservice.admin.controller;

import com.playdata.adminservice.admin.dto.req.SearchDto;
import com.playdata.adminservice.admin.dto.req.TermsInsertReqDto;
import com.playdata.adminservice.admin.dto.req.TermsUpdateReqDto;
import com.playdata.adminservice.admin.dto.res.TermsListResDto;
import com.playdata.adminservice.common.auth.TokenAdminInfo;
import com.playdata.adminservice.common.dto.CommonResDto;
import com.playdata.adminservice.common.enumeration.SwaggerExampleConstants;
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
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

@Tag(name = "약관/방침/QNA 게시판(Terms)", description = """
        이용약관 / 개인정보처리방침 / QNA 게시판 CRUD 관리하는 API
        
         ## 인증
            - 로그인 해야 조회 가능합니다.
            - BOSS, CUSTOMER 권한만 조회가 가능합니다.
        """)
public interface TermsControllerDocs {

    @Operation(summary = "약관/방침/QNA 목록 조회 (검색, 페이징)", description = "약관/방침/QNA 게시물의 목록 정보를 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200", description = "조회 성공", content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = TermsListResDto.class),
                    examples = @ExampleObject(value = SwaggerExampleConstants.TERMS_LIST_RESPONSE)
            )),
            @ApiResponse(
                    responseCode = "400", description = "잘못된 요청", content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = CommonException.class),
                    examples = @ExampleObject(value = SwaggerExampleConstants.COMMON_BAD_REQUEST)
            ))
    })
    ResponseEntity<CommonResDto> getTermsList(
            @Parameter(
                    name = "category",
                    description = "TERMS(이용약관), PRIVACY(개인정보처리방침), QNA(QNA) 중 하나",
                    required = true
            )
            @PathVariable String category,
            @ParameterObject SearchDto searchDto,
            @ParameterObject Pageable pageable);

    @Operation(summary = "약관/방침/QNA 상세 조회", description = "약관/방침/QNA 게시물의 상세 정보를 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200", description = "조회 성공", content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = TermsListResDto.class),
                    examples = @ExampleObject(value = SwaggerExampleConstants.TERMS_DETAIL_RESPONSE)
            )),
            @ApiResponse(
                    responseCode = "400", description = "잘못된 요청", content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = CommonException.class),
                    examples = @ExampleObject(value = SwaggerExampleConstants.COMMON_BAD_REQUEST)
            )),
            @ApiResponse(
                    responseCode = "404", description = "없는 데이터", content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = CommonException.class),
                    examples = @ExampleObject(value = SwaggerExampleConstants.COMMON_DETAIL_EXCEPTION)
            ))
    })
    ResponseEntity<CommonResDto> getTerms(
            @Parameter(
                    name = "category",
                    description = "TERMS(이용약관), PRIVACY(개인정보처리방침), QNA(QNA) 중 하나",
                    required = true
            )
            @PathVariable String category,
            @PathVariable Long id);

    @Operation(summary = "약관/방침/QNA 게시물 생성", description = "약관/방침/QNA 게시물을 생성합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "약관/방침/QNA 게시물 생성 성공",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = CommonException.class),
                            examples = @ExampleObject(value = SwaggerExampleConstants.CREATE_TERMS_SUCCESS)
                    )
            ),
            @ApiResponse(
                    responseCode = "400", description = "잘못된 요청",
                    content = @Content(mediaType = "application/json",
                        schema = @Schema(implementation = CommonException.class),
                        examples = @ExampleObject(value = SwaggerExampleConstants.COMMON_BAD_REQUEST)
                    )
            )
    })
    ResponseEntity<CommonResDto> createTerms(
            @Parameter(hidden = true) @AuthenticationPrincipal TokenAdminInfo adminInfo,
            @Parameter(
                    name = "category",
                    description = "TERMS(이용약관), PRIVACY(개인정보처리방침), QNA(QNA) 중 하나",
                    required = true
            )
            @PathVariable String category,
            @RequestBody @Valid TermsInsertReqDto termsInsertReqDto);

    @Operation(summary = "약관/방침/QNA 게시물 수정", description = "약관/방침/QNA 게시물을 수정합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "약관/방침/QNA 게시물 수정 성공",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = CommonException.class),
                            examples = @ExampleObject(value = SwaggerExampleConstants.UPDATE_TERMS_SUCCESS)
                    )
            ),
            @ApiResponse(
                    responseCode = "400", description = "잘못된 요청",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = CommonException.class),
                            examples = @ExampleObject(value = SwaggerExampleConstants.COMMON_BAD_REQUEST)
                    )
            ),
            @ApiResponse(
                    responseCode = "404", description = "없는 데이터", content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = CommonException.class),
                    examples = @ExampleObject(value = SwaggerExampleConstants.COMMON_DETAIL_EXCEPTION)
            ))
    })
    ResponseEntity<CommonResDto> updateTerms(
            @Parameter(hidden = true) @AuthenticationPrincipal TokenAdminInfo adminInfo,
            @PathVariable Long id,
            @Parameter(
                    name = "category",
                    description = "TERMS(이용약관), PRIVACY(개인정보처리방침), QNA(QNA) 중 하나",
                    required = true
            )
            @PathVariable String category,
            @RequestBody @Valid TermsUpdateReqDto termsUpdateReqDto);

    @Operation(summary = "약관/방침/QNA 게시물 삭제", description = "약관/방침/QNA 게시물을 삭제합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "약관/방침/QNA 게시물 삭제 성공",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = CommonException.class),
                            examples = @ExampleObject(value = SwaggerExampleConstants.DELETE_TERMS_SUCCESS)
                    )
            ),
            @ApiResponse(
                    responseCode = "400", description = "잘못된 요청",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = CommonException.class),
                            examples = @ExampleObject(value = SwaggerExampleConstants.COMMON_BAD_REQUEST)
                    )
            ),
            @ApiResponse(
                    responseCode = "404", description = "없는 데이터", content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = CommonException.class),
                    examples = @ExampleObject(value = SwaggerExampleConstants.COMMON_DETAIL_EXCEPTION)
            ))
    })
    ResponseEntity<CommonResDto> deleteTerms(
            @PathVariable Long id,
            @Parameter(
                    name = "category",
                    description = "TERMS(이용약관), PRIVACY(개인정보처리방침), QNA(QNA) 중 하나",
                    required = true
            )
            @PathVariable String category);

    @Operation(summary = "약관 마지막 게시글 조회", description = "약관 마지막 게시물을 조회합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "약관 마지막 게시글 조회",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = CommonException.class),
                            examples = @ExampleObject(value = SwaggerExampleConstants.LAST_TERMS_SUCCESS)
                    )
            ),
            @ApiResponse(
                    responseCode = "200", description = "없는 데이터", content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = CommonException.class),
                    examples = @ExampleObject(value = SwaggerExampleConstants.LAST_TERMS_NULL_SUCCESS)
            )),
            @ApiResponse(
                    responseCode = "400", description = "잘못된 요청",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = CommonException.class),
                            examples = @ExampleObject(value = SwaggerExampleConstants.COMMON_BAD_REQUEST)
                    )
            )
    })
    ResponseEntity<CommonResDto> getLastPostTerms(
            @Parameter(
                    name = "category",
                    description = "TERMS(이용약관)",
                    required = true
            )
            @PathVariable String category);
}