package com.playdata.animalboardservice.controller;

import com.playdata.animalboardservice.dto.StraySearchDto;
import com.playdata.animalboardservice.dto.res.StrayAnimalListResDto;
import com.playdata.animalboardservice.dto.res.StrayAnimalResDto;
import com.playdata.animalboardservice.entity.StrayAnimal;
import com.playdata.animalboardservice.service.StrayAnimalService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/stray-animal-board")
public class StrayAnimalBoardController {

    private final StrayAnimalService strayAnimalService;

    /**
     * 유기동물 목록 조회 (검색 조건 및 페이징 처리 포함)
     * @param straySearchDto 검색어
     * @param pageable 페이징
     * @return
     */
    @GetMapping("/list")
    public ResponseEntity<Page<StrayAnimalListResDto>> findStrayAnimalList(
            StraySearchDto straySearchDto, Pageable pageable) {
        // 서비스에서 조회된 유기동물 목록 반환
        Page<StrayAnimalListResDto> resDto = strayAnimalService.findStrayAnimalList(straySearchDto, pageable);
        return ResponseEntity.ok().body(resDto);
    }

    /**
     * 특정 유기동물 상세 조회
     * @param desertionNo
     * @return
     */
    @GetMapping("/{desertionNo}")
    public ResponseEntity<StrayAnimalResDto> getAnimalBoard(@PathVariable String desertionNo) {
        StrayAnimal resDto = strayAnimalService.findByStaryAnimal(desertionNo);
        return ResponseEntity.ok().body(new StrayAnimalResDto(resDto));
    }
}