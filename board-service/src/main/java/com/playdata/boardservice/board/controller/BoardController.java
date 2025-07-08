package com.playdata.boardservice.board.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.playdata.boardservice.board.dto.*;
import com.playdata.boardservice.board.entity.Category;
import com.playdata.boardservice.board.service.BoardService;
import com.playdata.boardservice.common.auth.TokenUserInfo;
import com.playdata.boardservice.common.dto.CommonResDto;
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

import java.util.List;

@RestController
@RequestMapping("/board")
@RequiredArgsConstructor
@Slf4j
public class BoardController {

    private final BoardService boardService;

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
    public ResponseEntity<?> getBoardDetail(@PathVariable String category,
                                            @PathVariable(name = "id") Long postId) {
        CommonResDto resDto = boardService.boardDetail(category, postId);

        return new  ResponseEntity<>(resDto, HttpStatus.OK);
    }

    // 정보 게시판 메인 최근 게시물 조회
    @GetMapping("/information/main")
    public ResponseEntity<?> findInformationMainList() {
        List<InformationBoardListResDto> resDto = boardService.findInformationMainList();
        return ResponseEntity.ok().body(resDto);
    }

    // 소개 게시판 메인 최근 게시물 조회
    @GetMapping("/introduction/main")
    public ResponseEntity<?> findIntroductionMainList() {
        List<IntroductionBoardListResDto> resDto = boardService.findIntroductionMainList();
        return ResponseEntity.ok().body(resDto);
    }

}
