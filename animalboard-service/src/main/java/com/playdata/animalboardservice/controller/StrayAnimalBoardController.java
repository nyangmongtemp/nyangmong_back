package com.playdata.animalboardservice.controller;

import com.playdata.animalboardservice.common.dto.CommonResDto;
import com.playdata.animalboardservice.dto.StraySearchDto;
import com.playdata.animalboardservice.dto.res.StrayAnimalListResDto;
import com.playdata.animalboardservice.dto.res.StrayAnimalDetailResDto;
import com.playdata.animalboardservice.entity.StrayAnimal;
import com.playdata.animalboardservice.service.StrayAnimalService;
import java.util.List;
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
public class StrayAnimalBoardController implements StrayAnimalBoardControllerDocs{

    private final StrayAnimalService strayAnimalService;

    /**
     * 유기동물 목록 조회 (검색 조건 및 페이징 처리 포함)
     * @param straySearchDto 검색어
     * @param pageable 페이징
     * @return
     */
    @GetMapping("/list")
    public ResponseEntity<CommonResDto> findStrayAnimalList(
            StraySearchDto straySearchDto, Pageable pageable) {
        // 서비스에서 조회된 유기동물 목록 반환
        Page<StrayAnimalListResDto> result = strayAnimalService.findStrayAnimalList(straySearchDto, pageable);
        CommonResDto resDto = new CommonResDto(HttpStatus.OK, "목록 조회", result);
        return new ResponseEntity<>(resDto, HttpStatus.OK);
    }

    /**
     * 특정 유기동물 상세 조회
     * @param desertionNo
     * @return
     */
    @GetMapping("/{desertionNo}")
    public ResponseEntity<CommonResDto> getAnimalBoard(@PathVariable String desertionNo) {
        StrayAnimal result = strayAnimalService.findByStaryAnimal(desertionNo);
        CommonResDto resDto = new CommonResDto(HttpStatus.OK, "상세 조회", result);
        return new ResponseEntity<>(resDto, HttpStatus.OK);
    }

    /**
     * 유기동물 메인 노출될 리스트 목록 조회
     * @return
     */
    @GetMapping("/main")
    public ResponseEntity<CommonResDto> findStrayAnimalMainList() {
        List<StrayAnimalListResDto> result = strayAnimalService.findStrayAnimalMainList();
        CommonResDto resDto = new CommonResDto(HttpStatus.OK, "목록 조회", result);
        return new ResponseEntity<>(resDto, HttpStatus.OK);
    }
}