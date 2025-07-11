package com.playdata.boardservice.board.repository;

import com.playdata.boardservice.board.entity.Category;
import com.playdata.boardservice.board.entity.InformationBoard;
import com.playdata.boardservice.board.repository.custom.InformationBoardRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface InformationBoardRepository extends JpaRepository<InformationBoard, Long>, InformationBoardRepositoryCustom {

    // 게시물 상세 조회 시 postId, category를 조회
    Optional<InformationBoard> findByPostIdAndCategory(Long postId, Category category);

    // 사용자의 상태 (비활성화, 사용자 닉네임 변경 등) 가 변경 되었을때 그 사용자 정보 조회
    @Query("SELECT i FROM InformationBoard i WHERE i.userId = :userId")
    List<InformationBoard> findByUserId(@Param("userId") Long userId);
}
