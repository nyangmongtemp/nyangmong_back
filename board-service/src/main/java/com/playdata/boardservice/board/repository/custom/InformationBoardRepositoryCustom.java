package com.playdata.boardservice.board.repository.custom;

import com.playdata.boardservice.board.dto.BoardSearchDto;
import com.playdata.boardservice.board.entity.Category;
import com.playdata.boardservice.board.entity.InformationBoard;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;


public interface InformationBoardRepositoryCustom {

    // 분양 목록 조회 (검색 조건 및 페이징 처리 포함) 인터페이스
    Page<InformationBoard> findList(BoardSearchDto boardSearchDto, Category category, Pageable pageable);
}
