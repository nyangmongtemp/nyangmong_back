package com.playdata.adminservice.admin.controller;

import com.playdata.adminservice.admin.dto.AdminSearchDto;
import com.playdata.adminservice.admin.dto.req.*;
import com.playdata.adminservice.admin.dto.res.AdminEmailAuthResDto;
import com.playdata.adminservice.admin.dto.res.AdminListResDto;
import com.playdata.adminservice.common.auth.TokenAdminInfo;
import com.playdata.adminservice.common.dto.CommonResDto;
import com.playdata.adminservice.common.enumeration.AdminSwaggerExample;
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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

@Tag(name = "관리자 서비스", description = "관리자 관련 API")
public interface AdminControllerDocs {

    @Operation(summary = "관리자 등록",
            description = """
               관리자 등록을 진행합니다.

           """)
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201", description = "관리자 생성에 성공하였습니다.", content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = CommonException.class),
                    examples = @ExampleObject(value = AdminSwaggerExample.ADMIN_CREATE)
            )),
            @ApiResponse(
                    responseCode = "400", description = "중복 이메일", content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = CommonException.class),
                    examples = @ExampleObject(value = AdminSwaggerExample.DUPLICATED_DATA)
            ))
    })
    ResponseEntity<CommonResDto> adminPlus(@RequestBody AdminSaveReqDto adminSaveReqDto);

    @Operation(summary = "로그인 요청",
            description = """
                로그인 요청을 진행합니다.
            
            """)
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200", description = "2차 인증 이메일 발송 완료", content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = CommonException.class),
                    examples = @ExampleObject(value = AdminSwaggerExample.ADMIN_LOGIN_EMAIL)
            )),
            @ApiResponse(
                    responseCode = "401", description = "이메일 오류", content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = CommonException.class),
                    examples = @ExampleObject(value = AdminSwaggerExample.ACCOUNT_NOT_FOUND)
            )),
            @ApiResponse(
                    responseCode = "401", description = "탈퇴한 관리자", content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = CommonException.class),
                    examples = @ExampleObject(value = AdminSwaggerExample.ACCOUNT_DISABLED)
            )),
            @ApiResponse(
                    responseCode = "401", description = "비밀번호 오류", content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = CommonException.class),
                    examples = @ExampleObject(value = AdminSwaggerExample.INVALID_PASSWORD)
            ))
    })
    ResponseEntity<CommonResDto> adminLogin(@RequestBody @Valid AdminLoginReqDto adminLoginReqDto);

    @Operation(summary = "로그인 2차 인증 검증",
            description = """
                로그인 2차 인증 검증을 진행합니다.
            
            """)
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200", description = "로그인 성공", content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = CommonException.class),
                    examples = @ExampleObject(value = AdminSwaggerExample.ADMIN_LOGIN)
            )),
            @ApiResponse(
                    responseCode = "400", description = "검증 실패", content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = CommonException.class),
                    examples = @ExampleObject(value = AdminSwaggerExample.INVALID_PARAMETER)
            )),
            @ApiResponse(
                    responseCode = "400", description = "관리자 정보 없음", content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = CommonException.class),
                    examples = @ExampleObject(value = AdminSwaggerExample.BAD_REQUEST)
            ))
    })
    ResponseEntity<CommonResDto> verifyAdminEmailCode(@RequestBody @Valid AdminEmailAuthResDto authResDto);

    @Operation(summary = "이메일 변경 요청",
            description = """
                이메일 변경 요청을 진행합니다.
            
            """)
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200", description = "이메일 변경 요청 성공", content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = CommonException.class),
                    examples = @ExampleObject(value = AdminSwaggerExample.ADMIN_EMAIL_CODE)
            )),
            @ApiResponse(
                    responseCode = "400", description = "검증 실패", content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = CommonException.class),
                    examples = @ExampleObject(value = AdminSwaggerExample.DUPLICATED_DATA)
            ))
    })
    ResponseEntity<CommonResDto> emailModify(@Parameter(hidden = true) @AuthenticationPrincipal TokenAdminInfo tokenAdminInfo,
                                             @RequestParam String newEmail);

    @Operation(summary = "이메일 변경 검증",
            description = """
                이메일 변경 요청 검증을 진행합니다.
            
            """)
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200", description = "이메일 변경 요청 검증", content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = CommonException.class),
                    examples = @ExampleObject(value = AdminSwaggerExample.ADMIN_EMAIL_VERIFY)
            )),
            @ApiResponse(
                    responseCode = "400", description = "검증 실패", content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = CommonException.class),
                    examples = @ExampleObject(value = AdminSwaggerExample.BAD_REQUEST)
            ))
    })
    ResponseEntity<CommonResDto> verifyNewEmail(@Parameter(hidden = true) @AuthenticationPrincipal TokenAdminInfo adminInfo,
                                                @RequestBody @Valid AdminEmailAuthResDto authResDto);

    @Operation(summary = "비밀번호 변경 요청",
            description = """
                비밀번호 변경 요청을 진행합니다.
            
            """)
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200", description = "이메일 변경 요청 검증", content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = CommonException.class),
                    examples = @ExampleObject(value = AdminSwaggerExample.ADMIN_PASSWORD)
            )),
            @ApiResponse(
                    responseCode = "401", description = "활성화 상태 아님", content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = CommonException.class),
                    examples = @ExampleObject(value = AdminSwaggerExample.ACCOUNT_NOT_FOUND)
            ))
    })
    ResponseEntity<CommonResDto> passwordModifyReq(@Parameter(hidden = true) @AuthenticationPrincipal TokenAdminInfo adminInfo);

    @Operation(summary = "비밀번호 변경 요청 검증",
            description = """
                비밀번호 변경 요청 검증을 진행합니다.
            
            """)
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200", description = "이메일 변경 요청 검증", content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = CommonException.class),
                    examples = @ExampleObject(value = AdminSwaggerExample.ADMIN_PASSWORD_VERIFY)
            )),
            @ApiResponse(
                    responseCode = "400", description = "활성화 상태 아님", content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = CommonException.class),
                    examples = @ExampleObject(value = AdminSwaggerExample.INVALID_AUTH_CODE)
            ))
    })
    ResponseEntity<CommonResDto> verifyNewPassword(@Parameter(hidden = true) @AuthenticationPrincipal TokenAdminInfo adminInfo,
                                                   @RequestBody @Valid AdminPasswordAuthReqDto authReqDto);

    @Operation(summary = "비밀번호 변경",
            description = """
                비밀번호 변경을 진행합니다.
            
            """)
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200", description = "이메일 변경", content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = CommonException.class),
                    examples = @ExampleObject(value = AdminSwaggerExample.ADMIN_PASSWORD_MODIFY)
            )),
            @ApiResponse(
                    responseCode = "401", description = "계정이 존재하지 않음", content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = CommonException.class),
                    examples = @ExampleObject(value = AdminSwaggerExample.ACCOUNT_NOT_FOUND)
            )),
            @ApiResponse(
                    responseCode = "401", description = "계정이 활성화 상태가 아님", content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = CommonException.class),
                    examples = @ExampleObject(value = AdminSwaggerExample.ACCOUNT_NOT_FOUND)
            ))
    })
    ResponseEntity<CommonResDto> modifyPassword(@Parameter(hidden = true) @AuthenticationPrincipal TokenAdminInfo adminInfo,
                                                @RequestBody AdminPasswordModifyReqDto modifyReqDto);

    @Operation(summary = "관리자 정보 변경",
            description = """
                관리자 정보 변경을 진행합니다.
            
            """)
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200", description = "정보 변경 성공", content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = CommonException.class),
                    examples = @ExampleObject(value = AdminSwaggerExample.ADMIN_MODIFY)
            )),
            @ApiResponse(
                    responseCode = "401", description = "계정이 존재하지 않음", content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = CommonException.class),
                    examples = @ExampleObject(value = AdminSwaggerExample.ACCOUNT_NOT_FOUND)
            )),
            @ApiResponse(
                    responseCode = "401", description = "계정이 활성화 상태가 아님", content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = CommonException.class),
                    examples = @ExampleObject(value = AdminSwaggerExample.ACCOUNT_NOT_FOUND)
            )),
            @ApiResponse(
                    responseCode = "400", description = "동일한 데이터", content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = CommonException.class),
                    examples = @ExampleObject(value = AdminSwaggerExample.DUPLICATED_DATA)
            ))
    })
    ResponseEntity<CommonResDto> modify(@Parameter(hidden = true) @AuthenticationPrincipal TokenAdminInfo adminInfo,
                                        @RequestBody AdminModifyReqDto modifyReqDto);

    @Operation(summary = "관리자 목록 조회",
            description = """
                관리자 목록 조회를 진행합니다.
                
                ## 인증
                - BOSS 권한을 가진 사람만 가능합니다.
            """)
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200", description = "목록 조회 성공", content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = CommonException.class),
                    examples = @ExampleObject(value = AdminSwaggerExample.ADMIN_LIST)
            )),
            @ApiResponse(
                    responseCode = "403", description = "접근 권한이 없음", content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = CommonException.class),
                    examples = @ExampleObject(value = AdminSwaggerExample.FORBIDDEN)
            ))
    })
    ResponseEntity<Page<AdminListResDto>> adminList(@Parameter(hidden = true) AdminSearchDto adminSearchDto, Pageable pageable);

    @Operation(summary = "관리자 권한, 활성화 상태 변경",
            description = """
                관리자 권한, 활성화 상태를 변경합니다.
            
                ## 인증
                - BOSS 권한을 가진 사람만 가능합니다.
            """)
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200", description = "목록 조회 성공", content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = CommonException.class),
                    examples = @ExampleObject(value = AdminSwaggerExample.ADMIN_ROLE_MODIFY)
            )),
            @ApiResponse(
                    responseCode = "401", description = "변경하려는 관리자가 없음", content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = CommonException.class),
                    examples = @ExampleObject(value = AdminSwaggerExample.ACCOUNT_NOT_FOUND)
            )),
            @ApiResponse(
                    responseCode = "403", description = "접근 권한이 없음", content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = CommonException.class),
                    examples = @ExampleObject(value = AdminSwaggerExample.FORBIDDEN)
            ))
    })
    ResponseEntity<CommonResDto> roleModify(@Parameter(hidden = true) @RequestBody AdminRoleModifyReqDto adminRoleModifyReqDto);

    @Operation(summary = "관리자 마이페이지 정보 조회",
            description = """
                관리자 마이페이지 정보를 조회합니다.
            
            """)
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200", description = "정보 조회 성공", content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = CommonException.class),
                    examples = @ExampleObject(value = AdminSwaggerExample.ADMIN_MYPAGE)
            )),
            @ApiResponse(
                    responseCode = "401", description = "변경하려는 관리자가 없음", content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = CommonException.class),
                    examples = @ExampleObject(value = AdminSwaggerExample.ACCOUNT_NOT_FOUND)
            ))
    })
    ResponseEntity<CommonResDto> getMyPage(@Parameter(hidden = true) @AuthenticationPrincipal TokenAdminInfo adminInfo);
}
