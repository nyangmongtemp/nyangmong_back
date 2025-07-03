package com.playdata.animalboardservice.controller;

import com.playdata.animalboardservice.dto.SearchDto;
import com.playdata.animalboardservice.dto.StrayAnimalResDto;
import com.playdata.animalboardservice.service.StrayAnimalService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
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

    // 유기동물 목록 조회 (검색 조건 및 페이징 처리 포함)
    @GetMapping("/list")
    public ResponseEntity<Page<?>> findStrayAnimalList(SearchDto searchDto, Pageable pageable) {
        // 서비스에서 조회된 유기동물 목록 반환
        Page<StrayAnimalResDto> resDto = strayAnimalService.findStrayAnimalList(searchDto, pageable);
        return ResponseEntity.ok().body(resDto);
    }

    // 특정 유기동물 상세 조회 (추후 구현 예정)
    @GetMapping("/{id}")
    public ResponseEntity<?> getAnimalBoard(@PathVariable Long id) {
        return new ResponseEntity<>(null, HttpStatus.OK);
    }
}