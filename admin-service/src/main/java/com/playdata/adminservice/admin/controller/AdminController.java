package com.playdata.adminservice.admin.controller;

import com.playdata.adminservice.admin.dto.req.AdminLoginReqDto;
import com.playdata.adminservice.admin.dto.req.AdminSaveReqDto;
import com.playdata.adminservice.admin.dto.res.AdminEmailAuthResDto;
import com.playdata.adminservice.admin.service.AdminService;
import com.playdata.adminservice.common.dto.CommonResDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/admin")
@Slf4j
@RequiredArgsConstructor
public class AdminController {

    private final AdminService adminService;

    // 회원가입 (총 관리자 DB 넣기 용)
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

    // 로그인 인증번호 검증
    @PostMapping("/verify-code")
    public ResponseEntity<?> verifyAdminEmailCode(@RequestBody @Valid AdminEmailAuthResDto authResDto){
        CommonResDto resDto = adminService.verifyCode(authResDto);

        return new ResponseEntity<>(resDto, HttpStatus.OK);
    }

}
