package com.playdata.festivalservice.controller;

import com.playdata.festivalservice.dto.FestivalResponseDto;
import com.playdata.festivalservice.dto.FestivalSearchDto;
import com.playdata.festivalservice.service.FestivalService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController // REST API 컨트롤러
@RequestMapping("/api") // 공통 URL prefix
@RequiredArgsConstructor // final 필드 자동 생성자 주입
public class FestivalController {

    private final FestivalService festivalService; // 서비스 레이어 주입

    /**
     * 전체 축제 조회 API
     * GET /api/festivals
     *
     * @param festivalSearchDto 검색 조건 (예: 검색어, 지역)
     * @param pageable 페이징 및 정렬 정보
     * @return 페이징된 축제 리스트
     */
    @GetMapping("/festivals")
    public ResponseEntity<Page<FestivalResponseDto>> getFestivalList(
            FestivalSearchDto festivalSearchDto,
            @PageableDefault(size = 10, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable
    ) {
        Page<FestivalResponseDto> resDto = festivalService.findFestivalList(festivalSearchDto, pageable);
        return ResponseEntity.ok(resDto); // 200 OK 응답
    }


}