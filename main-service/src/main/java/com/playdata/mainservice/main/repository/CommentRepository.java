package com.playdata.mainservice.main.repository;

import com.playdata.mainservice.main.entity.Category;
import com.playdata.mainservice.main.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface CommentRepository extends JpaRepository<Comment, Long> {


    Optional<List<Comment>> findByUserId(Long userId);

    @Query("SELECT c.commentId FROM Comment c WHERE c.category = :category AND c.contentId = :contentId AND c.active = true")
    List<Long> findActiveCommentIdsByCategoryAndContentId(@Param("category") Category category,
                                                          @Param("contentId") Long contentId);
}
