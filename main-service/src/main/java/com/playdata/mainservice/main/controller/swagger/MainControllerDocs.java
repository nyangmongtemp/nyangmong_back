package com.playdata.mainservice.main.controller.swagger;

import com.playdata.mainservice.common.auth.TokenUserInfo;
import com.playdata.mainservice.common.dto.CommonResDto;
import com.playdata.mainservice.common.enumeration.MainSwaggerEx;
import com.playdata.mainservice.common.exception.CommonException;
import com.playdata.mainservice.main.dto.req.*;
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
import org.springframework.web.bind.annotation.RequestParam;

@Tag(name = "댓글, 대댓글, 좋아요 컨트롤러", description = "댓글, 대댓글, 좋아요 조회 및 CRUD를 담당하는 API")
public interface MainControllerDocs {

    @Operation(summary = "좋아요 생성, 취소",
            description = """
               좋아요를 생성하거나, 기존 좋아요를 취소합니다.

               ## 인증
               - 로그인 해야 순서 수정이 가능합니다.
           """)
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200", description = "생성 성공", content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = CommonException.class),
                    examples = @ExampleObject(value = MainSwaggerEx.LIKE_CREATE)
            ))

    })
    ResponseEntity<CommonResDto> createLike(@Parameter(hidden = true)  @AuthenticationPrincipal TokenUserInfo userInfo
            ,@RequestBody @Valid MainLikeReqDto reqDto);


    @Operation(summary = "댓글 생성",
            description = """
               댓글을 생성합니다.

               ## 인증
               - 로그인 해야 순서 수정이 가능합니다.
           """)
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201", description = "생성 성공", content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = CommonException.class),
                    examples = @ExampleObject(value = MainSwaggerEx.COMMENT_CREATE)
            )),
            @ApiResponse(
                    responseCode = "500", description = "유저로부터 profileImage 받기 실패",
                    content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = CommonException.class),
                    examples = @ExampleObject(value = MainSwaggerEx.INTERNAL_SERVER_ERROR)
            ))
    })
    ResponseEntity<CommonResDto> createComment(@Parameter(hidden = true)  @AuthenticationPrincipal TokenUserInfo userInfo,
                                               @RequestBody @Valid MainComReqDto reqDto);


    @Operation(summary = "댓글 삭제",
            description = """
               댓글을 삭제합니다.

               ## 인증
               - 로그인 해야 순서 수정이 가능합니다.
               - 본인이 작성한 댓글만 삭제가 가능합니다.
           """)
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200", description = "삭제 성공", content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = CommonException.class),
                    examples = @ExampleObject(value = MainSwaggerEx.COMMENT_DELETE)
            )),
            @ApiResponse(
                    responseCode = "404", description = "삭제할 댓글이 없습니다.",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = CommonException.class),
                            examples = @ExampleObject(value = MainSwaggerEx.NO_COMMENT)
            )),
            @ApiResponse(
                    responseCode = "403", description = "삭제 권한이 없습니다.",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = CommonException.class),
                            examples = @ExampleObject(value = MainSwaggerEx.NO_DELETE_PERMISSION)
                    ))
    })
    ResponseEntity<CommonResDto> deleteComment(@Parameter(hidden = true)  @AuthenticationPrincipal TokenUserInfo userInfo,
                                               @PathVariable(name = "id") Long commentId);


    @Operation(summary = "댓글 수정",
            description = """
               댓글을 수정합니다.

               ## 인증
               - 로그인 해야 순서 수정이 가능합니다.
               - 본인이 작성한 댓글만 수정이 가능합니다.
           """)
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200", description = "수정 성공", content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = CommonException.class),
                    examples = @ExampleObject(value = MainSwaggerEx.COMMENT_MODIFY)
            )),
            @ApiResponse(
                    responseCode = "404", description = "수정할 댓글이 없습니다.",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = CommonException.class),
                            examples = @ExampleObject(value = MainSwaggerEx.NO_COMMENT)
                    )),
            @ApiResponse(
                    responseCode = "403", description = "수정 권한이 없습니다.",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = CommonException.class),
                            examples = @ExampleObject(value = MainSwaggerEx.NO_DELETE_PERMISSION)
                    ))
    })
    ResponseEntity<CommonResDto> modifyComment(@Parameter(hidden = true)  @AuthenticationPrincipal TokenUserInfo userInfo,
                                               @RequestBody @Valid ComModiReqDto reqDto);


    @Operation(summary = "대댓글 생성",
            description = """
               대댓글을 생성합니다.

               ## 인증
               - 로그인 해야 순서 수정이 가능합니다.
           """)
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201", description = "생성 성공", content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = CommonException.class),
                    examples = @ExampleObject(value = MainSwaggerEx.REPLY_CREATE)
            )),
            @ApiResponse(
                    responseCode = "500", description = "유저로부터 profileImage 받기 실패",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = CommonException.class),
                            examples = @ExampleObject(value = MainSwaggerEx.INTERNAL_SERVER_ERROR)
                    )),
            @ApiResponse(
                    responseCode = "404", description = "대댓글을 작성할 댓글이 없습니다.",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = CommonException.class),
                            examples = @ExampleObject(value = MainSwaggerEx.NO_COMMENT)
                    )),
    })
    ResponseEntity<CommonResDto> createReply(@Parameter(hidden = true)  @AuthenticationPrincipal TokenUserInfo userInfo,
                                             @RequestBody @Valid ReplySaveReqDto reqDto);

    @Operation(summary = "대댓글 삭제",
            description = """
               댓글을 삭제합니다.

               ## 인증
               - 로그인 해야 순서 수정이 가능합니다.
               - 본인이 작성한 대댓글만 삭제가 가능합니다.
           """)
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200", description = "삭제 성공", content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = CommonException.class),
                    examples = @ExampleObject(value = MainSwaggerEx.REPLY_DELETE)
            )),
            @ApiResponse(
                    responseCode = "404", description = "삭제할 대댓글이 없습니다.",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = CommonException.class),
                            examples = @ExampleObject(value = MainSwaggerEx.NO_REPLY)
                    )),
            @ApiResponse(
                    responseCode = "403", description = "삭제 권한이 없습니다.",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = CommonException.class),
                            examples = @ExampleObject(value = MainSwaggerEx.NO_DELETE_PERMISSION)
                    ))
    })
    ResponseEntity<CommonResDto> deleteReply(@Parameter(hidden = true)  @AuthenticationPrincipal TokenUserInfo userInfo
            ,@PathVariable(name = "id") Long replyId);



    @Operation(summary = "대댓글 수정",
            description = """
               대댓글을 수정합니다.

               ## 인증
               - 로그인 해야 순서 수정이 가능합니다.
               - 본인이 작성한 대댓글만 수정이 가능합니다.
           """)
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200", description = "수정 성공", content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = CommonException.class),
                    examples = @ExampleObject(value = MainSwaggerEx.REPLY_CREATE)
            )),
            @ApiResponse(
                    responseCode = "404", description = "수정할 대댓글이 없습니다.",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = CommonException.class),
                            examples = @ExampleObject(value = MainSwaggerEx.NO_REPLY)
                    )),
            @ApiResponse(
                    responseCode = "403", description = "수정 권한이 없습니다.",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = CommonException.class),
                            examples = @ExampleObject(value = MainSwaggerEx.NO_DELETE_PERMISSION)
                    ))
    })
    ResponseEntity<CommonResDto> modifyReply(@Parameter(hidden = true)  @AuthenticationPrincipal TokenUserInfo userInfo,
                                             @RequestBody @Valid ReplyModiReqDto reqDto);


    @Operation(summary = "게시물 댓글, 좋아요 개수 조회",
            description = """
               게시물의 댓글 개수, 좋아요 개수를 조회합니다.
           """)
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200", description = "조회 성공", content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = CommonException.class),
                    examples = @ExampleObject(value = MainSwaggerEx.POST_DETAIL)
            )),
    })
    ResponseEntity<CommonResDto> getDetailLikeCommentCount(@RequestBody LikeComCountReqDto reqDto);



    @Operation(summary = "댓글 페이징 조회",
            description = """
               게시물의 댓글 정보를 조회합니다.
           """)
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200", description = "조회 성공", content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = CommonException.class),
                    examples = @ExampleObject(value = MainSwaggerEx.COMMENT_DETAIL)
            )),
    })
    ResponseEntity<CommonResDto> getCommentList(@RequestBody LikeComCountReqDto reqDto,@ParameterObject Pageable pageable);



    @Operation(summary = "대댓글 조회",
            description = """
               댓글의 대댓글 정보를 조회합니다.
           """)
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200", description = "조회 성공", content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = CommonException.class),
                    examples = @ExampleObject(value = MainSwaggerEx.REPLY_DETAIL)
            )),
    })
    ResponseEntity<CommonResDto> getReplyList(@PathVariable(name = "id") Long commentId);



    @Operation(summary = "내 댓글 조회",
            description = """
               내가 작성한 댓글 정보를 페이징 조회합니다.
           """)
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200", description = "조회 성공", content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = CommonException.class),
                    examples = @ExampleObject(value = MainSwaggerEx.MY_COMMENT)
            )),
    })
    ResponseEntity<CommonResDto> getMyComment(
            @Parameter(hidden = true)  @AuthenticationPrincipal TokenUserInfo userInfo,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size,
            @RequestParam(value = "sort", defaultValue = "commentId,desc") String sort
    );


    @Operation(summary = "비공개 댓글 열람 요청",
            description = """
               비공개 댓글을 열람합니다.

               ## 인증
               - 로그인 해야 순서 수정이 가능합니다.
               - 게시물 작성자 또는 댓글 작성자만 열람이 가능합니다.
           """)
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200", description = "수정 성공", content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = CommonException.class),
                    examples = @ExampleObject(value = MainSwaggerEx.CAN_SEE)
            )),
            @ApiResponse(
                    responseCode = "404", description = "열람할 비공개 댓글이 없습니다.",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = CommonException.class),
                            examples = @ExampleObject(value = MainSwaggerEx.NO_COMMENT)
                    )),
            @ApiResponse(
                    responseCode = "403", description = "열람 권한이 없습니다.",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = CommonException.class),
                            examples = @ExampleObject(value = MainSwaggerEx.NO_DELETE_PERMISSION)
                    ))
    })
    ResponseEntity<Boolean> getCommentHidden(@Parameter(hidden = true)  @AuthenticationPrincipal TokenUserInfo userInfo,
                                             @RequestBody @Valid SeeHideComReqDto reqDto);


    
    @Operation(summary = "내 좋아요 조회",
            description = """
               해당 게시물에 좋아요를 눌렀는 지 확인하는 기능입니다.
           
               ## 인증
               - 로그인 해야 순서 수정이 가능합니다.
           """)
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200", description = "조회 성공", content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = CommonException.class),
                    examples = @ExampleObject(value = MainSwaggerEx.LIKED)
            )),
    })
    ResponseEntity<CommonResDto> getUserLiked(@Parameter(hidden = true)  @AuthenticationPrincipal TokenUserInfo userInfo,
                                              @RequestBody @Valid MainLikeReqDto reqDto);

}
