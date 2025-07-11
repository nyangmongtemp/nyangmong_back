package com.playdata.animalboardservice.repository.custom;

import com.playdata.animalboardservice.dto.StraySearchDto;
import com.playdata.animalboardservice.entity.StrayAnimal;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface StrayAnimalRepositoryCustom {

    /**
     * 유기동물 목록 조회 (검색 조건 및 페이징 처리 포함) 인터페이스
     * @param straySearchDto
     * @param pageable
     * @return
     */
    Page<StrayAnimal> findList(StraySearchDto straySearchDto, Pageable pageable);

    /**
     * 유기동물 메인 노출될 리스트 목록 조회 인터페이스
     * @return
     */
    List<StrayAnimal> findMainList();
}
