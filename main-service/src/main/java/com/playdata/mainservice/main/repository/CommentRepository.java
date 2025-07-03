package com.playdata.mainservice.main.repository;

import com.playdata.mainservice.main.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CommentRepository extends JpaRepository<Comment, Long> {


    Optional<List<Comment>> findByUserId(Long userId);
}
