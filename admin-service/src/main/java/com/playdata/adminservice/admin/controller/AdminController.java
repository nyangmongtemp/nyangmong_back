package com.playdata.adminservice.admin.controller;

import com.playdata.adminservice.admin.dto.req.AdminLoginReqDto;
import com.playdata.adminservice.admin.dto.req.AdminSaveReqDto;
import com.playdata.adminservice.admin.dto.res.AdminEmailAuthResDto;
import com.playdata.adminservice.admin.service.AdminService;
import com.playdata.adminservice.common.auth.TokenUserInfo;
import com.playdata.adminservice.common.dto.CommonResDto;

// 광고 관리 관련 import 추가
import com.playdata.adminservice.admin.dto.req.AdRegisterReqDto;
import com.playdata.adminservice.admin.dto.res.AdResDto;
import com.playdata.adminservice.admin.dto.req.AdSearchDto;
import com.playdata.adminservice.admin.service.AdvertisementService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

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

    // 광고 등록 API
    @PostMapping("/ads")
    public ResponseEntity<?> createAd(@RequestBody @Valid AdRegisterReqDto dto) {
        AdResDto resDto = advertisementService.registerAd(dto);
        CommonResDto response = new CommonResDto(HttpStatus.CREATED, "등록 완료", resDto);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    // 광고 수정 API
    @PutMapping("/ads/{id}")
    public ResponseEntity<?> updateAd(@PathVariable Long id, @RequestBody @Valid AdRegisterReqDto dto) {
        AdResDto resDto = advertisementService.updateAd(id, dto);
        CommonResDto response = new CommonResDto(HttpStatus.OK, "수정 완료", resDto);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    // 광고 삭제 API
    @DeleteMapping("/ads/{id}")
    public ResponseEntity<?> deleteAd(@PathVariable Long id) {
        advertisementService.deleteAd(id);
        CommonResDto response = new CommonResDto(HttpStatus.NO_CONTENT, "삭제 완료", null);
        return new ResponseEntity<>(response, HttpStatus.NO_CONTENT);
    }

    // 광고 상세 조회 API
    @GetMapping("/ads/{id}")
    public ResponseEntity<?> getAd(@PathVariable Long id) {
        AdResDto resDto = advertisementService.getAd(id);
        CommonResDto response = new CommonResDto(HttpStatus.OK, "조회 완료", resDto);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    // 광고 목록 조회 API
    @PostMapping("/ads/search")
    public ResponseEntity<?> searchAds(@RequestBody AdSearchDto searchDto,
                                       @RequestParam(defaultValue = "0") int page,
                                       @RequestParam(defaultValue = "10") int size,
                                       @RequestParam(defaultValue = "createdAt,DESC") String sort) {
        String[] sortParams = sort.split(",");
        Pageable pageable = PageRequest.of(page, size,
                Sort.by(Sort.Direction.fromString(sortParams[1]), sortParams[0]));
        Page<AdResDto> pageResult = advertisementService.searchAds(searchDto, pageable);
        CommonResDto response = new CommonResDto(HttpStatus.OK, "검색 완료", pageResult);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}