package com.playdata.boardservice.board.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.playdata.boardservice.board.dto.*;
import com.playdata.boardservice.board.entity.Category;
import com.playdata.boardservice.board.repository.InformationBoardRepository;
import com.playdata.boardservice.board.service.BoardService;
import com.playdata.boardservice.common.auth.JwtTokenProvider;
import com.playdata.boardservice.common.auth.TokenUserInfo;
import com.playdata.boardservice.common.dto.CommonResDto;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
    private final InformationBoardRepository informationBoardRepository;

    // 정보 게시판 게시물 생성
    @PostMapping(value = "/information/create", consumes = "multipart/form-data")
    public ResponseEntity<?> informationCreate (@AuthenticationPrincipal TokenUserInfo userInfo,
                                                // RequestDto 에 값이 있는지 유효성 검증
                                                @RequestPart("context") @Valid InformationBoardSaveReqDto informationBoardSaveReqDto,
                                                @RequestPart(value = "thumbnailImage") MultipartFile thumbnailImage) {

        // boardService로 전달
        CommonResDto resDto
                = boardService.informationCreate(informationBoardSaveReqDto, thumbnailImage, userInfo);

        // 성공 시 응답
       return new ResponseEntity<>(resDto, HttpStatus.OK);
    }

    // 소개 게시판 게시물 생성
    @PostMapping(value = "introduction/create", consumes = "multipart/form-data")
    public ResponseEntity<?> introductionCreate (@AuthenticationPrincipal TokenUserInfo userInfo,
                                                 // RequestDto 에 값이 있는지 유효성 검증
                                                 @RequestPart("context") @Valid IntroductionBoardSaveReqDto introductionBoardSaveDto,
                                                 @RequestPart(value = "thumbnailImage") MultipartFile thumbnailImage) {

        // boardService로 전달
        CommonResDto resDto = boardService.introductionCreate(introductionBoardSaveDto, thumbnailImage, userInfo);

        // 성공 시 응답
        return new ResponseEntity<>(resDto, HttpStatus.OK);
    }

    // 게시물 수정 (공통)
    @PutMapping("/{category}/modify/{postId}")
    public ResponseEntity<?> modifyBoard(@PathVariable String category,
                                         @PathVariable Long postId,
                                         @AuthenticationPrincipal TokenUserInfo userInfo,
                                         // ModiDto에 값이 있는지 유효성 검증
                                         @RequestPart("context") @Valid BoardModiDto boardModiDto,
                                         @RequestPart(value = "thumbnailImage") MultipartFile thumbnailImage) throws JsonProcessingException {

        // 대소문자 구분 없이 enum 변환
        Category categoryEnum = Category.valueOf(category.toUpperCase());


        boardService.boardModify(boardModiDto, thumbnailImage, userInfo, categoryEnum, postId);

        return new ResponseEntity<>(HttpStatus.OK);
    }


    // 게시물 삭제 (공통)
    @DeleteMapping("/{category}/delete/{postId}")
    public ResponseEntity<?> deleteBoard(@PathVariable String category,
                                         @PathVariable Long postId,
                                         @AuthenticationPrincipal TokenUserInfo userInfo) {

            // 대소문자 구분 없이 enum 변환
            Category categoryEnum = Category.valueOf(category.toUpperCase());

            // 서비스에 삭제 요청
            boardService.deleteBoard(userInfo, categoryEnum, postId);

            // 삭제 성공 응답
            return new ResponseEntity<>(HttpStatus.OK);
    }

    // 정보 게시판 게시물 목록 조회
    @GetMapping("/information/list")
    public ResponseEntity<Page<?>> getInformationBoardList(BoardSearchDto boardSearchDto,
                                                           @RequestParam("category") Category category,
                                                           Pageable pageable) {

        Page<InformationBoardListResDto> resDto = boardService.findInformationBoardList(boardSearchDto, category, pageable);

        return ResponseEntity.ok().body(resDto);
    }

    // 소개 게시판 게시물 목록 조회
    @GetMapping("/introduction/list")
    public ResponseEntity<Page<?>> getIntroductionBoardList(BoardSearchDto boardSearchDto,
                                                            Pageable pageable) {

        Page<IntroductionBoardListResDto> resDto = boardService.findIntroductionBoardList(boardSearchDto, pageable);

        return ResponseEntity.ok().body(resDto);
    }

    // 게시물 상세 조회 (공통)
    @GetMapping("/detail/{category}/{id}")
    public ResponseEntity<?> getBoardDetail(@PathVariable Category category,
                                            @PathVariable(name = "id") Long postId,
                                            @RequestHeader(value = "Authorization", required = false) String authHeader,
                                            HttpServletRequest request) {

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

        CommonResDto resDto = boardService.boardDetail(category, postId, email, request);

        return new  ResponseEntity<>(resDto, HttpStatus.OK);
    }

    // 정보 게시판 메인 최근 게시물 조회
    @GetMapping("/information/main")
    public ResponseEntity<?> findInformationMainList() {
        // 정보 게시판의 게시물 조회
        List<InformationBoardListResDto> resDto = boardService.findInformationMainList();
        return ResponseEntity.ok().body(resDto);
    }

    // 소개 게시판 메인 최근 게시물 조회
    @GetMapping("/introduction/main")
    public ResponseEntity<?> findIntroductionMainList() {
        // 소개 게시판의 게시물 조회
        List<IntroductionBoardListResDto> resDto = boardService.findIntroductionMainList();
        return ResponseEntity.ok().body(resDto);
    }

    // 정보 게시판 메인 인기 게시물 조회
    @GetMapping("/information/popular")
    public ResponseEntity<?> findPopularInformationBoard() {
        // 정보 게시판의 인기 게시물 조회
        List<InformationBoardListResDto> resDto = boardService.findPopularInformationBoard();
        return ResponseEntity.ok().body(resDto);
    }

    // 회원 탈퇴 시, 회원의 id를 줌 --> 회원의 모든 게시물 삭제 처리 (active = false)
    @DeleteMapping("/deleteUser/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable("id") Long userId) {

        boardService.deleteUserFindBoard(userId);

        // 요청 완료 응답
        return new ResponseEntity<>(HttpStatus.OK);
    }

    // 회원이 닉네임 변경 시 --> 회원의 모든 게시물의 nickname값 변경
    @PutMapping("/modifyNickname/{id}/{nickname}")
    ResponseEntity<?> modifyNickname(@PathVariable("id") Long userId,
                                     @PathVariable("nickname") String encodedNickname) {

        String nickname = URLDecoder.decode(encodedNickname, StandardCharsets.UTF_8);

        boardService.modifyUserFindBoard(userId, nickname);
        log.info(userId + ":" + nickname);

        // 요청 완료 응답
        return new ResponseEntity<>(HttpStatus.OK);
    }

}
