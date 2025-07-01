package com.playdata.userservice.user.controller;

import com.playdata.userservice.common.auth.JwtTokenProvider;
import com.playdata.userservice.common.auth.TokenUserInfo;
import com.playdata.userservice.common.dto.CommonResDto;
import com.playdata.userservice.user.dto.*;
import com.playdata.userservice.user.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.antlr.v4.runtime.Token;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;



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
    // 인증이 완료되면, 새로운 이메일로 DB에 업데이트
    // 화면단에서는 로그아웃 처리 해야함.
    // 토큰 필요
    @PatchMapping("/verify-new-email")
    public ResponseEntity<?> verifyNewEmail(@AuthenticationPrincipal TokenUserInfo userInfo,
            @RequestBody UserEmailAuthResDto authResDto){

        CommonResDto resDto = userService.verifyUserNewEmail(authResDto, userInfo);

        return new ResponseEntity<>(resDto, HttpStatus.OK);
    }
    
    // 마이페이지에서 비밀번호 변경 요청 시, 등록된 이메일에 인증 코드를 발송하는 로직
    // 토큰 필요
    @GetMapping("/new-password-req")
    public ResponseEntity<?> newPasswordReq(@AuthenticationPrincipal TokenUserInfo userInfo){

        CommonResDto resDto = userService.sendEmailAuthCodeNewPw(userInfo.getEmail());

        return new ResponseEntity<>(resDto, HttpStatus.OK);
    }

    // 마이페이지에서 비밀번호 변경 요청 및 인증 코드 발송 후, 해당 인증 코드를 검증
    // 토큰 필요
    @PostMapping("/verify-new-password")
    public ResponseEntity<?> verifyNewPassword(@AuthenticationPrincipal TokenUserInfo userInfo
            , @RequestBody UserPwAuthReqDto authResDto){

        // 최대한 기존 서비스 로직을 그대로 사용하기 위한 코드
        UserEmailAuthResDto dto = UserEmailAuthResDto.builder()
                .email(userInfo.getEmail())
                .authCode(authResDto.getAuthCode())
                .build();

        CommonResDto resDto = userService.verifyEmailCode(dto);

        return new ResponseEntity<>(resDto, HttpStatus.OK);
    }

    // 비밀번호 변경 인증이 모두 완료되면 변경해주는 메소드
    // 화면단에서는 로그아웃 처리해야함.
    @PatchMapping("/modify-password")
    public ResponseEntity<?> modifyPassword(@AuthenticationPrincipal TokenUserInfo userInfo
            ,@RequestBody UserPasswordModiReqDto reqDto) {

        CommonResDto resDto = userService.modifyNewPassword(userInfo.getEmail(), reqDto);

        return new ResponseEntity<>(resDto, HttpStatus.OK);
    }

    // 마이페이지 요청 메소드
    @GetMapping("/mypage")
    public ResponseEntity<?> userMyPage(@AuthenticationPrincipal TokenUserInfo userInfo){

        CommonResDto myPage = userService.getMyPage(userInfo.getEmail());

        return new ResponseEntity<>(myPage, HttpStatus.OK);
    }

}
