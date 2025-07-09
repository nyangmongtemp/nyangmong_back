package com.playdata.userservice.user.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.playdata.userservice.common.auth.TokenUserInfo;
import com.playdata.userservice.common.dto.CommonResDto;
import com.playdata.userservice.user.dto.message.req.UserMessageReqDto;
import com.playdata.userservice.user.dto.req.*;
import com.playdata.userservice.user.dto.res.UserEmailAuthResDto;
import com.playdata.userservice.user.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.antlr.v4.runtime.Token;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;


@RestController
@RequestMapping("/user")
@Slf4j
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    /**
     *
     * @param userSaveReqDto
     * @param profileImage
     * @return
     * @throws JsonProcessingException
     */
    // 회원가입
    @PostMapping(value = "/create", consumes = "multipart/form-data")
    public ResponseEntity<?> userCreate(
            @RequestPart("user") @Valid UserSaveReqDto userSaveReqDto,
            // 프로필 이미지는 필수가 아님
            @RequestPart(value = "profileImage", required = false) MultipartFile profileImage
    ) {
        // String으로 받은 값을 Dto로 변환
        /*ObjectMapper objectMapper = new ObjectMapper();
        UserSaveReqDto userSaveReqDto = objectMapper.readValue(userJson, UserSaveReqDto.class);*/

        CommonResDto resDto = userService.userCreate(userSaveReqDto, profileImage);
        return new ResponseEntity<>(resDto, HttpStatus.CREATED);
    }
    
    /***
     *
     * @param userLoginReqDto  --> email, password
     * @return
     */
    // 로그인
    @PostMapping("/login")
    public ResponseEntity<?> userLogin(@RequestBody @Valid UserLoginReqDto userLoginReqDto){
        CommonResDto resDto = userService.login(userLoginReqDto);

        return new ResponseEntity<>(resDto, HttpStatus.OK);
    }
    
    /***
     *
     * @param email
     * @return
     */
    // 회원가입 시 인증코드 발송
    @GetMapping("/verify-email")
    public ResponseEntity<?> sendVerifyEmail(@RequestParam("email") String email){

        CommonResDto resDto = userService.sendVerifyEmailCode(email);

        return new ResponseEntity<>(resDto, HttpStatus.OK);
    }

    /***
     * 
     * @param authResDto  --> email, authCode(인증코드)
     * @return
     */
    // 회원가입 시 이메일 인증 코드 검증
    @PostMapping("/verify-code")
    public ResponseEntity<?> verifyUserEmailCode(@RequestBody @Valid UserEmailAuthResDto authResDto){
        CommonResDto resDto = userService.verifyEmailCode(authResDto);

        return new ResponseEntity<>(resDto, HttpStatus.OK);
    }

    /**
     *
     * @param userInfo
     * @param modiDto --> nickname, phone, address
     * @param profileImage  --> 이미지 url
     * @return
     * @throws JsonProcessingException
     */
    // 내 정보 수정 (비밀번호, 이메일 제외)
    // 로그인 필요 -> 토큰 필요함.
    @PatchMapping(value = "/modify-userinfo", consumes = "multipart/form-data")
    public ResponseEntity<?> modifyUserInfo(@AuthenticationPrincipal TokenUserInfo userInfo
            ,@RequestPart("user") @Valid UserInfoModiReqDto modiDto,
            // 프로필 이미지 변경은 필수가 아님
            @RequestPart(value = "profileImage", required = false) MultipartFile profileImage){

        boolean result = userService.modiUserCommonInfo(userInfo, modiDto, profileImage);

        return new ResponseEntity<>(result ,HttpStatus.OK);
    }

    /**
     * 
     * @param userInfo
     * @param newEmail
     * @return
     */
    // 마이페이지에서 이메일 변경 요청 시 인증 시작하는 로직
    // 토큰 필요
    @GetMapping("/modify-email")
    public ResponseEntity<?> modifyUserEmail(@AuthenticationPrincipal TokenUserInfo userInfo,
                                             @RequestParam String newEmail) {
        CommonResDto resDto = userService.modiUserEmail(newEmail, userInfo);
        return new ResponseEntity<>(resDto, HttpStatus.OK);
    }

    /**
     * 
     * @param userInfo
     * @param authResDto  --> email, authCode(인증코드)
     * @return
     */
    // 마이페이지에서 이메일 변경 요청 인증 코드를 검증하는 로직
    // 인증이 완료되면, 새로운 이메일로 DB에 업데이트
    // 화면단에서는 로그아웃 처리 해야함.
    // 토큰 필요
    @PatchMapping("/verify-new-email")
    public ResponseEntity<?> verifyNewEmail(@AuthenticationPrincipal TokenUserInfo userInfo,
            @RequestBody @Valid UserEmailAuthResDto authResDto){

        CommonResDto resDto = userService.verifyUserNewEmail(authResDto, userInfo);

        return new ResponseEntity<>(resDto, HttpStatus.OK);
    }

    /**
     * 
     * @param userInfo
     * @return
     */
    // 마이페이지에서 비밀번호 변경 요청 시, 등록된 이메일에 인증 코드를 발송하는 로직
    // 토큰 필요
    @GetMapping("/new-password-req")
    public ResponseEntity<?> newPasswordReq(@AuthenticationPrincipal TokenUserInfo userInfo){

        CommonResDto resDto = userService.sendEmailAuthCodeNewPw(userInfo.getEmail());

        return new ResponseEntity<>(resDto, HttpStatus.OK);
    }

    /**
     * 
     * @param userInfo
     * @param authResDto  --> email, authCode(인증코드)
     * @return
     */
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

    /**
     *
     * @param userInfo
     * @param reqDto  --> password, 민감정보라서 data 하나지만 post로 받음
     * @return
     */
    // 비밀번호 변경 인증이 모두 완료되면 변경해주는 메소드
    // 화면단에서는 로그아웃 처리해야함.
    @PatchMapping("/modify-password")
    public ResponseEntity<?> modifyPassword(@AuthenticationPrincipal TokenUserInfo userInfo
            ,@RequestBody UserPasswordModiReqDto reqDto) {

        CommonResDto resDto = userService.modifyNewPassword(userInfo.getUserId(), reqDto);

        return new ResponseEntity<>(resDto, HttpStatus.OK);
    }

    // 비밀번호 분실 시 입력한 이메일로 인증번호를 발송해주는 메소드
    @GetMapping("/forget/{email}")
    public ResponseEntity<?> forgetPasswordReq(@PathVariable String email){
        CommonResDto resDto = userService.forgetPasswordReq(email);

        return new ResponseEntity<>(resDto, HttpStatus.OK);
    }

    // 비밀번호 분실 시, 발급된 인증번호로 인증 후 임시비밀번호를 발급해주는 메소드
    @PostMapping("/forget/auth")
    public ResponseEntity<?> forgetAuth(@RequestBody @Valid UserEmailAuthResDto reqDto){
        CommonResDto resDto = userService.authCodeAndRePw(reqDto);

        return new ResponseEntity<>(resDto, HttpStatus.OK);
    }

    /**
     *
     * @param userInfo
     * @return
     */
    // 마이페이지 요청 메소드
    @GetMapping("/mypage")
    public ResponseEntity<?> userMyPage(@AuthenticationPrincipal TokenUserInfo userInfo){

        CommonResDto myPage = userService.getMyPage(userInfo.getUserId());

        return new ResponseEntity<>(myPage, HttpStatus.OK);
    }

    /**
     *
     * @param userInfo
     * @return
     */
    // 회원 탈퇴 요청 메소드
    @DeleteMapping("/resign")
    public ResponseEntity<?> resignUser(@AuthenticationPrincipal TokenUserInfo userInfo){
        CommonResDto resDto = userService.resignUser(userInfo.getUserId());

        return new ResponseEntity<>(resDto, HttpStatus.OK);
    }

/////////////// 쪽지 관련 로직들입니다.

    // 쪽지를 보내기 위한, 사용자 검색 -> email, nickname으로 검색
    // 마이페이지에서 요청을 보내는 것이기에, token의 정보는 쓰지 않더라도 token이 필요로 하게 함.
    // 비로그인 상태의 사용자는 사용하지 못하게 할 것 임.
    @GetMapping("/search/{keyword}")
    public ResponseEntity<?> searchUser(@AuthenticationPrincipal TokenUserInfo userInfo,
                                        @PathVariable String keyword){
        CommonResDto resDto = userService.searchUser(keyword);

        return new ResponseEntity<>(resDto, HttpStatus.OK);
    }

    // 본인의 활성화된 대화방 조회
    @GetMapping("/chat")
    public ResponseEntity<?> getMyMessageList (@AuthenticationPrincipal TokenUserInfo userInfo){
        CommonResDto resDto = userService.findMyActiveChat(userInfo.getUserId(), userInfo.getNickname());
        return new ResponseEntity<>(resDto, HttpStatus.OK);
    }

    // 대화방 삭제
    @GetMapping("/clear/{chatId}")
    public ResponseEntity<?> clearUserChat(@AuthenticationPrincipal TokenUserInfo userInfo,
                                              @PathVariable(name = "chatId") Long chatId) {
        CommonResDto resDto = userService.clearChat(userInfo.getUserId(), chatId);

        return new ResponseEntity<>(resDto, HttpStatus.OK);
    }


    // 선택한 채팅방의 7일간의 모든 쪽지 내용 조회
    @GetMapping("/chat/list/{id}")
    public ResponseEntity<?> getMyChatList (@AuthenticationPrincipal TokenUserInfo userInfo,
                                            @PathVariable(name = "id") Long chatId){
        CommonResDto resDto
                = userService.getMyChatMessages(userInfo.getUserId(), userInfo.getNickname(), chatId);
        return new ResponseEntity<>(resDto, HttpStatus.OK);
    }


    // 쪽지 발송
    @PostMapping("/send")
    public ResponseEntity<?> sendUserMessage(@AuthenticationPrincipal TokenUserInfo userInfo,
                                             @RequestBody @Valid UserMessageReqDto reqDto){
        CommonResDto resDto = userService.sendMessage(userInfo.getUserId(), userInfo.getNickname(), reqDto);

        return new ResponseEntity<>(resDto, HttpStatus.CREATED);
    }


    /**
     *
     * @param userEmail
     * @return
     */
    // 리프레시 토큰을 통한 Access Token 재발급용 메소드
    // localStorage에 사용자의 이메일을 저장해놓고 이 메소드의 요청값으로 넣자
    // Access Token이 필요한 요청을 보냈을 때, 토큰이 만료된 경우
    // 화면단에서 요청하는 메소드
    @PostMapping("/refresh")
    public ResponseEntity<?> reProvideAccessToken(@RequestBody Map<String, String> userEmail) {
        CommonResDto resDto = userService.reProvideToken(userEmail.get("email"));

        return new ResponseEntity<>(resDto, HttpStatus.OK);
    }


//////// feign 요청을 받는 메소드 들입니다.


    /**
     *
     * @param userId
     * @return
     */
    // 댓글 및 대댓글 생성 시 프로필 이미지 주소를 넘겨주는 메소드
    @GetMapping("/profileImage/{id}")
    ResponseEntity<String> getUserProfileImage(@PathVariable(name = "id") Long userId) {
        String profileImage = userService.getProfileImage(userId);

        return new ResponseEntity<>(profileImage, HttpStatus.OK);
    }

    @GetMapping("/findId/{email}")
    ResponseEntity<?> findUserEmail(@AuthenticationPrincipal TokenUserInfo userInfo) {
        Long foundUserId = userService.findByEmail(userInfo.getEmail());

        return ResponseEntity.ok(foundUserId);
    }

    /**
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
