package com.playdata.festivalservice.controller;

import com.playdata.festivalservice.dto.FestivalResponseDto;
import com.playdata.festivalservice.service.FestivalService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class FestivalController {

    private final FestivalService festivalService;

    /**
     * 전체 축제 조회
     * GET /api/festivals
     */
    @GetMapping("/festivals")
    public List<FestivalResponseDto> getAllFestivals() {
        return festivalService.getAllFestivals();
    }

    /**
     * 단일 축제 상세 조회
     * GET /api/festivals/{id}
     */
    @GetMapping("/festivals/{id}")
    public FestivalResponseDto getFestival(@PathVariable Long id) {
        return festivalService.getFestivalById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 축제를 찾을 수 없습니다."));
    }
}