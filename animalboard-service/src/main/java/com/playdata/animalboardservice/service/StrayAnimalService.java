package com.playdata.animalboardservice.service;

import com.playdata.animalboardservice.common.enumeration.ErrorCode;
import com.playdata.animalboardservice.common.exception.CommonException;
import com.playdata.animalboardservice.dto.SearchDto;
import com.playdata.animalboardservice.dto.StrayAnimalListResDto;
import com.playdata.animalboardservice.dto.StrayAnimalResDto;
import com.playdata.animalboardservice.entity.StrayAnimal;
import com.playdata.animalboardservice.repository.StrayAnimalRepository;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class StrayAnimalService {

    private final StrayAnimalRepository strayAnimalRepository;

    /**
     * 유기동물 목록을 조회하고 응답 DTO로 매핑
     * @param searchDto
     * @param pageable
     * @return
     */
    public Page<StrayAnimalListResDto> findStrayAnimalList(SearchDto searchDto, Pageable pageable) {
        // 검색 조건과 페이징 정보를 통해 DB에서 유기동물 목록 조회
        Page<StrayAnimal> strayAnimalList = strayAnimalRepository.findList(searchDto, pageable);

        // Entity → DTO 변환
        return strayAnimalList.map(strayAnimal ->
                StrayAnimalListResDto.builder()
                        .strayAnimal(strayAnimal)
                        .build()
        );
    }

    /**
     * 특정 유기동물 상세 조회
     * @param desertionNo
     * @return
     */
    public StrayAnimal findByStaryAnimal(String desertionNo) {
        StrayAnimal strayAnimal = strayAnimalRepository.findByDesertionNo(desertionNo);
        Optional.ofNullable(strayAnimal).orElseThrow(() -> new CommonException(ErrorCode.DATA_NOT_FOUND));

        return strayAnimal;
    }
}
