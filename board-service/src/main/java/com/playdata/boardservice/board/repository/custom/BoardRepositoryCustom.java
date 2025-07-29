package com.playdata.boardservice.board.repository.custom;

import com.playdata.boardservice.board.dto.BoardSearchDto;
import com.playdata.boardservice.board.entity.Board;
import com.playdata.boardservice.board.entity.Category;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface BoardRepositoryCustom {

    // 목록 조회 (검색 조건 및 페이징 처리 포함) 인터페이스
    Page<Board> findList(BoardSearchDto boardSearchDto, Category category, Pageable pageable);

    // 메인 노출될 리스트 목록 조회 인터페이스
    List<Board> findMainList();

    // 조회수 기반 메인 인기 게시글 조회
    List<Board> findPopularList(int limit, int days);

}
