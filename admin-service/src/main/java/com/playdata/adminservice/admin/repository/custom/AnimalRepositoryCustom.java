package com.playdata.adminservice.admin.repository.custom;

import com.playdata.adminservice.admin.dto.board.AnimalSearchDto;
import com.playdata.adminservice.admin.dto.board.res.AnimalListResDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface AnimalRepositoryCustom {

    /**
     * 분양 목록 조회 (검색 조건 및 페이징 처리 포함) 인터페이스
     * @param searchDto
     * @param pageable
     * @return
     */
    Page<AnimalListResDto> findList(AnimalSearchDto searchDto, Pageable pageable);
}
