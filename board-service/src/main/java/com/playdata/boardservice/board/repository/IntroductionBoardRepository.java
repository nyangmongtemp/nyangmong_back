package com.playdata.boardservice.board.repository;

import com.playdata.boardservice.board.entity.IntroductionBoard;
import com.playdata.boardservice.board.repository.custom.IntroductionBoardRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface IntroductionBoardRepository extends JpaRepository<IntroductionBoard, Long>, IntroductionBoardRepositoryCustom {

    // 사용자의 상태 (비활성화, 사용자 닉네임 변경 등) 가 변경 되었을때 그 사용자 정보 조회
    List<IntroductionBoard> findUserId(Long userId);

}
