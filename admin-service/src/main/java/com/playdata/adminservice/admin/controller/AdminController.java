package com.playdata.adminservice.admin.controller;

import com.playdata.adminservice.admin.dto.req.*;
import com.playdata.adminservice.admin.dto.res.AdminEmailAuthResDto;
import com.playdata.adminservice.admin.service.AdminService;
import com.playdata.adminservice.common.auth.TokenUserInfo;
import com.playdata.adminservice.common.dto.CommonResDto;

// 광고 관리 관련 import 추가
import com.playdata.adminservice.admin.dto.res.AdResDto;
import com.playdata.adminservice.admin.service.AdvertisementService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin")
@Slf4j
@RequiredArgsConstructor
public class AdminController {

    private final AdminService adminService;
    private final AdvertisementService advertisementService;

    // 관리자 생성
    @PostMapping("/create")
    public ResponseEntity<?> adminCreate(@RequestBody AdminSaveReqDto adminSaveReqDto){
        CommonResDto resDto = adminService.create(adminSaveReqDto);
        return new ResponseEntity<>(resDto, HttpStatus.CREATED);
    }

    // 로그인
    @PostMapping("/login")
    public ResponseEntity<?> adminLogin(@RequestBody @Valid AdminLoginReqDto adminLoginReqDto) {
        CommonResDto resDto = adminService.login(adminLoginReqDto);
        return new ResponseEntity<>(resDto, HttpStatus.OK);
    }

    // 인증 코드 확인
    @PostMapping("/verify-code")
    public ResponseEntity<?> verifyAdminEmailCode(@RequestBody @Valid AdminEmailAuthResDto authResDto){
        CommonResDto resDto = adminService.verifyCode(authResDto);
        return new ResponseEntity<>(resDto, HttpStatus.OK);
    }

    // 임시 토큰 검증
    @GetMapping("/temp22")
    public ResponseEntity<?> temp22(@AuthenticationPrincipal TokenUserInfo userInfo){
        log.info(userInfo.toString());
        return ResponseEntity.ok(userInfo);
    }


}