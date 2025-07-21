package com.playdata.adminservice.admin.controller;

import com.playdata.adminservice.admin.dto.req.AdminLoginReqDto;
import com.playdata.adminservice.admin.dto.req.AdminPasswordAuthReqDto;
import com.playdata.adminservice.admin.dto.req.AdminPasswordModifyReqDto;
import com.playdata.adminservice.admin.dto.req.AdminSaveReqDto;
import com.playdata.adminservice.admin.dto.res.AdminEmailAuthResDto;
import com.playdata.adminservice.admin.service.AdminService;
import com.playdata.adminservice.common.auth.TokenUserInfo;
import com.playdata.adminservice.common.dto.CommonResDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
        CommonResDto resDto = adminService.login(adminLoginReqDto);

        return new ResponseEntity<>(resDto, HttpStatus.OK);
    }

    // 로그인 인증번호 검증
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

    /**
     *
     *
     *
     * @param userInfo
     * @return
     */
    // 토큰 검증용 메소드 --> 추후 삭제 예정
    @GetMapping("/temp22")
    public ResponseEntity<?> temp22(@AuthenticationPrincipal TokenUserInfo userInfo){
        log.info(userInfo.toString());
        return ResponseEntity.ok(userInfo);
    }

}
