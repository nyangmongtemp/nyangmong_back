package com.playdata.mainservice.main.repository;

import com.playdata.mainservice.main.entity.Category;
import com.playdata.mainservice.main.entity.Like;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface LikeRepository extends JpaRepository<Like, Long> {

    Optional<List<Like>> findByUserId(Long userId);

    Optional<Like> findByCategoryAndContentIdAndUserId(Category category, Long contentId, Long userId);

    // contentType, category, contentId가 모두 일치하고 active가 true인 데이터 개수 조회
    Long countByCategoryAndContentIdAndActiveIsTrue(Category category, Long contentId);
    
    // 마이페이지에서 사용자가 작성한 활성화된 모든 댓글의 개수 조회
    Long countByContentIdAndActiveTrue (Long contentId);
}
