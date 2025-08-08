package com.playdata.animalboardservice.controller;

import com.playdata.animalboardservice.common.dto.CommonResDto;
import com.playdata.animalboardservice.dto.StraySearchDto;
import com.playdata.animalboardservice.dto.res.StrayAnimalListResDto;
import com.playdata.animalboardservice.dto.res.StrayAnimalDetailResDto;
import com.playdata.animalboardservice.entity.StrayAnimal;
import com.playdata.animalboardservice.service.StrayAnimalService;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.net.URL;


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
    
    // 이미지 프록시용
    @GetMapping("/proxy-image")
    public ResponseEntity<byte[]> proxyImage(@RequestParam String imageUrl) {
        try {
            URL url = new URL(imageUrl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");

            InputStream is = conn.getInputStream();
            byte[] imageBytes = is.readAllBytes();

            HttpHeaders headers = new HttpHeaders();
            String contentType = conn.getContentType(); // 예: "image/png", "image/jpeg"
            headers.setContentType(MediaType.parseMediaType(contentType));

            return new ResponseEntity<>(imageBytes, headers, HttpStatus.OK);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }


}