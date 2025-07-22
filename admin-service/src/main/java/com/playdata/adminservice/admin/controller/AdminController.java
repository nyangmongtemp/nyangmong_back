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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
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

    // 회원가입 (총 관리자 회원가입)
    @PostMapping("/create")
    public ResponseEntity<?> adminCreate(@RequestBody AdminSaveReqDto adminSaveReqDto){
        CommonResDto resDto = adminService.create(adminSaveReqDto);
        return new ResponseEntity<>(resDto, HttpStatus.CREATED);
    }

    // 관리자 생성
    @PostMapping("/admin-create")
    public ResponseEntity<?> adminPlus(@RequestBody AdminSaveReqDto adminSaveReqDto){
        CommonResDto resDto = adminService.plus(adminSaveReqDto);

        return new ResponseEntity<>(resDto, HttpStatus.CREATED);
    }

    // 로그인
    @PostMapping("/login")
    public ResponseEntity<?> adminLogin(@RequestBody @Valid AdminLoginReqDto adminLoginReqDto) {

        log.error(adminLoginReqDto.toString());

        CommonResDto resDto = adminService.login(adminLoginReqDto);
        return new ResponseEntity<>(resDto, HttpStatus.OK);
    }

    // 인증 코드 확인
    @PostMapping("/verify-code")
    public ResponseEntity<?> verifyAdminEmailCode(@RequestBody @Valid AdminEmailAuthResDto authResDto){
        CommonResDto resDto = adminService.loginVerifyCode(authResDto);

        return new ResponseEntity<>(resDto, HttpStatus.OK);
    }

//    // 총 관리자가 타 관리자의 권한, 활성화 여부 수정
//    @PatchMapping("/role-modify")
//    public ResponseEntity<?> roleModify(@RequestBody AdminRoleModifyReqDto adminRoleModifyReqDto){
//        CommonResDto resDto = adminService.roleModify(adminRoleModifyReqDto);
//
//        return new ResponseEntity<>(resDto, HttpStatus.OK);
//    }

    // 관리자 이메일 변경 요청
    @GetMapping("/modify-email")
    public ResponseEntity<?> emailModify(@AuthenticationPrincipal TokenUserInfo tokenUserInfo,
                                         @RequestParam String newEmail) {

        CommonResDto resDto = adminService.modifyEmail(tokenUserInfo, newEmail);

        return new ResponseEntity<>(resDto, HttpStatus.OK);
    }

    // 마이페이지에서 이메일 변경 요청 인증 코드를 검증하는 로직
    // 인증이 완료되면, 새로운 이메일로 DB에 업데이트
    // 화면단에서는 로그아웃 처리 해야함.
    // 토큰 필요
    @PatchMapping("/verify-new-email")
    public ResponseEntity<?> verifyNewEmail(@AuthenticationPrincipal TokenUserInfo userInfo,
                                            @RequestBody @Valid AdminEmailAuthResDto authResDto){

        CommonResDto resDto = adminService.verifyAdminNewEmail(authResDto, userInfo);

        return new ResponseEntity<>(resDto, HttpStatus.OK);
    }

    // 관리자 비밀변호 변경 요청
    @GetMapping("/modify-password-req")
    public ResponseEntity<?> passwordModifyReq(@AuthenticationPrincipal TokenUserInfo userInfo) {
        CommonResDto resDto = adminService.modifyPasswordReq(userInfo.getEmail());

        return new ResponseEntity<>(resDto, HttpStatus.OK);
    }

    // 관리자 비밀번호 변경 요청 검증
    @PatchMapping("/verify-new-password")
    public ResponseEntity<?> verifyNewPassword(@AuthenticationPrincipal TokenUserInfo userInfo,
                                               @RequestBody @Valid AdminPasswordAuthReqDto authReqDto) {

        CommonResDto resDto = adminService.verifyNewPassword(userInfo, authReqDto);

        return new ResponseEntity<>(resDto, HttpStatus.OK);
    }

    // 관리자 비밀번호 변경
    @PatchMapping("/modify-password")
    public ResponseEntity<?> modifyPassword(@AuthenticationPrincipal TokenUserInfo userInfo,
                                            @RequestBody AdminPasswordModifyReqDto modifyReqDto) {

        CommonResDto resDto = adminService.modifyPassword(userInfo, modifyReqDto);

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
        CommonResDto resDto = advertisementService.registerAd(dto);
        return new ResponseEntity<>(resDto, HttpStatus.OK);
    }

    // 광고 수정 API
    @PutMapping("/ads/{id}")
    public ResponseEntity<?> updateAd(@PathVariable Long id, @RequestBody @Valid AdUpdateReqDto dto) {
        CommonResDto resDto = advertisementService.updateAd(id, dto);
        return new ResponseEntity<>(resDto, HttpStatus.OK);
    }

    // 광고 삭제(비활성화) API
    @DeleteMapping("/ads/{id}")
    public ResponseEntity<?> deleteAd(@PathVariable Long id) {
        CommonResDto resDto = advertisementService.deleteAd(id);
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

    // 광고 순서 변경 API
    @PutMapping("/ads/order")
    public ResponseEntity<?> updateAdOrder(@RequestBody @Valid  List<AdOrderReqDto> orderDtoList) {
        advertisementService.updateAdOrder(orderDtoList);
        return new ResponseEntity<>(new CommonResDto(HttpStatus.OK, "광고 순서 수정 완료", null), HttpStatus.OK);
    }
}