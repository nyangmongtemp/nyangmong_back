package com.playdata.boardservice.board.repository;

import com.playdata.boardservice.board.entity.IntroductionBoard;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IntroductionBoardRepository extends JpaRepository<IntroductionBoard, Long> {
}
