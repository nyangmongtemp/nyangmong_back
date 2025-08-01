package com.playdata.boardservice.board.controller;

import com.playdata.boardservice.board.dto.*;
import com.playdata.boardservice.board.dto.req.BoardSaveReqDto;
import com.playdata.boardservice.board.dto.res.*;
import com.playdata.boardservice.board.entity.Category;
import com.playdata.boardservice.board.service.BoardService;
import com.playdata.boardservice.common.auth.JwtTokenProvider;
import com.playdata.boardservice.common.auth.TokenUserInfo;
import com.playdata.boardservice.common.dto.CommonResDto;
import com.playdata.boardservice.common.enumeration.ErrorCode;
import com.playdata.boardservice.common.exception.CommonException;
import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.List;

@RestController
@RequestMapping("/board")
@RequiredArgsConstructor
@Slf4j
public class BoardController {

    private final BoardService boardService;
    private final JwtTokenProvider jwtTokenProvider;

    /**
     *
     * @param userInfo
     * @param boardSaveReqDto
     * @param thumbnailImage
     * @return
     */
    // 게시물 생성
    @PostMapping(value = "/create", consumes = "multipart/form-data")
    public ResponseEntity<CommonResDto> createBoard(@AuthenticationPrincipal TokenUserInfo userInfo,
                                    @RequestPart("context") @Valid BoardSaveReqDto boardSaveReqDto,
                                    @RequestPart(value = "thumbnailImage") MultipartFile thumbnailImage) {

        // 카테고리 값 검증 및 대문자 변환
        Category categoryEnum = parseCategory(String.valueOf(boardSaveReqDto.getCategory()));

        // boardService로 전달
        CommonResDto resDto
                = boardService.create(boardSaveReqDto, thumbnailImage, userInfo, categoryEnum);

        // 성공 시 응답
       return new ResponseEntity<>(resDto, HttpStatus.OK);
    }

    /**
     *
     * @param category
     * @param postId
     * @param userInfo
     * @param boardModiDto
     * @param thumbnailImage
     * @return
     */
    // 게시물 수정
    @PutMapping(value = "/{category}/modify/{postId}", consumes = "multipart/form-data")
    public ResponseEntity<CommonResDto> modifyBoard(@PathVariable String category,
                                         @PathVariable Long postId,
                                         @AuthenticationPrincipal TokenUserInfo userInfo,
                                         @RequestPart("context") @Valid BoardModiDto boardModiDto,
                                         @RequestPart(value = "thumbnailImage") MultipartFile thumbnailImage) {

        // 대소문자 구분 없이 enum 변환
        Category categoryEnum = parseCategory(category);

        boardService.boardModify(boardModiDto, thumbnailImage, userInfo, categoryEnum, postId);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    /**
     *
     * @param category
     * @param postId
     * @param userInfo
     * @return
     */
    // 게시물 삭제
    @DeleteMapping("/{category}/delete/{postId}")
    public ResponseEntity<CommonResDto> deleteBoard(@PathVariable String category,
                                         @PathVariable Long postId,
                                         @AuthenticationPrincipal TokenUserInfo userInfo) {

        // 대소문자 구분 없이 enum 변환
        Category categoryEnum = parseCategory(category);

        // 서비스에 삭제 요청
        boardService.deleteBoard(userInfo, categoryEnum, postId);

        // 삭제 성공 응답
        return new ResponseEntity<>(HttpStatus.OK);
    }

    /**
     *
     * @param boardSearchDto
     * @param category
     * @param pageable
     * @return
     */
    // 게시판 게시물 목록 조회
    @GetMapping("/list/{category}")
    public ResponseEntity<Page<LikeComResDto>> getBoardList(BoardSearchDto boardSearchDto,
                                                           @PathVariable String category,
                                                           Pageable pageable) {

        // 대소문자 구분 없이 enum 변환
        Category categoryEnum = parseCategory(category);

        Page<LikeComResDto> resDto = boardService.findInformationBoardList(boardSearchDto, categoryEnum, pageable);
        return ResponseEntity.ok().body(resDto);
    }

    /**
     *
     * @param category
     * @param postId
     * @param authHeader
     * @param request
     * @return
     */
    // 게시물 상세 조회
    @GetMapping("/detail/{category}/{id}")
    public ResponseEntity<CommonResDto> getBoardDetail(@PathVariable String category,
                                            @PathVariable(name = "id") Long postId,
                                            @RequestHeader(value = "Authorization", required = false) String authHeader,
                                            HttpServletRequest request) {

        // 대소문자 구분 없이 enum 변환
        Category categoryEnum = parseCategory(category);

        String email = null;
        // Authorization 헤더가 존재하고 Bearer로 시작하는 경우
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7); // "Bearer " 이후의 토큰만 추출
            try {
                // 토큰에서 이메일 추출
                email = jwtTokenProvider.extractEmail(token);
            } catch (Exception e) {
                // JWT 파싱 실패 시 로그 기록 (비로그인 사용자로 처리)
                e.printStackTrace();
            }
        }

        CommonResDto resDto = boardService.boardDetail(categoryEnum, postId, email, request);

        return new  ResponseEntity<>(resDto, HttpStatus.OK);
    }

    /**
     *
     * @return
     */
    // 정보 게시판 메인 최근 게시물 조회
    @GetMapping("/information/main")
    public ResponseEntity<List<BoardListResDto>> findInformationMainList() {
        // 정보 게시판의 게시물 조회
        List<BoardListResDto> resDto = boardService.findInformationMainList();
        return ResponseEntity.ok().body(resDto);
    }

    /**
     *
     * @return
     */
    // 소개 게시판 메인 인기 게시물 조회
    @GetMapping("/main")
    public ResponseEntity<List<IntroductionMainListResDto>> findIntroductionMainList() {
        // 소개 게시판의 게시물 조회
        List<IntroductionMainListResDto> resDto = boardService.findIntroductionMainList();
        return ResponseEntity.ok().body(resDto);
    }

    /**
     *
     * @return
     */
    // 정보 게시판 메인 인기 게시물 조회
    @GetMapping("/popular")
    public ResponseEntity<List<BoardListResDto>> findPopularInformationBoard() {
        // 정보 게시판의 인기 게시물 조회
        List<BoardListResDto> resDto = boardService.findPopularInformationBoard();
        return ResponseEntity.ok().body(resDto);
    }

    /**
     *
     * @param userId
     * @return
     */
    // 회원 탈퇴 시, 회원의 id를 줌 --> 회원의 모든 게시물 삭제 처리 (active = false)
    @Operation(hidden = true)
    @DeleteMapping("/deleteUser/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable("id") Long userId) {

        boardService.deleteUserFindBoard(userId);

        // 요청 완료 응답
        return new ResponseEntity<>(HttpStatus.OK);
    }

    /**
     *
     * @param userId
     * @param nickname
     * @return
     */
    // 회원이 닉네임 변경 시 --> 회원의 모든 게시물의 nickname값 변경
    @Operation(hidden = true)
    @PutMapping("/modifyNickname/{id}/{nickname}")
    ResponseEntity<?> modifyNickname(@PathVariable("id") Long userId,
                                     @PathVariable("nickname") String nickname) {

        String decodedNickname = URLDecoder.decode(nickname, StandardCharsets.UTF_8);

        // 디코딩된
        boardService.modifyUserFindBoard(userId, decodedNickname);
        log.info(userId + ":" + nickname);

        // 요청 완료 응답
        return new ResponseEntity<>(HttpStatus.OK);
    }

    /**
     *
     * @param userInfo
     * @param category
     * @param page
     * @param size
     * @return
     */
    // 마이페이지에서 token을 통한, 내 게시물 조회
    @GetMapping("/mypage/{category}")
    public ResponseEntity<CommonResDto> myPost(@AuthenticationPrincipal TokenUserInfo userInfo,
                                    @PathVariable(name = "category") String category,
                                    @RequestParam(value = "page", defaultValue = "0") int page,
                                    @RequestParam(value = "size", defaultValue = "10") int size) {

        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Order.desc("postId")));
        CommonResDto resDto = boardService.findMyPost(userInfo.getUserId(), parseCategory(category), pageable);

        return new ResponseEntity<>(resDto, HttpStatus.OK);
    }

    /**
     * URL 경로 변수로 들어온 문자열 category를 Category Enum 으로 변환한다.
     * 변환에 실패하면 BAD_REQUEST 예외를 발생시킨다.
     *
     * @param category 문자열 카테고리 (예: "FREE", "INTRODUCTION", "QUESTION", "REVIEW")
     * @return 변환된 Category Enum
     * @throws CommonException 변환 실패 시 발생 (BAD_REQUEST)
     */
    private Category parseCategory(String category) {
        try {
            return Category.valueOf(category.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new CommonException(ErrorCode.BAD_REQUEST);
        }
    }

}
