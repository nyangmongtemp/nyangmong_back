package com.playdata.mainservice.main.repository;

import com.playdata.mainservice.main.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, Long> {



}
