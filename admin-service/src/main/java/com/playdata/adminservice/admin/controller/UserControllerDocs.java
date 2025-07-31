package com.playdata.adminservice.admin.controller;

import com.playdata.adminservice.admin.dto.req.ReportUpdateReqDto;
import com.playdata.adminservice.admin.dto.req.UserSearchDto;
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
import jakarta.servlet.http.HttpServletRequest;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

@Tag(name = "사용자 게시판(User)", description = """
        사용자 목록 / 신고 관리하는 API
        
         ## 인증
            - 로그인 해야 조회 가능합니다.
            - BOSS, CONTENT 권한만 조회가 가능합니다.
        """)
public interface UserControllerDocs {

    @Operation(summary = "사용자 목록 조회 (검색, 페이징)", description = "사용자 목록 정보를 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200", description = "조회 성공", content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = TermsListResDto.class),
                    examples = @ExampleObject(value = SwaggerExampleConstants.USER_LIST_RESPONSE)
            ))
    })
    ResponseEntity<CommonResDto> userList(@ParameterObject UserSearchDto searchDto, @ParameterObject Pageable pageable);

    @Operation(summary = "사용자 상세 조회", description = "사용자 상세 정보를 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200", description = "조회 성공", content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = TermsListResDto.class),
                    examples = @ExampleObject(value = SwaggerExampleConstants.USER_DETAIL_RESPONSE)
            )),
            @ApiResponse(
                    responseCode = "404", description = "없는 데이터", content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = CommonException.class),
                    examples = @ExampleObject(value = SwaggerExampleConstants.COMMON_DETAIL_EXCEPTION)
            ))
    })
    ResponseEntity<CommonResDto> userDetail(
            @Parameter(hidden = true) @AuthenticationPrincipal TokenAdminInfo adminInfo,
            @PathVariable long id,
            HttpServletRequest request
    );

    @Operation(summary = "사용자의 신고 목록 조회", description = "사용자의 신고 목록 정보를 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200", description = "조회 성공", content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = TermsListResDto.class),
                    examples = @ExampleObject(value = SwaggerExampleConstants.USER_REPORT_LIST_RESPONSE)
            )),
            @ApiResponse(
                    responseCode = "404", description = "없는 데이터", content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = CommonException.class),
                    examples = @ExampleObject(value = SwaggerExampleConstants.COMMON_DETAIL_EXCEPTION)
            ))
    })
    ResponseEntity<CommonResDto> getReportList(@PathVariable long userId);

    @Operation(summary = "사용자의 신고내역 확인(신고내역 active true처리)", description = "사용자의 신고내역을 확인합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "사용자 신고내역 확인",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = CommonException.class),
                            examples = @ExampleObject(value = SwaggerExampleConstants.USER_REPORT_TREAT_RESPONSE)
                    )
            ),
            @ApiResponse(
                    responseCode = "404", description = "없는 데이터", content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = CommonException.class),
                    examples = @ExampleObject(value = SwaggerExampleConstants.COMMON_DETAIL_EXCEPTION)
            ))
    })
    ResponseEntity<CommonResDto> updateReportTreat(@PathVariable long id, @Parameter(hidden = true) @AuthenticationPrincipal TokenAdminInfo adminInfo);

    @Operation(summary = "사용자 정지", description = "사용자의 계정을 정지 시킵니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "사용자 계정 정지 성공",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = CommonException.class),
                            examples = @ExampleObject(value = SwaggerExampleConstants.USER_REPORT_RESPONSE)
                    )
            ),
            @ApiResponse(
                    responseCode = "404", description = "없는 데이터", content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = CommonException.class),
                    examples = @ExampleObject(value = SwaggerExampleConstants.COMMON_DETAIL_EXCEPTION)
            ))
    })
    ResponseEntity<CommonResDto> updateReport(@PathVariable long userId,
            @Parameter(hidden = true) @AuthenticationPrincipal TokenAdminInfo adminInfo,
            @RequestBody ReportUpdateReqDto reportUpdateReqDto
    );

}