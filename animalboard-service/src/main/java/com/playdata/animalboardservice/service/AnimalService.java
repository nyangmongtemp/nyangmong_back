package com.playdata.animalboardservice.service;

import com.playdata.animalboardservice.dto.SearchDto;
import com.playdata.animalboardservice.dto.StraySearchDto;
import com.playdata.animalboardservice.dto.res.AnimalListResDto;
import com.playdata.animalboardservice.entity.Animal;
import com.playdata.animalboardservice.repository.AnimalRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class AnimalService {

    private final AnimalRepository animalRepository;

    /**
     * 분양 목록을 조회하고 응답 DTO로 매핑
     * @param searchDto
     * @param pageable
     * @return
     */
    public Page<AnimalListResDto> findStrayAnimalList(SearchDto searchDto, Pageable pageable) {
        // 검색 조건과 페이징 정보를 통해 DB에서 유기동물 목록 조회
        Page<Animal> animalList = animalRepository.findList(searchDto, pageable);

        // Entity → DTO 변환
        return animalList.map(animal ->
                AnimalListResDto.builder()
                        .animal(animal)
                        .build()
        );
    }

}
