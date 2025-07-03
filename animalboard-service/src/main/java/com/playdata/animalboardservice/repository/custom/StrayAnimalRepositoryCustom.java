package com.playdata.animalboardservice.repository.custom;

import com.playdata.animalboardservice.dto.SearchDto;
import com.playdata.animalboardservice.entity.StrayAnimal;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface StrayAnimalRepositoryCustom {

    // QueryDSL 기반 커스텀 조회용 Repository 인터페이스
    Page<StrayAnimal> findList(SearchDto searchDto, Pageable pageable);

}
