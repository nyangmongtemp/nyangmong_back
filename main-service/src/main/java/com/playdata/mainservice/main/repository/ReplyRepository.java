package com.playdata.mainservice.main.repository;

import com.playdata.mainservice.main.entity.Reply;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ReplyRepository extends JpaRepository<Reply, Long> {


    Optional<List<Reply>> findByUserId(Long userId);
}
