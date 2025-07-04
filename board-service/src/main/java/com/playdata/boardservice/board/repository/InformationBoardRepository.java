package com.playdata.boardservice.board.repository;

import com.playdata.boardservice.board.entity.InformationBoard;
import com.playdata.boardservice.board.repository.custom.InformationBoardRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InformationBoardRepository extends JpaRepository<InformationBoard, Long>, InformationBoardRepositoryCustom {
}
