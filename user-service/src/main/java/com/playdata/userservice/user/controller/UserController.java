package com.playdata.userservice.user.controller;

import com.playdata.userservice.common.auth.JwtTokenProvider;
import com.playdata.userservice.common.auth.TokenUserInfo;
import com.playdata.userservice.common.dto.CommonResDto;
import com.playdata.userservice.user.dto.UserLoginReqDto;
import com.playdata.userservice.user.dto.UserLoginResDto;
import com.playdata.userservice.user.dto.UserSaveReqDto;
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

    private final JwtTokenProvider jwtTokenProvider;

    @PostMapping("/create")
    public ResponseEntity<?> userCreate(@ModelAttribute UserSaveReqDto userSaveReqDto){
        Object isCraeted = userService.userCreate(userSaveReqDto);
        
        // 파일 저장 간에 오류가 발생한 경우
        if(isCraeted == null){
            CommonResDto resDto = new CommonResDto(HttpStatus.INTERNAL_SERVER_ERROR, "회원가입에 실패하였습니다", false);
            return new ResponseEntity<>(resDto, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        // 이미 존재하는 이메일인 경우
        else if(isCraeted instanceof Boolean && !((Boolean) isCraeted)){
            CommonResDto resDto = new CommonResDto(HttpStatus.BAD_REQUEST, "이미 존재하는 이메일입니다.", false);
            return new ResponseEntity<>(resDto, HttpStatus.BAD_REQUEST);
        }
        // 회원가입에 성공한 경우
        CommonResDto resDto = new CommonResDto(HttpStatus.CREATED, "회원가입에 성공하였습니다", true);
        return new ResponseEntity<>(resDto, HttpStatus.CREATED);

    }

    @PostMapping("/login")
    public ResponseEntity<?> userLogin(@RequestBody UserLoginReqDto userLoginReqDto){
        userService.login(userLoginReqDto);
    }

    // 임시로 토큰 발급 과정을 보기 위한 메소드입니다.
    @PostMapping("/templogin")
    public ResponseEntity<?> userTempLogin(@RequestBody UserLoginReqDto userLoginReqDto) {

        UserLoginResDto result = userService.tempLogin(userLoginReqDto);

        // 입력한 이메일이 DB에 없는 경우
        if(result == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        
        // 입력한 비밀번호가 일치하지 않는 경우
        if(!result.isLogged()) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
        
        // 로그인을 성공한 경우
        // 토큰 발급 시작
        String token
                = jwtTokenProvider.createToken(result.getEmail(), "USER");
        Map<String ,Object> loginInfo = new HashMap<>();
        loginInfo.put("token",token);
        loginInfo.put("email", result.getEmail());

        return new ResponseEntity<>(loginInfo, HttpStatus.OK);
    }
    
    // 토큰이 필요한 요청에서 Jwt Filter가 제대로 동작하는 지 확인하는 메소드
    @GetMapping("/temp")
    public ResponseEntity<?> temp(@AuthenticationPrincipal TokenUserInfo userInfo) {

        // 유효한 토큰이면 OK
        return new ResponseEntity<>(userInfo, HttpStatus.OK);

        // 유효하지 않은 토큰인 경우 JwtAuthFilter 에서 Invalid Token 이라는 값을 리턴할 것임.
    }

}
