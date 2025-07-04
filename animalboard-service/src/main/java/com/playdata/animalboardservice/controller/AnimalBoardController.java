package com.playdata.animalboardservice.controller;

import com.playdata.animalboardservice.dto.SearchDto;
import com.playdata.animalboardservice.dto.StraySearchDto;
import com.playdata.animalboardservice.dto.res.AnimalListResDto;
import com.playdata.animalboardservice.service.AnimalService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/animal-board")
public class AnimalBoardController {

    private final AnimalService animalService;

    /**
     * 분양 동물 게시판 목록 조회
     * @param searchDto 검색Dto
     * @param pageable 페이징
     * @return
     */
    @GetMapping("/list")
    public ResponseEntity<Page<?>> getAnimalBoardList(SearchDto searchDto, Pageable pageable) {

        Page<AnimalListResDto> resDto = animalService.findStrayAnimalList(searchDto, pageable);

        return ResponseEntity.ok().body(resDto);
    }

//    @GetMapping("/{id}")
//    public ResponseEntity<?> getAnimalBoard(@PathVariable Long id) {
//
//    }
//
//    @PostMapping
//    public ResponseEntity<Long> createAnimalBoard() {
//
//    }
//
//    @PatchMapping("/{id}")
//    public ResponseEntity<Void> updateAnimalBoard() {
//
//    }
//
//    @DeleteMapping("/{id}")
//    public ResponseEntity<Void> deleteAnimalBoard(@PathVariable Long id) {
//
//    }
}
