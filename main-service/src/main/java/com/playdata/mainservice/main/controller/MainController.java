package com.playdata.mainservice.main.controller;

import com.playdata.mainservice.common.auth.TokenUserInfo;
import com.playdata.mainservice.common.dto.CommonResDto;
import com.playdata.mainservice.main.dto.ComModiReqDto;
import com.playdata.mainservice.main.dto.MainComReqDto;
import com.playdata.mainservice.main.dto.MainLikeReqDto;
import com.playdata.mainservice.main.dto.ReplySaveReqDto;
import com.playdata.mainservice.main.service.MainService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/main")
@RequiredArgsConstructor
public class MainController {

    private final MainService mainService;

    // 게시물 좋아요 --> 좋아요 생성, 취소 모두 이 메소드로 통일함.
    @PostMapping("/like")
    public ResponseEntity<?> createLike(@AuthenticationPrincipal TokenUserInfo userInfo
            ,@RequestBody MainLikeReqDto reqDto) {

        CommonResDto likePost = mainService.createLike(userInfo.getUserId(), reqDto);
        
        return new ResponseEntity<>(likePost,
                // 요청이 잘못된 경우에는 에러 코드가 나오게끔 하기 위한 코드
                HttpStatusCode.valueOf(likePost.getStatusCode()));
    }

    // 댓글 생성
    @PostMapping("/comment/create")
    public ResponseEntity<?> createComment(@AuthenticationPrincipal TokenUserInfo userInfo,
                                           @RequestBody MainComReqDto reqDto){
        CommonResDto resDto = mainService.createComment(reqDto, userInfo.getUserId(), userInfo.getNickname());

        return new ResponseEntity(resDto, HttpStatus.CREATED);
    }
    
    // 댓글 삭제
    @GetMapping("/comment/delete/{id}")
    public ResponseEntity<?> deleteComment(@AuthenticationPrincipal TokenUserInfo userInfo,
                                           @PathVariable(name = "id") Long commentId) {
        CommonResDto resDto = mainService.deleteComment(commentId, userInfo.getUserId());

        return new ResponseEntity<>(resDto, HttpStatus.OK);
    }

    // 댓글 수정
    @PostMapping("/comment/modify")
    public ResponseEntity<?> modifyComment(@AuthenticationPrincipal TokenUserInfo userInfo,
                                           @RequestBody ComModiReqDto reqDto){
        CommonResDto resDto
                = mainService.modifyComment(userInfo.getUserId(), reqDto, userInfo.getNickname());

        return new ResponseEntity<>(resDto, HttpStatus.OK);
    }

    // 대댓글 생성
    @PostMapping("/reply/create")
    public ResponseEntity<?> createReply(@AuthenticationPrincipal TokenUserInfo userInfo,
                                         @RequestBody ReplySaveReqDto reqDto) {
        CommonResDto resDto = mainService.createReply(userInfo.getUserId(), reqDto);

        return new ResponseEntity<>(resDto, HttpStatus.CREATED);
    }

    // 대댓글 삭제
    @GetMapping("/reply/delete/{id}")
    public ResponseEntity<?> deleteReply(@AuthenticationPrincipal TokenUserInfo userInfo
            ,@PathVariable(name = "id") Long replyId) {
        CommonResDto resDto = mainService.deleteReply(userInfo.getUserId(), replyId);

        return new ResponseEntity<>(resDto, HttpStatus.OK);
    }

    // 대댓글 수정

}
