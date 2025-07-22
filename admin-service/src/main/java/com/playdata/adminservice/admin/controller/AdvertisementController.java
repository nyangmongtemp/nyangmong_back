package com.playdata.adminservice.admin.controller;

import com.playdata.adminservice.admin.dto.req.AdOrderReqDto;
import com.playdata.adminservice.admin.dto.req.AdRegisterReqDto;
import com.playdata.adminservice.admin.dto.req.AdSearchDto;
import com.playdata.adminservice.admin.dto.req.AdUpdateReqDto;
import com.playdata.adminservice.admin.dto.res.AdResDto;
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

}
