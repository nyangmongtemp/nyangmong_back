package com.playdata.boardservice.board.repository;

import com.playdata.boardservice.board.entity.Category;
import com.playdata.boardservice.board.entity.InformationBoard;
import com.playdata.boardservice.board.repository.custom.InformationBoardRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface InformationBoardRepository extends JpaRepository<InformationBoard, Long>, InformationBoardRepositoryCustom {

    // 게시물 상세 조회 시 postId, category를 조회
    Optional<InformationBoard> findByPostIdAndCategory(Long postId, Category category);
}
