package com.playdata.userservice.user.controller;

import com.playdata.userservice.common.auth.JwtTokenProvider;
import com.playdata.userservice.user.dto.UserLoginReqDto;
import com.playdata.userservice.user.dto.UserLoginResDto;
import com.playdata.userservice.user.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/user")
@Slf4j
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    private final JwtTokenProvider jwtTokenProvider;


    @PostMapping("/login")
    public ResponseEntity<?> userLogin(@RequestBody UserLoginReqDto userLoginReqDto) {

        UserLoginResDto result = userService.login(userLoginReqDto);

        if(result == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        if(!result.isLogged()) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }

        String token
                = jwtTokenProvider.createToken(result.getEmail());
        Map<String ,Object> loginInfo = new HashMap<>();
        loginInfo.put("token",token);
        loginInfo.put("email", result.getEmail());

        return new ResponseEntity<>(loginInfo, HttpStatus.OK);
    }

}
