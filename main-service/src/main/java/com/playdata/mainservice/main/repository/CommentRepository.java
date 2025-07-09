package com.playdata.mainservice.main.repository;

import com.playdata.mainservice.main.entity.Category;
import com.playdata.mainservice.main.entity.Comment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    // userId를 통한 모든 댓글 조회
    Optional<List<Comment>> findByUserId(Long userId);

    // 특정 게시물의 모든 활성화 댓글 id를 조회  --> 댓글 개수 확인용
    @Query("SELECT c.commentId FROM Comment c WHERE c.category = :category AND c.contentId = :contentId AND c.active = true")
    List<Long> findActiveCommentIdsByCategoryAndContentId(@Param("category") Category category,
                                                          @Param("contentId") Long contentId);

    // 특정 게시물의 활성화된 모든 댓글 조회  --> 게시물 상세 조회 시 댓글 상세 조회용
    @Query("SELECT c FROM Comment c WHERE c.category = :category AND c.contentId = :contentId AND c.active = true")
    List<Comment> findActiveByCategoryAndContentId(@Param("category") Category category,
                                                   @Param("contentId") Long contentId);

    // 특정 게시물의 활성화된 모든 댓글 페이징 조회
    @Query("SELECT c FROM Comment c WHERE c.category = :category AND c.contentId = :contentId AND c.active = true")
    Page<Comment> findActiveByCategoryAndContentId(@Param("category") Category category,
                                                   @Param("contentId") Long contentId, Pageable pageable);

    // 모든 활성화된 사용자의 댓글 페이징 조회
    Page<Comment> findActiveByUserId(Long userId, Pageable pageable);

}
