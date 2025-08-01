package com.playdata.adminservice.admin.repository.custom;


import com.playdata.adminservice.admin.dto.board.BoardSearchDto;
import com.playdata.adminservice.admin.dto.board.res.BoardListResDto;
import com.playdata.adminservice.admin.entity.Category;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface BoardRepositoryCustom {

    // 목록 조회 (검색 조건 및 페이징 처리 포함) 인터페이스
    Page<BoardListResDto> findByList(BoardSearchDto boardSearchDto, Category category, Pageable pageable);


}
