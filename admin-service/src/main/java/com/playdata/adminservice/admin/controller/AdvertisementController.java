package com.playdata.adminservice.admin.controller;

import com.playdata.adminservice.admin.dto.req.*;
import com.playdata.adminservice.admin.dto.res.AdResDto;
import com.playdata.adminservice.admin.entity.Advertisement;
import com.playdata.adminservice.admin.service.AdvertisementService;
import com.playdata.adminservice.common.dto.CommonResDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/admin")
@Slf4j
@RequiredArgsConstructor
public class AdvertisementController {


    private final AdvertisementService advertisementService;



    // 광고 등록 API
    @PostMapping("/ads")
    public ResponseEntity<?> createAd(@RequestBody @Valid AdRegisterReqDto dto) {
        CommonResDto resDto = advertisementService.registerAd(dto);
        return new ResponseEntity<>(resDto, HttpStatus.OK);
    }

    // 광고 수정 API
    @PutMapping("/ads/{id}")
    public ResponseEntity<?> updateAd(@PathVariable Long id, @RequestBody @Valid AdUpdateReqDto dto) {
        CommonResDto resDto = advertisementService.updateAd(id, dto);
        return new ResponseEntity<>(resDto, HttpStatus.OK);
    }


    // 광고 상세 조회 API
    @GetMapping("/ads/{id}")
    public ResponseEntity<?> getAd(@PathVariable Long id) {
        CommonResDto resDto = advertisementService.getAd(id);
        return new ResponseEntity<>(resDto, HttpStatus.OK);
    }

    //광고 조회 API
    @PostMapping("/ads/search")
    public ResponseEntity<?> searchAds(AdSearchDto searchDto, Pageable pageable) {
        Page<AdResDto> pageResult = advertisementService.searchAds(searchDto, pageable);
        CommonResDto resDto = new CommonResDto(HttpStatus.OK, "검색 완료", pageResult);
        return ResponseEntity.ok(resDto);
    }

    /**
     * 광고 개수 수정
     *
     * @param dto 광고 개수 수정 요청 DTO
     * @return 공통 응답 DTO
     */
    @PatchMapping("/ads/count")
    public ResponseEntity<CommonResDto> updateAdCount(@RequestBody @Valid AdCountReqDto dto) {
        log.info("/ads/count: Patch, dto: {}", dto.toString());
        CommonResDto response = advertisementService.updateAdCount(dto);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    // 광고 노출용 리스트 조회 API
    @GetMapping("/ads/display")
    public ResponseEntity<CommonResDto> getAdsForDisplay() {
        List<Advertisement> ads = advertisementService.getAdListForDisplay();
        CommonResDto response = new CommonResDto(HttpStatus.OK, "광고 노출 리스트 조회 성공", ads);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

}
