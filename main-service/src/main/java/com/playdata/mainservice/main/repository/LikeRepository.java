package com.playdata.mainservice.main.repository;

import com.playdata.mainservice.main.entity.Category;
import com.playdata.mainservice.main.entity.ContentType;
import com.playdata.mainservice.main.entity.Like;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface LikeRepository extends JpaRepository<Like, Long> {

    Optional<Like> findByCategoryAndContentTypeAndContentIdAndUserId(Category category, ContentType contentType, Long contentId, Long userId);

    Optional<List<Like>> findByUserId(Long userId);

}
