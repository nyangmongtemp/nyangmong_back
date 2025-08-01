package com.playdata.adminservice.admin.controller;

import com.playdata.adminservice.admin.dto.req.InformReplyReqDto;
import com.playdata.adminservice.admin.dto.req.InformSearchDto;
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

@Tag(name = "문의 게시판(Inform)", description = """
        문의사항을 관리하는 API
        
         ## 인증
            - 로그인 해야 조회 가능합니다.
            - BOSS, CUSTOMER 권한만 조회가 가능합니다.
        """)
public interface InformControllerDocs {

    @Operation(summary = "문의 목록 조회 (검색, 페이징)", description = "문의 목록 정보를 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200", description = "조회 성공", content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = TermsListResDto.class),
                    examples = @ExampleObject(value = SwaggerExampleConstants.INFORM_LIST_RESPONSE)
            ))
    })
    ResponseEntity<CommonResDto> getInformList(@ParameterObject InformSearchDto searchDto, @ParameterObject Pageable pageable);

    @Operation(summary = "문의 상세 조회", description = "문의 상세 정보를 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200", description = "조회 성공", content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = TermsListResDto.class),
                    examples = @ExampleObject(value = SwaggerExampleConstants.INFORM_DETAIL_RESPONSE)
            )),
            @ApiResponse(
                    responseCode = "404", description = "없는 데이터", content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = CommonException.class),
                    examples = @ExampleObject(value = SwaggerExampleConstants.COMMON_DETAIL_EXCEPTION)
            ))
    })
    ResponseEntity<CommonResDto> getInform(@PathVariable Long id);

    @Operation(summary = "문의 답변", description = "사용자의 문의글에 답변을 남깁니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "문의 답변 성공",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = CommonException.class),
                            examples = @ExampleObject(value = SwaggerExampleConstants.INFORM_REPLY_RESPONSE)
                    )
            ),
            @ApiResponse(
                    responseCode = "404", description = "없는 데이터", content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = CommonException.class),
                    examples = @ExampleObject(value = SwaggerExampleConstants.COMMON_DETAIL_EXCEPTION)
            ))
    })
    ResponseEntity<CommonResDto> replyInform(
            @PathVariable Long id,
            @RequestBody @Valid InformReplyReqDto informReplyReqDto,
            @Parameter(hidden = true) @AuthenticationPrincipal TokenAdminInfo adminInfo
    );
}