package com.playdata.boardservice.board.controller;


import com.playdata.boardservice.board.dto.BoardModiDto;
import com.playdata.boardservice.board.dto.BoardSearchDto;
import com.playdata.boardservice.board.dto.req.BoardSaveReqDto;
import com.playdata.boardservice.board.dto.res.BoardListResDto;
import com.playdata.boardservice.board.dto.res.IntroductionMainListResDto;
import com.playdata.boardservice.board.dto.res.LikeComResDto;
import com.playdata.boardservice.common.auth.TokenUserInfo;
import com.playdata.boardservice.common.dto.CommonResDto;
import com.playdata.boardservice.common.enumeration.BoardSwaggerEx;
import com.playdata.boardservice.common.exception.CommonException;
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
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Tag(name = "게시판 서비스", description = "소개, 후기, 자유, 질문 게시판 관련 API입니다." +
        "category는 introduction, free, question, review 입니다.")
public interface BoardControllerDocs {

    @Operation(summary = "게시물 생성",
            description = """
                게시물을 생성합니다.
          
                ## 인증
               - 로그인 해야 생성 가능합니다.
           """)
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201", description = "생성 성공", content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = CommonException.class),
                    examples = @ExampleObject(value = BoardSwaggerEx.BOARD_CREATE)
            )),
            @ApiResponse(
                    responseCode = "500", description = "이미지 필수", content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = CommonException.class),
                    examples = @ExampleObject(value = BoardSwaggerEx.EMPTY_FILE)
            )),
            @ApiResponse(
                    responseCode = "500", description = "카테고리값 오류", content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = CommonException.class),
                    examples = @ExampleObject(value = BoardSwaggerEx.BAD_REQUEST)
            )),
            @ApiResponse(
                    responseCode = "500", description = "이미지 오류", content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = CommonException.class),
                    examples = @ExampleObject(value = BoardSwaggerEx.FILE_INVALID_ERROR)
            ))
    })
    ResponseEntity<CommonResDto> createBoard(@Parameter(hidden = true) @AuthenticationPrincipal TokenUserInfo userInfo,
                                             @RequestPart("context") @Valid BoardSaveReqDto boardSaveReqDto,
                                             @Parameter(
                                                     name = "thumbnailImage",
                                                     description = "썸네일 이미지 파일",
                                                     content = {
                                                             @Content(mediaType = "image/jpeg"),
                                                             @Content(mediaType = "image/png"),
                                                             @Content(mediaType = "image/gif"),
                                                             @Content(mediaType = "image/bmp"),
                                                             @Content(mediaType = "image/webp")
                                                     }
                                             )
                                             @RequestPart(value = "thumbnailImage", required = false) MultipartFile thumbnailImage);



    @Operation(summary = "게시물 수정",
            description = """
                게시물을 수정합니다.
          
                ## 인증
               - 로그인 해야 수정 가능합니다.
               - 본인이 작성한 게시물만 수정 가능합니다.
           """)
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200", description = "수정 성공", content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = CommonException.class),
                    examples = @ExampleObject(value = BoardSwaggerEx.BOARD_UPDATE)
            )),
            @ApiResponse(
                    responseCode = "500", description = "이미지 필수", content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = CommonException.class),
                    examples = @ExampleObject(value = BoardSwaggerEx.EMPTY_FILE)
            )),
            @ApiResponse(
                    responseCode = "500", description = "카테고리값 오류", content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = CommonException.class),
                    examples = @ExampleObject(value = BoardSwaggerEx.BAD_REQUEST)
            )),
            @ApiResponse(
                    responseCode = "500", description = "이미지 오류", content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = CommonException.class),
                    examples = @ExampleObject(value = BoardSwaggerEx.FILE_INVALID_ERROR)
            )),
            @ApiResponse(
                    responseCode = "401", description = "권한 없음", content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = CommonException.class),
                    examples = @ExampleObject(value = BoardSwaggerEx.UNAUTHORIZED)
            )),
            @ApiResponse(
                    responseCode = "404", description = "게시물 없음", content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = CommonException.class),
                    examples = @ExampleObject(value = BoardSwaggerEx.DATA_NOT_FOUND)
            )),
    })
    ResponseEntity<CommonResDto> modifyBoard(@PathVariable String category,
                                             @PathVariable Long postId,
                                             @Parameter(hidden = true) @AuthenticationPrincipal TokenUserInfo userInfo,
                                             @RequestPart("context") @Valid BoardModiDto boardModiDto,
                                             @Parameter(
                                                     name = "thumbnailImage",
                                                     description = "썸네일 이미지 파일",
                                                     content = {
                                                             @Content(mediaType = "image/jpeg"),
                                                             @Content(mediaType = "image/png"),
                                                             @Content(mediaType = "image/gif"),
                                                             @Content(mediaType = "image/bmp"),
                                                             @Content(mediaType = "image/webp")
                                                     }
                                             )
                                             @RequestPart(value = "thumbnailImage", required = false) MultipartFile thumbnailImage);


    @Operation(summary = "게시물 삭제",
            description = """
                게시물을 삭제합니다.
          
                ## 인증
               - 로그인 해야 삭제 가능합니다.
               - 본인이 작성한 게시물만 삭제 가능합니다.
           """)
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200", description = "삭제 성공", content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = CommonException.class),
                    examples = @ExampleObject(value = BoardSwaggerEx.BOARD_DELETE)
            )),
            @ApiResponse(
                    responseCode = "500", description = "카테고리값 오류", content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = CommonException.class),
                    examples = @ExampleObject(value = BoardSwaggerEx.BAD_REQUEST)
            )),
            @ApiResponse(
                    responseCode = "401", description = "권한 없음", content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = CommonException.class),
                    examples = @ExampleObject(value = BoardSwaggerEx.UNAUTHORIZED)
            )),
            @ApiResponse(
                    responseCode = "404", description = "게시물 없음", content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = CommonException.class),
                    examples = @ExampleObject(value = BoardSwaggerEx.DATA_NOT_FOUND)
            ))
    })
    ResponseEntity<CommonResDto> deleteBoard(@PathVariable String category,
                                             @PathVariable Long postId,
                                             @Parameter(hidden = true) @AuthenticationPrincipal TokenUserInfo userInfo);


    @Operation(summary = "게시물 목록 조회",
            description = """
                각 게시판의 게시물 목록을 페이징 조회합니다.
          
           """)
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200", description = "조회 성공", content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = CommonException.class),
                    examples = @ExampleObject(value = BoardSwaggerEx.BOARD_LIST)
            )),
            @ApiResponse(
                    responseCode = "500", description = "카테고리값 오류", content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = CommonException.class),
                    examples = @ExampleObject(value = BoardSwaggerEx.BAD_REQUEST)
            ))
    })
    ResponseEntity<Page<LikeComResDto>> getBoardList(BoardSearchDto boardSearchDto,
                                                     @PathVariable String category,
                                                     Pageable pageable);


    @Operation(summary = "게시물 상세 조회",
            description = """
                각 게시판의 게시물의 상세정보를 조회합니다.
          
           """)
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200", description = "조회 성공", content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = CommonException.class),
                    examples = @ExampleObject(value = BoardSwaggerEx.BOARD_DETAIL)
            )),
            @ApiResponse(
                    responseCode = "500", description = "카테고리값 오류", content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = CommonException.class),
                    examples = @ExampleObject(value = BoardSwaggerEx.BAD_REQUEST)
            )),
            @ApiResponse(
                    responseCode = "404", description = "게시물 없음", content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = CommonException.class),
                    examples = @ExampleObject(value = BoardSwaggerEx.DATA_NOT_FOUND)
            ))
    })
    ResponseEntity<CommonResDto> getBoardDetail(@PathVariable String category,
                                                @PathVariable(name = "id") Long postId,
                                                @Parameter(
                                                        description = "조회수 증가 검증용"
                                                )
                                                @RequestHeader(value = "Authorization", required = false) String authHeader,
                                                HttpServletRequest request);


    @Operation(summary = "최근 게시물 조회",
            description = """
                메인화면에 노출될 최근 게시물들을 조회합니다.
           
           """)
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200", description = "조회 성공", content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = CommonException.class),
                    examples = @ExampleObject(value = BoardSwaggerEx.RECENT_POST_LIST)
            ))
    })
    ResponseEntity<List<BoardListResDto>> findInformationMainList();


    @Operation(summary = "인기 소개 게시물 조회",
            description = """
                메인화면에 노출될 인기 소개 게시물들을 조회합니다.
          
           """)
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200", description = "조회 성공", content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = CommonException.class),
                    examples = @ExampleObject(value = BoardSwaggerEx.POPULAR_INTRO)
            ))
    })
    ResponseEntity<List<IntroductionMainListResDto>> findIntroductionMainList();


    @Operation(summary = "인기 게시물 조회",
            description = """
                메인화면에 노출될 인기 게시물들을 조회합니다.
          
           """)
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200", description = "조회 성공", content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = CommonException.class),
                    examples = @ExampleObject(value = BoardSwaggerEx.POPULAR)
            ))
    })
    ResponseEntity<List<BoardListResDto>> findPopularInformationBoard();


    @Operation(summary = "마이페이지 게시물 조회",
            description = """
                마이페이지에 본인이 작성한 게시물들을 카테고리별로 페이징 조회합니다.
          
                ## 인증
               - 로그인 해야 조회 가능합니다.
               - 본인이 작성한 게시물만 조회 가능합니다.
           
           """)
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200", description = "조회 성공", content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = CommonException.class),
                    examples = @ExampleObject(value = BoardSwaggerEx.MINE)
            )),
            @ApiResponse(
                    responseCode = "500", description = "카테고리값 오류", content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = CommonException.class),
                    examples = @ExampleObject(value = BoardSwaggerEx.BAD_REQUEST)
            ))
    })
    ResponseEntity<CommonResDto> myPost(@Parameter(hidden = true) @AuthenticationPrincipal TokenUserInfo userInfo,
                                        @PathVariable(name = "category") String category,
                                        @RequestParam(value = "page", defaultValue = "0") int page,
                                        @RequestParam(value = "size", defaultValue = "10") int size);

}
