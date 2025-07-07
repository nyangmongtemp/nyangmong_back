package com.playdata.mainservice.main.repository.custom;

import com.playdata.mainservice.main.dto.MainLikeReqDto;
import com.playdata.mainservice.main.entity.Category;
import com.playdata.mainservice.main.entity.ContentType;
import com.playdata.mainservice.main.entity.Like;
import com.querydsl.core.Tuple;

import java.util.List;
import java.util.Optional;

public interface LikeRepositoryCustom {
    
    // 사용자가 작성한 좋아요를 찾는 메소드
    Optional<Like> findByCategoryAndContentTypeAndContentIdAndUserId(Category category, ContentType contentType, Long contentId, Long userId);

    // 로그인한 사용자가 특정 게시물에 좋아요롤 눌렀는지 확인하는 메소드
    Optional<Like> findUserLiked(Long userId, MainLikeReqDto reqDto);

    // contentType, category, contentId가 모두 일치하고 active가 true인 좋아요 개수 조회
    // 게시물, 댓글, 대댓글의 활성화된 좋아요 개수를 리턴하는 메소드
    Long countByContentTypeAndCategoryAndContentIdAndActiveIsTrue(ContentType contentType, Category category, Long contentId);

    // 마이페이지에서 사용자가 작성한 활성화된 모든 댓글의 개수 조회
    Long countByContentTypeAndContentIdAndActiveTrue (ContentType contentType, Long contentId);

    // 메인화면에서 한달간 좋아요 개수가 가장 많은 소개 게시물 3개의 contentId를 리턴해주는 메소드
    List<Tuple> getPostIdMainIntroductionPost();

}
