package com.playdata.adminservice.admin.controller;

import com.playdata.adminservice.admin.dto.req.AdminLogSearchDto;
import com.playdata.adminservice.admin.dto.req.UserSearchDto;
import com.playdata.adminservice.admin.dto.res.TermsListResDto;
import com.playdata.adminservice.common.dto.CommonResDto;
import com.playdata.adminservice.common.enumeration.SwaggerExampleConstants;
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

@Tag(name = "관리자 로그 게시판(AdminLog)", description = """
        관리자의 로그를 관리하는 API
        
         ## 인증
            - 로그인 해야 조회 가능합니다.
            - BOSS, CUSTOMER 권한만 조회가 가능합니다.
        """)
public interface AdminLogControllerDocs {

    @Operation(summary = "관리자 로그 목록 조회 (검색, 페이징)", description = "관리자의 로그기록 목록 정보를 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200", description = "조회 성공", content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = TermsListResDto.class),
                    examples = @ExampleObject(value = SwaggerExampleConstants.ADMIN_LOG_LIST_RESPONSE)
            ))
    })
    ResponseEntity<?> getAdminLogList(@ParameterObject AdminLogSearchDto searchDto, @ParameterObject Pageable pageable);

}