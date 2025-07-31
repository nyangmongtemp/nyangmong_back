package com.playdata.animalboardservice.service;

import com.playdata.animalboardservice.common.enumeration.ErrorCode;
import com.playdata.animalboardservice.common.exception.CommonException;
import com.playdata.animalboardservice.dto.StraySearchDto;
import com.playdata.animalboardservice.dto.res.StrayAnimalDetailResDto;
import com.playdata.animalboardservice.dto.res.StrayAnimalListResDto;
import com.playdata.animalboardservice.entity.StrayAnimal;
import com.playdata.animalboardservice.repository.StrayAnimalRepository;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
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
     * @param straySearchDto
     * @param pageable
     * @return
     */
    public Page<StrayAnimalListResDto> findStrayAnimalList(StraySearchDto straySearchDto, Pageable pageable) {
        return strayAnimalRepository.findList(straySearchDto, pageable);
    }

    /**
     * 특정 유기동물 상세 조회
     * @param desertionNo 유기동물 번호
     * @return
     */
    public StrayAnimal findByStaryAnimal(String desertionNo) {
        StrayAnimal strayAnimal = Optional.ofNullable(strayAnimalRepository.findByDesertionNo(desertionNo)).orElseThrow(() -> new CommonException(ErrorCode.DATA_NOT_FOUND));

        return strayAnimal;
    }

    /**
     * 유기동물 메인 노출될 리스트 목록 조회
     * @return
     */
    public List<StrayAnimalListResDto> findStrayAnimalMainList() {
        return strayAnimalRepository.findMainList();
    }
}
