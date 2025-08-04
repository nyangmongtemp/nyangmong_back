package com.playdata.adminservice.admin.controller;

import com.playdata.adminservice.admin.dto.board.AnimalSearchDto;
import com.playdata.adminservice.admin.dto.board.BoardSearchDto;
import com.playdata.adminservice.admin.dto.board.res.BoardListResDto;
import com.playdata.adminservice.common.auth.TokenAdminInfo;
import com.playdata.adminservice.common.dto.CommonResDto;
import com.playdata.adminservice.common.enumeration.BoardSwaggerExample;
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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;

@Tag(name = "관리자 게시판 관리", description = "관리자 게시판 관련 API")
public interface AdminBoardControllerDocs {

    @Operation(summary = "게시글 목록 조회",
            description = """
                게시글 목록 조회를 진행합니다.
            
                ## 인증
                - BOSS/CUSTOMER 권한을 가진 사람만 가능합니다.
            """)
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200", description = "목록 조회 성공", content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = CommonException.class),
                    examples = @ExampleObject(value = BoardSwaggerExample.ADMIN_LIST)
            )),
            @ApiResponse(
                    responseCode = "403", description = "접근 권한이 없음", content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = CommonException.class),
                    examples = @ExampleObject(value = BoardSwaggerExample.FORBIDDEN)
            ))
    })
    ResponseEntity<Page<BoardListResDto>> getBoardList(@Parameter(hidden = true) BoardSearchDto boardSearchDto,
                                                       @PathVariable String category,
                                                       Pageable pageable);

    @Operation(summary = "분양 게시글 목록 조회",
            description = """
                분양 게시글 목록 조회를 진행합니다.
            
                ## 인증
                - BOSS/CUSTOMER 권한을 가진 사람만 가능합니다.
            """)
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200", description = "분양 게시글 목록 조회 성공", content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = CommonException.class),
                    examples = @ExampleObject(value = BoardSwaggerExample.ADMIN_ANIMAL_LIST)
            )),
            @ApiResponse(
                    responseCode = "403", description = "접근 권한이 없음", content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = CommonException.class),
                    examples = @ExampleObject(value = BoardSwaggerExample.FORBIDDEN)
            ))
    })
    ResponseEntity<CommonResDto> getAnimalList(@Parameter(hidden = true) AnimalSearchDto searchDto, Pageable pageable);

    @Operation(summary = "게시글 상세 조회",
            description = """
                게시글 상세 조회를 진행합니다.
            
                ## 인증
                - BOSS/CUSTOMER 권한을 가진 사람만 가능합니다.
            """)
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200", description = "게시글 상세조회 성공", content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = CommonException.class),
                    examples = @ExampleObject(value = BoardSwaggerExample.BOARD_DETAIL)
            )),
            @ApiResponse(
                    responseCode = "400", description = "게시물이 없음", content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = CommonException.class),
                    examples = @ExampleObject(value = BoardSwaggerExample.DATE_NOT_FOUND)
            )),
            @ApiResponse(
                    responseCode = "403", description = "접근 권한이 없음", content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = CommonException.class),
                    examples = @ExampleObject(value = BoardSwaggerExample.FORBIDDEN)
            ))
    })
    ResponseEntity<CommonResDto> getBoardDetail(@Parameter(hidden = true) @PathVariable String category,
                                                @PathVariable(name = "id") Long postId,
                                                @RequestHeader(value = "Authorization", required = false) String authHeader,
                                                HttpServletRequest request);

    @Operation(summary = "분양 게시글 상세 조회",
            description = """
                분양 게시글 상세 조회를 진행합니다.
            
                ## 인증
                - BOSS/CUSTOMER 권한을 가진 사람만 가능합니다.
            """)
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200", description = "분양 게시글 상세 조회 성공", content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = CommonException.class),
                    examples = @ExampleObject(value = BoardSwaggerExample.BOARD_ANIMAL_DETAIL)
            )),
            @ApiResponse(
                    responseCode = "400", description = "게시물이 없음", content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = CommonException.class),
                    examples = @ExampleObject(value = BoardSwaggerExample.DATE_NOT_FOUND)
            )),
            @ApiResponse(
                    responseCode = "403", description = "접근 권한이 없음", content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = CommonException.class),
                    examples = @ExampleObject(value = BoardSwaggerExample.FORBIDDEN)
            ))
    })
    ResponseEntity<CommonResDto> getAnimal(@Parameter(hidden = true) @PathVariable Long postId,
                                           @RequestHeader(value = "Authorization", required = false) String authHeader,
                                           HttpServletRequest request);

    @Operation(summary = "게시글 삭제",
            description = """
                게시글 삭제를 진행합니다.
            
                ## 인증
                - BOSS/CUSTOMER 권한을 가진 사람만 가능합니다.
            """)
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200", description = "게시글 삭제 성공", content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = CommonException.class),
                    examples = @ExampleObject(value = BoardSwaggerExample.BOARD_DELETE)
            )),
            @ApiResponse(
                    responseCode = "400", description = "게시물이 없음", content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = CommonException.class),
                    examples = @ExampleObject(value = BoardSwaggerExample.DATE_NOT_FOUND)
            )),
            @ApiResponse(
                    responseCode = "400", description = "카테고리 값이 다름", content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = CommonException.class),
                    examples = @ExampleObject(value = BoardSwaggerExample.DATE_NOT_FOUND)
            )),
            @ApiResponse(
                    responseCode = "403", description = "접근 권한이 없음", content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = CommonException.class),
                    examples = @ExampleObject(value = BoardSwaggerExample.FORBIDDEN)
            ))
    })
    ResponseEntity<CommonResDto> deleteBoard(@Parameter(hidden = true) @PathVariable String category,
                                             @PathVariable Long postId,
                                             @AuthenticationPrincipal TokenAdminInfo adminInfo);
}
