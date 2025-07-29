package com.playdata.adminservice.admin.controller;

import com.playdata.adminservice.admin.dto.AdminSearchDto;
import com.playdata.adminservice.admin.dto.req.*;
import com.playdata.adminservice.admin.dto.res.AdminEmailAuthResDto;
import com.playdata.adminservice.admin.dto.res.AdminListResDto;
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
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.security.authorization.AuthorityReactiveAuthorizationManager.hasRole;

@RestController
@RequestMapping("/admin")
@Slf4j
@RequiredArgsConstructor
public class AdminController {

    private final AdminService adminService;
    private final AdvertisementService advertisementService;

    /**
     *
     * @param adminSaveReqDto
     * @return
     */
    // 총 관리자 회원가입
    @PostMapping("/create")
    public ResponseEntity<?> adminCreate(@RequestBody AdminSaveReqDto adminSaveReqDto){
        CommonResDto resDto = adminService.create(adminSaveReqDto);
        return new ResponseEntity<>(resDto, HttpStatus.CREATED);
    }

    /**
     *
     * @param adminSaveReqDto
     * @return
     */
    // 관리자 등록
    @PostMapping("/admin-create")
    public ResponseEntity<?> adminPlus(@RequestBody AdminSaveReqDto adminSaveReqDto){
        CommonResDto resDto = adminService.plus(adminSaveReqDto);

        return new ResponseEntity<>(resDto, HttpStatus.CREATED);
    }

    /**
     *
     * @param adminLoginReqDto
     * @return
     */
    // 로그인
    @PostMapping("/login")
    public ResponseEntity<?> adminLogin(@RequestBody @Valid AdminLoginReqDto adminLoginReqDto) {

        CommonResDto resDto = adminService.login(adminLoginReqDto);
        return new ResponseEntity<>(resDto, HttpStatus.OK);
    }

    /**
     *
     * @param authResDto
     * @return
     */
    // 로그인 이메일 2차 검증
    @PostMapping("/verify-code")
    public ResponseEntity<?> verifyAdminEmailCode(@RequestBody @Valid AdminEmailAuthResDto authResDto){
        CommonResDto resDto = adminService.loginVerifyCode(authResDto);

        return new ResponseEntity<>(resDto, HttpStatus.OK);
    }

    /**
     *
     * @param tokenUserInfo
     * @param newEmail
     * @return
     */
    // 이메일 변경 요청
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
    /**
     *
     * @param userInfo
     * @param authResDto
     * @return
     */
    @PatchMapping("/verify-new-email")
    public ResponseEntity<?> verifyNewEmail(@AuthenticationPrincipal TokenUserInfo userInfo,
                                            @RequestBody @Valid AdminEmailAuthResDto authResDto){

        CommonResDto resDto = adminService.verifyAdminNewEmail(authResDto, userInfo);

        return new ResponseEntity<>(resDto, HttpStatus.OK);
    }

    /**
     *
     * @param userInfo
     * @return
     */
    // 비밀번호 변경 요청
    @GetMapping("/modify-password-req")
    public ResponseEntity<?> passwordModifyReq(@AuthenticationPrincipal TokenUserInfo userInfo) {
        CommonResDto resDto = adminService.modifyPasswordReq(userInfo.getEmail());

        return new ResponseEntity<>(resDto, HttpStatus.OK);
    }

    /**
     *
     * @param userInfo
     * @param authReqDto
     * @return
     */
    // 비밀번호 변경 검증
    @PatchMapping("/verify-new-password")
    public ResponseEntity<?> verifyNewPassword(@AuthenticationPrincipal TokenUserInfo userInfo,
                                               @RequestBody @Valid AdminPasswordAuthReqDto authReqDto) {

        CommonResDto resDto = adminService.verifyNewPassword(userInfo, authReqDto);

        return new ResponseEntity<>(resDto, HttpStatus.OK);
    }

    /**
     *
     * @param userInfo
     * @param modifyReqDto
     * @return
     */
    // 비밀번호 변경
    @PatchMapping("/modify-password")
    public ResponseEntity<?> modifyPassword(@AuthenticationPrincipal TokenUserInfo userInfo,
                                            @RequestBody AdminPasswordModifyReqDto modifyReqDto) {

        CommonResDto resDto = adminService.modifyPassword(userInfo, modifyReqDto);

        return new ResponseEntity<>(resDto, HttpStatus.OK);
    }

    /**
     *
     * @param userInfo
     * @param modifyReqDto
     * @return
     */
    // 비밀번호, 이메일 외 정보 수정
    @PatchMapping("/modify")
    public ResponseEntity<?> modify(@AuthenticationPrincipal TokenUserInfo userInfo,
                                    @RequestBody AdminModifyReqDto modifyReqDto) {

        CommonResDto resDto = adminService.myPageModify(userInfo, modifyReqDto);

        return new ResponseEntity<>(resDto, HttpStatus.OK);
    }

    /**
     *
     * @param adminSearchDto
     * @param pageable
     * @return
     */
    // 관리자 목록 조회
    @GetMapping("/list")
    @PreAuthorize("hasRole('BOSS')")
    public ResponseEntity<Page<AdminListResDto>> adminList(AdminSearchDto adminSearchDto, Pageable pageable) {

        Page<AdminListResDto> resDto = adminService.adminList(adminSearchDto, pageable);

        return new ResponseEntity<>(resDto, HttpStatus.OK);
    }

    /**
     *
     * @param adminRoleModifyReqDto
     * @return
     */
    // 관리자 권한, 활성화 상태 변경
    @PatchMapping("/role-modify")
    @PreAuthorize("hasRole('BOSS')")
    public ResponseEntity<?> roleModify(@RequestBody AdminRoleModifyReqDto adminRoleModifyReqDto){
        CommonResDto resDto = adminService.roleModify(adminRoleModifyReqDto);

        return new ResponseEntity<>(resDto, HttpStatus.OK);
    }

    /**
     *
     * @param userInfo
     * @return
     */
    // 토큰 검증
    @GetMapping("/temp22")
    public ResponseEntity<?> temp22(@AuthenticationPrincipal TokenUserInfo userInfo){
        log.info(userInfo.toString());
        return ResponseEntity.ok(userInfo);
    }
}