package com.playdata.userservice.user.controller;

import com.playdata.userservice.common.auth.JwtTokenProvider;
import com.playdata.userservice.common.auth.TokenUserInfo;
import com.playdata.userservice.common.dto.CommonResDto;
import com.playdata.userservice.user.dto.*;
import com.playdata.userservice.user.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/user")
@Slf4j
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    // 회원가입
    @PostMapping("/create")
    public ResponseEntity<?> userCreate(@ModelAttribute UserSaveReqDto userSaveReqDto){
        CommonResDto resDto = userService.userCreate(userSaveReqDto);

        // 회원 가입에 성공한 경우
        return new ResponseEntity<>(resDto, HttpStatus.CREATED);

    }
    
    // 로그인
    @PostMapping("/login")
    public ResponseEntity<?> userLogin(@RequestBody UserLoginReqDto userLoginReqDto){
        CommonResDto resDto = userService.login(userLoginReqDto);

        return new ResponseEntity<>(resDto, HttpStatus.OK);
    }
    
    // 회원가입 시 인증코드 발송
    @GetMapping("/verify-email")
    public ResponseEntity<?> sendVerifyEmail(@RequestParam("email") String email){

        CommonResDto resDto = userService.sendVerifyEmailCode(email);

        return new ResponseEntity<>(resDto, HttpStatus.OK);
    }

    // 회원가입 시 이메일 인증 코드 검증
    @PostMapping("/verify-code")
    public ResponseEntity<?> verifyUserEmailCode(@RequestBody UserEmailAuthResDto authResDto){
        CommonResDto resDto = userService.verifyEmailCode(authResDto);

        return new ResponseEntity<>(resDto, HttpStatus.OK);
    }

    // 내 정보 수정 (비밀번호, 이메일 제외)
    // 로그인 필요 -> 토큰 필요함.
    @PatchMapping("/modify-userinfo")
    public ResponseEntity<?> modifyUserInfo(@AuthenticationPrincipal TokenUserInfo userInfo
            , @ModelAttribute UserInfoModiReqDto modiDto){
        userService.modiUserCommonInfo(userInfo, modiDto);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    // 마이페이지에서 이메일 변경 요청 시 인증 시작하는 로직
    // 토큰 필요
    @GetMapping("/modify-email")
    public ResponseEntity<?> modifyUserEmail(@AuthenticationPrincipal TokenUserInfo userInfo,
                                             @RequestParam String newEmail) {
        CommonResDto resDto = userService.modiUserEmail(newEmail, userInfo);
        return new ResponseEntity<>(resDto, HttpStatus.OK);
    }

    // 마이페이지에서 이메일 변경 요청 인증 코드를 검증하는 로직
    @PatchMapping("/verify-new-email")
    public ResponseEntity<?> verifyNewEmail(@AuthenticationPrincipal TokenUserInfo userInfo ,
            @RequestBody UserEmailAuthResDto authResDto){
        CommonResDto resDto = userService.verifyUserNewEmail(authResDto, userInfo);

        return new ResponseEntity<>(resDto, HttpStatus.OK);
    }

}
