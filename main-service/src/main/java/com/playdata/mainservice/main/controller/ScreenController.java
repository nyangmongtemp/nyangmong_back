package com.playdata.mainservice.main.controller;

import com.playdata.mainservice.common.dto.CommonResDto;
import com.playdata.mainservice.main.service.BannerService;
import io.swagger.v3.oas.annotations.Hidden;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/main/screen")
@RequiredArgsConstructor
@Slf4j
@Hidden
public class ScreenController {

    private final BannerService  bannerService;

    // 노출 배너 목록 조회
    @GetMapping("/banner/list")
    public ResponseEntity<?> getExposedList() {
        CommonResDto resDto = bannerService.getExposedList();

        return new ResponseEntity<>(resDto, HttpStatus.OK);
    }

}
