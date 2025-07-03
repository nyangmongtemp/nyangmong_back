package com.playdata.mainservice.main.repository;

import com.playdata.mainservice.main.entity.Reply;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ReplyRepository extends JpaRepository<Reply, Long> {


    Optional<List<Reply>> findByUserId(Long userId);

    @Query("SELECT COUNT(r) FROM Reply r " +
            "WHERE r.comment.commentId IN :commentIds AND r.active = true")
    Long countAllActiveRepliesByCommentIds(@Param("commentIds") List<Long> commentIds);


}
