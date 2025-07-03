package com.playdata.mainservice.main.service;

import com.playdata.mainservice.common.auth.TokenUserInfo;
import com.playdata.mainservice.common.dto.CommonResDto;
import com.playdata.mainservice.main.dto.*;
import com.playdata.mainservice.main.entity.*;
import com.playdata.mainservice.main.repository.CommentRepository;
import com.playdata.mainservice.main.repository.LikeRepository;
import com.playdata.mainservice.main.repository.ReplyRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class MainService {

    private final LikeRepository likeRepository;

    private final CommentRepository commentRepository;

    private final ReplyRepository replyRepository;

    // 들어온 url의 값의 유효성을 확인용
    private List<String> categoryList = List.of("free", "adopt", "children", "review", "question");
    private List<String> typeList = List.of("post", "comment", "reply");

    // 좋아요를 통합적으로 생성하는 서비스 메소드
    public CommonResDto createLike(Long userId, MainLikeReqDto reqDto) {
        
        // 요청 Dto 값의 유효성을 확인
        if(!isValidCategory(reqDto.getCategory()) || !isValidContentType(reqDto.getContentType())) {
            return new CommonResDto(HttpStatus.BAD_REQUEST, "요청 url이 잘못되었습니다.", null);
        }
        
        // 유효한 값들이니 ENUM 값으로 변환  --> ENUM 값이 대문자라서 toUpperCase 적용
        Category cate = Category.valueOf(reqDto.getCategory().toUpperCase());
        ContentType ct = ContentType.valueOf(reqDto.getContentType().toUpperCase());
        
        // DB에 기존에 생성된 좋아요가 있는지 조회
        Optional<Like> foundLike
                = likeRepository.findByCategoryAndContentTypeAndContentIdAndUserId(cate, ct, reqDto.getContentId(), userId);

        // 기존에 좋아요를 눌렀던 이력이 있는 경우
        if (foundLike.isPresent()) {
            Like like = foundLike.get();
            // 기존에 좋아요의 active 값을 바꿈. --> 취소와 다시 좋아요를 한번에
            like.changeActive();
            likeRepository.save(like);
            return new CommonResDto(HttpStatus.OK, "좋아요 값이 변경됨",
                    // 새로 바뀐 좋아요 active 값을 리턴
                    like.isActive());
        }

        // 좋아요를 눌렀던 이력이 없는 경우
        Like like = new Like(userId, reqDto.getContentId(), ct, cate);
        likeRepository.save(like);
        return new CommonResDto(HttpStatus.CREATED, "좋아요가 생성됨",
                // 새로 생성됐으니 active는 무조건 true
                true);
    }

    // 댓글 생성 메소드
    public CommonResDto createComment(MainComReqDto reqDto, Long userId, String nickname) {

        // 요청 Dto 값의 유효성을 확인
        if(!isValidCategory(reqDto.getCategory())) {
            return new CommonResDto(HttpStatus.BAD_REQUEST, "요청 url이 잘못되었습니다.", null);
        }

        // 유효한 값들이니 ENUM 값으로 변환  --> ENUM 값이 대문자라서 toUpperCase 적용
        Category cate = Category.valueOf(reqDto.getCategory().toUpperCase());

        // 새로운 댓글 생성
        Comment newComment
                = new Comment(userId, cate, reqDto.getContentId(), reqDto.getContent(), reqDto.isHidden(), nickname);
        // DB에 저장
        commentRepository.save(newComment);

        return new CommonResDto(HttpStatus.CREATED, "댓글이 생성됨",
                // Dto 변환 해서 화면단으로 리턴
                newComment.fromEntity());
    }
    
    // 댓글 삭제 메소드
    public CommonResDto deleteComment(Long commentId, Long userId) {

        Comment foundComment = isValidComment(commentId, userId);
        foundComment.deleteComment();
        commentRepository.save(foundComment);

        return new CommonResDto(HttpStatus.OK, "댓글이 정상적으로 삭제되었습니다.", true);
    }

    // 댓글 수정 메소드
    public CommonResDto modifyComment(Long userId, ComModiReqDto reqDto) {

        Comment comment = isValidComment(reqDto.getCommentId(), userId);
        comment.mofifyComment(reqDto.getContent());
        ComSaveResDto dto = commentRepository.save(comment).fromEntity();

        return new CommonResDto(HttpStatus.OK, "댓글 내용이 수정되었습니다.", dto);
    }

    public CommonResDto createReply(TokenUserInfo userInfo, ReplySaveReqDto reqDto) {

        Comment foundComment = isPresentComment(reqDto);

        Reply createdReply = new Reply(userInfo.getUserId(), reqDto.getContent(), foundComment, userInfo.getNickname());

        ReplySaveResDto resDto = replyRepository.save(createdReply).fromEntity();

        return new CommonResDto(HttpStatus.CREATED, "대댓글이 생성되었습니다.", resDto);
    }

    public CommonResDto deleteReply(Long userId, Long replyId) {

        Reply validReply = isValidReply(userId, replyId);
        validReply.deleteReply();
        replyRepository.save(validReply);

        return new CommonResDto(HttpStatus.OK, "대댓글이 삭제되었습니다.", true);
    }

    public CommonResDto modifyReply(TokenUserInfo userInfo, ReplyModiReqDto reqDto) {

        // 대댓글 존재 여부 및 활성화 여부, 수정, 삭제 권한 여부 확인
        Reply validReply = isValidReply(userInfo.getUserId(), reqDto.getReplyId());

        // 대댓글에 매핑된 댓글의 존재 및 활성화 여부 확인
        ReplySaveReqDto saveReqDto
                = new ReplySaveReqDto(userInfo.getUserId(), validReply.getContent());
        isPresentComment(saveReqDto);

        // 대댓글의 댓글이 존재하고 수정 권한도 있는 경우
        // 수정 진행
        validReply.modifyReply(reqDto.getContent());
        replyRepository.save(validReply);

        return new CommonResDto(HttpStatus.CREATED, "대댓글 수정이 완료되었습니다.", validReply.fromEntity());
    }
    
    // 탈퇴한 회원의 모든 좋아요, 댓글, 대댓글의 active 값을 false 처리 하는 로직
    public CommonResDto deleteUserAll(Long userId) {

        // 좋아요
        Optional<List<Like>> foundLike = likeRepository.findByUserId(userId);
        // 사용자가 만든 좋아요가 있는 경우에만 active false 작업 수행
        if(foundLike.isPresent()) {
            List<Like> likes = foundLike.get();
            likes.forEach(Like::deleteLike); // 상태만 먼저 수정
            likeRepository.saveAll(likes);   // 일괄 저장
        }
        // 댓글
        Optional<List<Comment>> foundComment = commentRepository.findByUserId(userId);
        // 사용자가 작성한 댓글이 있는 경우에만 active false 작업 수행
        if(foundComment.isPresent()) {
            List<Comment> comments = foundComment.get();
            comments.forEach(Comment::deleteComment);
            commentRepository.saveAll(comments);
        }

        // 대댓글  --> 필요 없을 수도 있지만, 미연의 사태를 방지하기 위해
        // 댓글 삭제 시, 매핑된 모든 대댓글을 active false하는 로직이 있지만
        // 혹시 모르는 경우를 대비해 대댓글도 모드 active false 처리 진행
        Optional<List<Reply>> foundReply = replyRepository.findByUserId(userId);
        // 사용자가 작성한 대댓글이 있는 경우에만 active false 작업 수행
        if(foundReply.isPresent()) {
            List<Reply> replies = foundReply.get();
            replies.forEach(Reply::deleteReply);
            replyRepository.saveAll(replies);
        }

        return new CommonResDto(HttpStatus.OK, "회원의 모든 댓글, 대댓글, 좋아요를 삭제하였습니다.", true);
    }

    // 회원의 닉네임 변경 시, 저장된 모든 댓글, 대댓글의 닉네임 값 변경
    public CommonResDto changeUserNickname(Long userId, String encodedNickname) {

        String nickname = URLDecoder.decode(encodedNickname, StandardCharsets.UTF_8);
        // 사용자가 작성한 모든 댓글 조회
        Optional<List<Comment>> foundComment = commentRepository.findByUserId(userId);
        // 사용자가 작성한 댓글이 있는 경우에만 닉네임 변경 작업 수행
        if(foundComment.isPresent()) {
            List<Comment> comments = foundComment.get();
            comments.stream().filter(Comment::isActive).forEach(comment -> {
                comment.modifyNickname(nickname);
            });
            commentRepository.saveAll(comments);
        }

        // 사용자가 작성한 모든 대댓글 조회
        Optional<List<Reply>> foundReply = replyRepository.findByUserId(userId);
        // 사용자가 작성한 대댓글이 있는 경우에만, 닉네임 변경 작업 수행
        if(foundReply.isPresent()) {
            List<Reply> replies = foundReply.get();
            replies.stream().filter(Reply::isActive).forEach(reply -> {
                reply.modifyNickname(nickname);
            });
            replyRepository.saveAll(replies);
        }

        return new CommonResDto(HttpStatus.OK, "사용자의 모든 댓글, 대댓글의 닉네임이 변경되었습니다.", true);
    }

    // 들어온 요청의 url값의 유효성을 확인하는 메소드
    // 컨텐츠타입의 유효성 확인
    private boolean isValidContentType(String contentType) {
        return typeList.contains(contentType);
    }

    // 카테고리의 유효성 확인
    private boolean isValidCategory(String category) {
        return categoryList.contains(category);
    }

    // 댓글이 존재하고 삭제되지 않았는 지 판별해서 리턴해주는 메소드
    private Comment isValidComment(Long commentId, Long userId) {
        Optional<Comment> foundComment = commentRepository.findById(commentId);
        // 삭제하려는 댓글이 존재하지 않는 경우
        if(!foundComment.isPresent()) {
            throw new EntityNotFoundException("해당 댓글을 찾을 수 없습니다.");
        }
        // 삭제 요청을 보낸 사용자가 댓글의 작성자가 아닌 경우
        if(!foundComment.get().getUserId().equals(userId)) {
            throw new IllegalArgumentException("해당 댓글의 삭제 및 수정 권한이 없습니다.");
        }
        return foundComment.get();
    }

    // 대댓글을 작성할 댓글이 유효한지 판별해주는 메소드
    private Comment isPresentComment(ReplySaveReqDto reqDto) {
        Optional<Comment> foundComment = commentRepository.findById(reqDto.getCommentId());
        // 대댓글을 작성하려는 댓글이 존재하지 않거나 삭제된 경우
        if(!foundComment.isPresent() || !foundComment.get().isActive()) {
            throw new EntityNotFoundException("대댓글을 작성할 댓글이 존재하지 않습니다.");
        }
        return foundComment.get();
    }

    // 대댓글을 수정 및 삭제할 권한이 있는지 판별해주는 메소드

    private Reply isValidReply(Long userId, Long replyId) {
        Optional<Reply> foundReply = replyRepository.findById(replyId);
        // 삭제할 대댓글이 존재하지 않는 경우
        if(!foundReply.isPresent() || !foundReply.get().isActive()) {
            throw new EntityNotFoundException("수정, 삭제할 대댓글이 존재하지 않습니다.");
        }
        // 삭제를 요청한 사용자가 대댓글 작성자가 아닌 경우
        if(!foundReply.get().getUserId().equals(userId)) {
            throw new IllegalArgumentException("해당 대댓글의 수정 및 삭제 권한이 없습니다.");
        }
        return foundReply.get();
    }
}
