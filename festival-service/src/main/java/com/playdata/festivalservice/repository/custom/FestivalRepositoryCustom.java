package com.playdata.festivalservice.repository.custom;

import com.playdata.festivalservice.dto.FestivalSearchDto;
import com.playdata.festivalservice.entity.FestivalEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface FestivalRepositoryCustom {


    /**
     * 축제 목록 조회 (검색 조건 및 페이징 처리 포함) 인터페이스
     * @param festivalSearchDto
     * @param pageable
     * @return
     */
    Page<FestivalEntity> findList(FestivalSearchDto festivalSearchDto, Pageable pageable);






}
