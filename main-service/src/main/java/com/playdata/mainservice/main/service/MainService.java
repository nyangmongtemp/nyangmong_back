package com.playdata.mainservice.main.service;

import com.playdata.mainservice.common.dto.CommonResDto;
import com.playdata.mainservice.main.dto.MainComReqDto;
import com.playdata.mainservice.main.dto.MainLikeReqDto;
import com.playdata.mainservice.main.entity.Category;
import com.playdata.mainservice.main.entity.Comment;
import com.playdata.mainservice.main.entity.ContentType;
import com.playdata.mainservice.main.entity.Like;
import com.playdata.mainservice.main.repository.CommentRepository;
import com.playdata.mainservice.main.repository.LikeRepository;
import com.playdata.mainservice.main.repository.ReplyRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

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
    public CommonResDto createComment(MainComReqDto reqDto, Long userId) {

        // 요청 Dto 값의 유효성을 확인
        if(!isValidCategory(reqDto.getCategory())) {
            return new CommonResDto(HttpStatus.BAD_REQUEST, "요청 url이 잘못되었습니다.", null);
        }

        // 유효한 값들이니 ENUM 값으로 변환  --> ENUM 값이 대문자라서 toUpperCase 적용
        Category cate = Category.valueOf(reqDto.getCategory().toUpperCase());

        Comment newComment
                = new Comment(userId, cate, reqDto.getContentId(), reqDto.getContent(), reqDto.isHidden());

        commentRepository.save(newComment);

        return new CommonResDto(HttpStatus.CREATED, "댓글이 생성됨", newComment);
    }

    public CommonResDto deleteComment(Long commentId, Long userId) {

        Optional<Comment> foundComment = commentRepository.findById(commentId);
        // 삭제하려는 댓글이 존재하지 않는 경우
        if(!foundComment.isPresent()) {
            throw new EntityNotFoundException("해당 댓글을 찾을 수 없습니다.");
        }
        // 삭제 요청을 보낸 사용자가 댓글의 작성자가 아닌 경우
        if(!foundComment.get().getUserId().equals(userId)) {
            throw new IllegalArgumentException("해당 댓글의 삭제 권한이 없습니다.");
        }
        Comment deleted = foundComment.get();
        deleted.deleteComment();
        commentRepository.save(deleted);

        return new CommonResDto(HttpStatus.OK, "댓글이 정상적으로 삭제되었습니다.", true);
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
}
