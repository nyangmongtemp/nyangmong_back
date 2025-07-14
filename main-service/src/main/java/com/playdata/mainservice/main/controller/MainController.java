package com.playdata.mainservice.main.controller;

import com.playdata.mainservice.common.auth.TokenUserInfo;
import com.playdata.mainservice.common.dto.CommonResDto;
import com.playdata.mainservice.main.dto.req.*;
import com.playdata.mainservice.main.dto.res.LikeComCountResDto;
import com.playdata.mainservice.main.service.MainService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/main")
@RequiredArgsConstructor
public class MainController {

    private final MainService mainService;

    // 게시물 좋아요 --> 좋아요 생성, 취소 모두 이 메소드로 통일함.
    /**
     *
     * @param userInfo
     * @param reqDto  --> contentId, contentType, category
     * @return
     */
    @PostMapping("/like")
    public ResponseEntity<?> createLike(@AuthenticationPrincipal TokenUserInfo userInfo
            ,@RequestBody @Valid MainLikeReqDto reqDto) {

        CommonResDto likePost = mainService.createLike(userInfo.getUserId(), reqDto);
        
        return new ResponseEntity<>(likePost,
                // 요청이 잘못된 경우에는 에러 코드가 나오게끔 하기 위한 코드
                HttpStatusCode.valueOf(likePost.getStatusCode()));
    }

    /**
     *
     * @param userInfo
     * @param reqDto --> categoty, hidden, contentType, contentId
     * @return
     */
    // 댓글 생성
    @PostMapping("/comment/create")
    public ResponseEntity<?> createComment(@AuthenticationPrincipal TokenUserInfo userInfo,
                                           @RequestBody @Valid MainComReqDto reqDto){
        CommonResDto resDto = mainService.createComment(reqDto, userInfo.getUserId(), userInfo.getNickname());

        return new ResponseEntity(resDto, HttpStatus.CREATED);
    }
    
    // 댓글 삭제
    /**
     * asdf
     * @param userInfo
     * @param commentId
     * @return
     */
    @DeleteMapping("/comment/delete/{id}")
    public ResponseEntity<?> deleteComment(@AuthenticationPrincipal TokenUserInfo userInfo,
                                           @PathVariable(name = "id") Long commentId) {
        CommonResDto resDto = mainService.deleteComment(commentId, userInfo.getUserId());

        return new ResponseEntity<>(resDto, HttpStatus.OK);
    }

    /**
     *
     * @param userInfo
     * @param reqDto  --> content, commentId
     * @return
     */
    // 댓글 수정
    @PatchMapping("/comment/modify")
    public ResponseEntity<?> modifyComment(@AuthenticationPrincipal TokenUserInfo userInfo,
                                           @RequestBody @Valid ComModiReqDto reqDto){
        CommonResDto resDto
                = mainService.modifyComment(userInfo.getUserId(), reqDto);

        return new ResponseEntity<>(resDto, HttpStatus.OK);
    }

    /**
     *
     * @param userInfo
     * @param reqDto  --> content, commentId
     * @return
     */
    // 대댓글 생성
    @PostMapping("/reply/create")
    public ResponseEntity<?> createReply(@AuthenticationPrincipal TokenUserInfo userInfo,
                                         @RequestBody @Valid ReplySaveReqDto reqDto) {
        CommonResDto resDto = mainService.createReply(userInfo, reqDto);

        return new ResponseEntity<>(resDto, HttpStatus.CREATED);
    }

    /**
     *
     * @param userInfo
     * @param replyId
     * @return
     */
    // 대댓글 삭제
    @DeleteMapping("/reply/delete/{id}")
    public ResponseEntity<?> deleteReply(@AuthenticationPrincipal TokenUserInfo userInfo
            ,@PathVariable(name = "id") Long replyId) {
        CommonResDto resDto = mainService.deleteReply(userInfo.getUserId(), replyId);

        return new ResponseEntity<>(resDto, HttpStatus.OK);
    }

    /**
     *
     * @param userInfo
     * @param reqDto  --> content, commentId
     * @return
     */
    // 대댓글 수정
    @PatchMapping("/reply/modify")
    public ResponseEntity<?> modifyReply(@AuthenticationPrincipal TokenUserInfo userInfo,
                                         @RequestBody @Valid ReplyModiReqDto reqDto){
        CommonResDto resDto = mainService.modifyReply(userInfo, reqDto);

        return new ResponseEntity<>(resDto, HttpStatus.OK);
    }

    /**
     *
     * @param reqDto  ->> contentId, category
     * @return
     */
    // 게시물 상세 조회 시 모든 좋아요, 댓글 개수 리턴
    @PostMapping("/detail")
    public ResponseEntity<?> getDetailLikeCommentCount(@RequestBody LikeComCountReqDto reqDto) {
        CommonResDto resDto = mainService.getDetail(reqDto);

        return new ResponseEntity<>(resDto, HttpStatus.OK);
    }

    /**
     *
     * @param reqDto  --> contentId, category
     * @param pageable  --> ?page=2&size=10&sort=createTime
     * @return
     */
    // 게시물 상세 조회 시 모든 댓글 리턴 --> 페이징 처리 필요
    @PostMapping("/comment/list")
    public ResponseEntity<?> getCommentList(@RequestBody LikeComCountReqDto reqDto, Pageable pageable) {
        CommonResDto resDto = mainService.getCommentDetail(reqDto, pageable);

        return new ResponseEntity<>(resDto, HttpStatus.OK);
    }

    @GetMapping("/reply/list/{id}")
    public ResponseEntity<?> getReplyList(@PathVariable(name = "id") Long commentId) {
        CommonResDto resDto = mainService.getCommentReplies(commentId);

        return new ResponseEntity<>(resDto, HttpStatus.OK);
    }

    /**
     *
     * @param userInfo
     * @param pageable  -->  ?page=2&size=10&sort=createTime
     * @return
     */
    // 마이페이지에서 내가 쓴 댓글 목록 조회
    @GetMapping("/comment/mypage")
    public ResponseEntity<?> getMyComment(@AuthenticationPrincipal TokenUserInfo userInfo, Pageable pageable) {
        CommonResDto resDto = mainService.getMyComment(userInfo.getUserId(), pageable);

        return new ResponseEntity<>(resDto, HttpStatus.OK);
    }

    /**
     *
     * @param userInfo
     * @param pageable  --> ?page=2&size=10&sort=createTime
     * @return
     */
    // 마이페이지에서 내가 쓴 대댓글의 댓글 목록 조회
    @GetMapping("/reply/mypage")
    public ResponseEntity<?> getMyReply(@AuthenticationPrincipal TokenUserInfo userInfo, Pageable pageable) {
        CommonResDto resDto = mainService.getMyReply(userInfo.getUserId(), pageable);

        return new ResponseEntity<>(resDto, HttpStatus.OK);
    }

    /**
     *
     * @param userInfo
     * @param reqDto  --> commentId, userId (작성자 id)
     * @return
     */
    // 화면단에서 commentId 와 게시물 작성자 userId를 줘야함.
    @PostMapping("/comment/hidden")
    public ResponseEntity<?> getCommentHidden(@AuthenticationPrincipal TokenUserInfo userInfo,
                                              @RequestBody @Valid SeeHideComReqDto reqDto) {
        boolean canSee = mainService.canSeeHideComment(userInfo.getUserId(), reqDto);

        return new ResponseEntity<>(canSee, HttpStatus.OK);
    }

    /**
     * 메인 화면에서 소개 게시물의 인기 게시물 3개를 리턴해주는 메소드 입니다
     *
     * @return
     */
    @GetMapping("/introduction")
    public List<LikeComCountResDto> getMainIntroduction() {
        return mainService.getMainIntroduction();
    }

    /**  게시물 상세에서 로그인한 사용자의 좋아요 클릭 여부를 확인시켜주는 메소드
     *
     * @param userInfo
     * @param reqDto  --> category, contentId, contentType
     * @return
     */

    @PostMapping("/liked")
    public ResponseEntity<?> getUserLiked(@AuthenticationPrincipal TokenUserInfo userInfo,
                                          @RequestBody @Valid MainLikeReqDto reqDto) {
        CommonResDto resDto = mainService.getUserLiked(userInfo.getUserId(), reqDto);

        return new ResponseEntity<>(resDto, HttpStatus.OK);
    }


////// feign 요청를 필요로 하는 메소드들입니다. --> 게시판 서비스가 완성이 된다면 그때 작성하도록 하겠습니다.

    /**
     *
     * @param userId
     * @param profileImage
     * @return
     */
    // 회원의 프로필 사진이 변경되었을 때, 해당 사용자가 작성한 모든 댓글, 대댓글의 profileImage 값을 변경하는 메소드
    @PutMapping("/modifyProfileImage/{id}/{profileImage}")
        ResponseEntity<?> modifyProfileImage(@PathVariable("id") Long userId,
                                     @PathVariable("profileImage") String profileImage) {
        CommonResDto resDto = mainService.changeUserProfile(userId, profileImage);

        return new ResponseEntity<>(resDto, HttpStatus.OK);
    }

//////// feign 요청을 받는 메소드들입니다.

    /**
     *
     * @param userId
     * @return
     */
    // 회원이 탈퇴했을 때, 회원이 작성한 좋아요, 댓글, 대댓글을 모두 active false로 변경하는 메소드
    @DeleteMapping("/deleteUser/{id}")
    ResponseEntity<?> deleteUser(@PathVariable("id") Long userId) {
        CommonResDto resDto = mainService.deleteUserAll(userId);

        return new ResponseEntity<>(resDto, HttpStatus.OK);
    }

    /**
     *
     * @param userId
     * @param nickname
     * @return
     */
    // 회원의 닉네임이 변경되었을 때, 해당 사용자가 작성한 모든 댓글, 대댓글의 nickname값을 변경하는 메소드
    @PutMapping("/modifyNickname/{id}/{nickname}")
    ResponseEntity<?> modifyNickname(@PathVariable("id") Long userId, @PathVariable("nickname") String nickname) {
        CommonResDto resDto = mainService.changeUserNickname(userId, nickname);

        return new ResponseEntity<>(resDto, HttpStatus.OK);
    }

    /**
     * @param contentList --> List<contentId, category>
     * @return  List<LikeComCountResDto>  --> category, contentId, commentCount(대댓글까지 포함), likeCount
     */
    // feign으로 받으셔야 합니다.
    // 게시물 좋아요, 댓글 개수 조회 -> 리스트 형태로 올 경우
    @PostMapping("/list")
    public List<LikeComCountResDto> getListLikeCommentCount(@RequestBody List<LikeComCountReqDto> contentList) {
        return mainService.getLikeCommentCount(contentList);
    }

}
