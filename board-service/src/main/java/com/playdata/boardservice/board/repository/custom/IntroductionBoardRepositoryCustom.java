package com.playdata.boardservice.board.repository.custom;

import com.playdata.boardservice.board.dto.BoardSearchDto;
import com.playdata.boardservice.board.entity.InformationBoard;
import com.playdata.boardservice.board.entity.IntroductionBoard;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;


public interface IntroductionBoardRepositoryCustom {

    // 목록 조회 (검색 조건 및 페이징 처리 포함) 인터페이스
    Page<IntroductionBoard> findList(BoardSearchDto boardSearchDto, Pageable pageable);

    // 메인 노출될 리스트 목록 조회 인터페이스
    List<IntroductionBoard> findMainList();

}
