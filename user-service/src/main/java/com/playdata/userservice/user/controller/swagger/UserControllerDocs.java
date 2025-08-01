package com.playdata.userservice.user.controller.swagger;

import com.playdata.userservice.common.auth.TokenUserInfo;
import com.playdata.userservice.common.dto.CommonResDto;
import com.playdata.userservice.common.enumeration.UserSwaggerEx;
import com.playdata.userservice.common.exception.CommonException;
import com.playdata.userservice.user.dto.inform.req.InformModiReqDto;
import com.playdata.userservice.user.dto.inform.req.InformReqDto;
import com.playdata.userservice.user.dto.message.req.UserMessageReqDto;
import com.playdata.userservice.user.dto.report.req.ReportSaveReqDto;
import com.playdata.userservice.user.dto.req.*;
import com.playdata.userservice.user.dto.res.UserEmailAuthResDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

@Tag(name = "유저 서비스", description = "사용자 관련 API")
public interface UserControllerDocs {

    @Operation(summary = "사용자 생성",
            description = """
               회원가입을 진행합니다.

           """)
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201", description = "생성 성공", content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = CommonException.class),
                    examples = @ExampleObject(value = UserSwaggerEx.USER_CREATE)
            )),
            @ApiResponse(
                    responseCode = "400", description = "중복 이메일", content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = CommonException.class),
                    examples = @ExampleObject(value = UserSwaggerEx.DUPLICATED_DATA)
            )),
            @ApiResponse(
                    responseCode = "500", description = "파일 에러", content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = CommonException.class),
                    examples = @ExampleObject(value = UserSwaggerEx.FILE_INVALID_ERROR)
            ))
    })
    ResponseEntity<CommonResDto> userCreate(
            @Parameter(
                    name = "user",
                    description = "유저 JSON 데이터",
                    required = true,
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = UserSaveReqDto.class))
            )
            @RequestPart("user") @Valid UserSaveReqDto userSaveReqDto,
            // 프로필 이미지는 필수가 아님
            @Parameter(
                    name = "profileImage",
                    description = "프로필 이미지 파일",
                    required = true,
                    content = {
                            @Content(mediaType = "image/jpeg"),
                            @Content(mediaType = "image/png"),
                            @Content(mediaType = "image/gif"),
                            @Content(mediaType = "image/bmp"),
                            @Content(mediaType = "image/webp")
                    }
            )
            @RequestPart(value = "profileImage", required = false) MultipartFile profileImage
    );

    @Operation(summary = "로그인",
            description = """
               로그인을 진행합니다.

           """)
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200", description = "로그인 성공", content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = UserLoginReqDto.class),
                    examples = @ExampleObject(value = UserSwaggerEx.USER_LOGIN)
            )),
            @ApiResponse(
                    responseCode = "401", description = "계정이 잠겼습니다.", content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = CommonException.class),
                    examples = @ExampleObject(value = UserSwaggerEx.ACCOUNT_DISABLED)
            )),
            @ApiResponse(
                    responseCode = "401", description = "계정이 잠겼습니다.", content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = CommonException.class),
                    examples = @ExampleObject(value = UserSwaggerEx.ACCOUNT_LOCKED)
            )),
            @ApiResponse(
                    responseCode = "404", description = "회원가입이 되지 않은 이메일입니다.", content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = CommonException.class),
                    examples = @ExampleObject(value = UserSwaggerEx.ACCOUNT_NOT_FOUND)
            )),
            @ApiResponse(
                    responseCode = "401", description = "비밀번호 오류", content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = CommonException.class),
                    examples = @ExampleObject(value = UserSwaggerEx.INVALID_PASSWORD)
            )),
    })
    ResponseEntity<CommonResDto> userLogin(@RequestBody @Valid UserLoginReqDto userLoginReqDto);



    @Operation(summary = "회원가입 이메일 인증 전송",
            description = """
               이메일 인증을 진행합니다.

           """)
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200", description = "전송 성공", content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = UserLoginReqDto.class),
                    examples = @ExampleObject(value = UserSwaggerEx.SEND_VERIFY_CODE)
            )),
            @ApiResponse(
                    responseCode = "401", description = "회원가입 되지 않은 이메일입니다.", content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = CommonException.class),
                    examples = @ExampleObject(value = UserSwaggerEx.ACCOUNT_DISABLED)
            )),
            @ApiResponse(
                    responseCode = "400", description = "중복 이메일", content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = CommonException.class),
                    examples = @ExampleObject(value = UserSwaggerEx.DUPLICATED_DATA)
            )),
            @ApiResponse(
                    responseCode = "400", description = "이메일 전송 오류", content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = CommonException.class),
                    examples = @ExampleObject(value = UserSwaggerEx.MAIL_SERVER_ERROR)
            )),
    })
    ResponseEntity<CommonResDto> sendVerifyEmail(@RequestParam("email") String email);



    @Operation(summary = "회원가입 이메일 인증",
            description = """
               이메일 인증을 진행합니다.

           """)
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200", description = "인증 성공", content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = UserLoginReqDto.class),
                    examples = @ExampleObject(value = UserSwaggerEx.VERIFY_CODE)
            )),
            @ApiResponse(
                    responseCode = "401", description = "인증코드 만료", content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = CommonException.class),
                    examples = @ExampleObject(value = UserSwaggerEx.EXPIRED_AUTH_CODE)
            )),
            @ApiResponse(
                    responseCode = "400", description = "인증 횟수 초과로 인한 30분간 잠김", content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = CommonException.class),
                    examples = @ExampleObject(value = UserSwaggerEx.AUTH_DISALLOWED)
            )),
            @ApiResponse(
                    responseCode = "400", description = "인증코드 불일치", content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = CommonException.class),
                    examples = @ExampleObject(value = UserSwaggerEx.INVALID_AUTH_CODE)
            ))
    })
    ResponseEntity<CommonResDto> verifyUserEmailCode(@RequestBody @Valid UserEmailAuthResDto authResDto);


    @Operation(summary = "회원정보 수정",
            description = """
               회원정보 수정을 진행합니다.
           
               ## 인증
               - 로그인 해야 수정 가능합니다.
           """)
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200", description = "변경 성공", content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = UserLoginReqDto.class),
                    examples = @ExampleObject(value = UserSwaggerEx.TRUE)
            )),
            @ApiResponse(
                    responseCode = "401", description = "인증코드 만료", content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = CommonException.class),
                    examples = @ExampleObject(value = UserSwaggerEx.INTERNAL_SERVER_ERROR)
            )),
            @ApiResponse(
                    responseCode = "500", description = "파일 에러", content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = CommonException.class),
                    examples = @ExampleObject(value = UserSwaggerEx.FILE_INVALID_ERROR)
            ))
    })
    ResponseEntity<Boolean> modifyUserInfo(@AuthenticationPrincipal TokenUserInfo userInfo
            ,@RequestPart("user") @Valid UserInfoModiReqDto modiDto,
                                           // 프로필 이미지는 필수가 아님
                                           @Parameter(
                                                   name = "profileImage",
                                                   description = "프로필 이미지 파일",
                                                   required = true,
                                                   content = {
                                                           @Content(mediaType = "image/jpeg"),
                                                           @Content(mediaType = "image/png"),
                                                           @Content(mediaType = "image/gif"),
                                                           @Content(mediaType = "image/bmp"),
                                                           @Content(mediaType = "image/webp")
                                                   }
                                           )
                                           @RequestPart(value = "profileImage", required = false) MultipartFile profileImage);


    @Operation(summary = "회원의 이메일 변경 인증 발송",
            description = """
               변경용 이메일 인증 발송을 진행합니다.
           
                ## 인증
               - 로그인 해야 인증 발송이 가능합니다.
           """)
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200", description = "발송 성공", content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = UserLoginReqDto.class),
                    examples = @ExampleObject(value = UserSwaggerEx.EMAIL_VERIFY_CODE)
            )),
            @ApiResponse(
                    responseCode = "400", description = "중복 이메일", content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = CommonException.class),
                    examples = @ExampleObject(value = UserSwaggerEx.DUPLICATED_DATA)
            )),
            @ApiResponse(
                    responseCode = "400", description = "이메일 전송 오류", content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = CommonException.class),
                    examples = @ExampleObject(value = UserSwaggerEx.MAIL_SERVER_ERROR)
            )),
    })
    ResponseEntity<CommonResDto> modifyUserEmail(@AuthenticationPrincipal TokenUserInfo userInfo,
                                                 @RequestParam String newEmail);


    @Operation(summary = "변경 이메일 인증",
            description = """
               변경용 이메일 인증을 진행합니다.
           
                ## 인증
               - 로그인 해야 인증 가능합니다.
           """)
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200", description = "인증 성공", content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = UserLoginReqDto.class),
                    examples = @ExampleObject(value = UserSwaggerEx.VERIFY_CODE)
            )),
            @ApiResponse(
                    responseCode = "401", description = "인증코드 만료", content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = CommonException.class),
                    examples = @ExampleObject(value = UserSwaggerEx.EXPIRED_AUTH_CODE)
            )),
            @ApiResponse(
                    responseCode = "400", description = "인증 횟수 초과로 인한 30분간 잠김", content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = CommonException.class),
                    examples = @ExampleObject(value = UserSwaggerEx.AUTH_DISALLOWED)
            )),
            @ApiResponse(
                    responseCode = "400", description = "인증코드 불일치", content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = CommonException.class),
                    examples = @ExampleObject(value = UserSwaggerEx.INVALID_AUTH_CODE)
            ))
    })
    ResponseEntity<CommonResDto> verifyNewEmail(@AuthenticationPrincipal TokenUserInfo userInfo,
                                                @RequestBody @Valid UserEmailAuthResDto authResDto);



    @Operation(summary = "회원의 비밀번호 변경 인증 발송",
            description = """
               비밀번호 변경용 이메일 인증 발송을 진행합니다.
           
                ## 인증
               - 로그인 해야 인증 발송이 가능합니다.
           """)
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200", description = "발송 성공", content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = UserLoginReqDto.class),
                    examples = @ExampleObject(value = UserSwaggerEx.EMAIL_VERIFY_CODE)
            )),
            @ApiResponse(
                    responseCode = "400", description = "이메일 전송 오류", content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = CommonException.class),
                    examples = @ExampleObject(value = UserSwaggerEx.MAIL_SERVER_ERROR)
            )),
    })
    ResponseEntity<CommonResDto> newPasswordReq(@AuthenticationPrincipal TokenUserInfo userInfo);




    @Operation(summary = "비밀번호 변경 이메일 인증",
            description = """
               비밀번호 변경용 이메일 인증을 진행합니다.
           
                ## 인증
               - 로그인 해야 인증 가능합니다.
           """)
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200", description = "인증 성공", content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = UserLoginReqDto.class),
                    examples = @ExampleObject(value = UserSwaggerEx.VERIFY_CODE)
            )),
            @ApiResponse(
                    responseCode = "401", description = "인증코드 만료", content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = CommonException.class),
                    examples = @ExampleObject(value = UserSwaggerEx.EXPIRED_AUTH_CODE)
            )),
            @ApiResponse(
                    responseCode = "400", description = "인증 횟수 초과로 인한 30분간 잠김", content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = CommonException.class),
                    examples = @ExampleObject(value = UserSwaggerEx.AUTH_DISALLOWED)
            )),
            @ApiResponse(
                    responseCode = "400", description = "인증코드 불일치", content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = CommonException.class),
                    examples = @ExampleObject(value = UserSwaggerEx.INVALID_AUTH_CODE)
            ))
    })
    ResponseEntity<CommonResDto> verifyNewPassword(@AuthenticationPrincipal TokenUserInfo userInfo
            , @RequestBody UserPwAuthReqDto authResDto);




    @Operation(summary = "비밀번호 변경",
            description = """
               비밀번호 변경을 진행합니다.
           
               ## 인증
               - 로그인 해야 변경 가능합니다.
           """)
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200", description = "변경 성공", content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = UserLoginReqDto.class),
                    examples = @ExampleObject(value = UserSwaggerEx.MODIFY_NEW_PASSWORD)
            )),
            @ApiResponse(
                    responseCode = "401", description = "회원가입 되지 않은 이메일입니다.", content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = CommonException.class),
                    examples = @ExampleObject(value = UserSwaggerEx.ACCOUNT_DISABLED)
            ))
    })
    ResponseEntity<CommonResDto> modifyPassword(@AuthenticationPrincipal TokenUserInfo userInfo
            ,@RequestBody UserPasswordModiReqDto reqDto);


    @Operation(summary = "임시 비밀번호 발급 인증 요청",
            description = """
               임시 비밀번호 발급 이메일 인증을 진행합니다.
           
           """)
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200", description = "발송 성공", content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = UserLoginReqDto.class),
                    examples = @ExampleObject(value = UserSwaggerEx.FORGET_PASSWORD_REQ)
            )),
            @ApiResponse(
                    responseCode = "401", description = "소셜 로그인 회원은 임시 비밀번호 발급이 불가", content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = CommonException.class),
                    examples = @ExampleObject(value = UserSwaggerEx.BAD_REQUEST)
            )),
            @ApiResponse(
                    responseCode = "400", description = "이메일 전송 오류", content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = CommonException.class),
                    examples = @ExampleObject(value = UserSwaggerEx.MAIL_SERVER_ERROR)
            ))

    })
    ResponseEntity<CommonResDto> forgetPasswordReq(@PathVariable String email);



    @Operation(summary = "임시 비밀번호 발급 인증 및 발급",
            description = """
               임시 비밀번호 발급을 진행합니다.
           
           """)
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200", description = "발급 성공", content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = UserLoginReqDto.class),
                    examples = @ExampleObject(value = UserSwaggerEx.NEW_PASSWORD)
            )),
            @ApiResponse(
                    responseCode = "400", description = "이메일 전송 오류", content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = CommonException.class),
                    examples = @ExampleObject(value = UserSwaggerEx.MAIL_SERVER_ERROR)
            ))

    })
    ResponseEntity<CommonResDto> forgetAuth(@RequestBody @Valid UserEmailAuthResDto reqDto);





    @Operation(summary = "마이페이지",
            description = """
               내 회원 정보를 조회합니다.
           
               ## 인증
               - 로그인 해야 조회 가능합니다.
           """)
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200", description = "조회 성공", content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = UserLoginReqDto.class),
                    examples = @ExampleObject(value = UserSwaggerEx.MY_PAGE)
            )),
            @ApiResponse(
                    responseCode = "401", description = "회원가입 되지 않은 이메일입니다.", content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = CommonException.class),
                    examples = @ExampleObject(value = UserSwaggerEx.ACCOUNT_DISABLED)
            ))
    })
    ResponseEntity<CommonResDto> userMyPage(@AuthenticationPrincipal TokenUserInfo userInfo);




    @Operation(summary = "회원 탈퇴",
            description = """
               회원 탈퇴를 진행합니다.
           
               ## 인증
               - 로그인 해야 탈퇴 가능합니다.
           """)
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200", description = "탈퇴 성공", content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = UserLoginReqDto.class),
                    examples = @ExampleObject(value = UserSwaggerEx.RESIGN)
            )),
            @ApiResponse(
                    responseCode = "401", description = "서버 오류", content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = CommonException.class),
                    examples = @ExampleObject(value = UserSwaggerEx.INTERNAL_SERVER_ERROR)
            ))
    })
    ResponseEntity<CommonResDto> resignUser(@AuthenticationPrincipal TokenUserInfo userInfo);




    @Operation(summary = "회원 검색",
            description = """
               쪽지를 보낼 회원을 검색합니다.
           
               ## 인증
               - 로그인 해야 검색 가능합니다.
           """)
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200", description = "검색 성공", content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = UserLoginReqDto.class),
                    examples = @ExampleObject(value = UserSwaggerEx.SEARCH)
            ))
    })
    ResponseEntity<CommonResDto> searchUser(@AuthenticationPrincipal TokenUserInfo userInfo,
                                            @PathVariable String keyword);




    @Operation(summary = "쪽지 목록 조회",
            description = """
               쪽지를 주고받은 채팅방을 조회합니다.
           
               ## 인증
               - 로그인 해야 조회 가능합니다.
           """)
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200", description = "조회 성공", content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = UserLoginReqDto.class),
                    examples = @ExampleObject(value = UserSwaggerEx.MY_CHAT)
            )),
            @ApiResponse(
                    responseCode = "200", description = "조회 성공 (chat이 없는 경우)"
                    , content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = UserLoginReqDto.class),
                    examples = @ExampleObject(value = UserSwaggerEx.FIRST_CHAT)
            )),
    })
    ResponseEntity<CommonResDto> getMyMessageList (@AuthenticationPrincipal TokenUserInfo userInfo);




    @Operation(summary = "채팅방 삭제",
            description = """
               쪽지를 주고받은 채팅방을 삭제합니다.
           
               ## 인증
               - 로그인 해야 삭제 가능합니다.
               - 해당 채팅방의 참여자여야 합니다.
           """)
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200", description = "삭제 성공", content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = UserLoginReqDto.class),
                    examples = @ExampleObject(value = UserSwaggerEx.CLEAR_CHAT)
            )),
            @ApiResponse(
                    responseCode = "404", description = "권한이 없거나, 채팅방이 없습니다."
                    , content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = UserLoginReqDto.class),
                    examples = @ExampleObject(value = UserSwaggerEx.NOT_FOUND)
            )),
    })
    ResponseEntity<CommonResDto> clearUserChat(@AuthenticationPrincipal TokenUserInfo userInfo,
                                               @PathVariable(name = "chatId") Long chatId);




    @Operation(summary = "쪽지 조회",
            description = """
               특정 사용자와 7일간 주고 받은 쪽지 내역을 조회합니다.
           
               ## 인증
               - 로그인 해야 조회 가능합니다.
               - 해당 채팅방의 참여자여야 합니다.
           """)
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200", description = "조회 성공", content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = UserLoginReqDto.class),
                    examples = @ExampleObject(value = UserSwaggerEx.CHAT_LIST)
            )),
            @ApiResponse(
                    responseCode = "404", description = "권한이 없거나, 채팅방이 없습니다."
                    , content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = UserLoginReqDto.class),
                    examples = @ExampleObject(value = UserSwaggerEx.NOT_FOUND)
            )),
    })
    ResponseEntity<CommonResDto> getMyChatList (@AuthenticationPrincipal TokenUserInfo userInfo,
                                                @PathVariable(name = "id") Long chatId);





    @Operation(summary = "쪽지 발송",
            description = """
               특정 사용자에게 쪽지를 전송합니다.
           
               ## 인증
               - 로그인 해야 전송 가능합니다.
           """)
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201", description = "전송 성공", content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = UserLoginReqDto.class),
                    examples = @ExampleObject(value = UserSwaggerEx.SEND_MESSAGE)
            )),
            @ApiResponse(
                    responseCode = "404", description = "전송할 사용자가 없음"
                    , content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = UserLoginReqDto.class),
                    examples = @ExampleObject(value = UserSwaggerEx.NOT_FOUND)
            )),
            @ApiResponse(
                    responseCode = "400", description = "자기 자신에게는 쪽지를 전송할 수 없음"
                    , content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = UserLoginReqDto.class),
                    examples = @ExampleObject(value = UserSwaggerEx.BAD_REQUEST)
            )),
    })
    ResponseEntity<CommonResDto> sendUserMessage(@AuthenticationPrincipal TokenUserInfo userInfo,
                                                 @RequestBody @Valid UserMessageReqDto reqDto);
    


    @Operation(summary = "문의 생성",
            description = """
               문의를 생성합니다.
           
               ## 인증
               - 로그인 해야 생성 가능합니다.
           """)
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201", description = "생성 성공", content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = UserLoginReqDto.class),
                    examples = @ExampleObject(value = UserSwaggerEx.INFORM_CREATE)
            )),
            @ApiResponse(
                    responseCode = "400", description = "유효하지 않은 회원"
                    , content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = UserLoginReqDto.class),
                    examples = @ExampleObject(value = UserSwaggerEx.BAD_REQUEST)
            )),
    })
    ResponseEntity<CommonResDto> createInform(@AuthenticationPrincipal TokenUserInfo userInfo,
                                              @RequestBody @Valid InformReqDto reqDto);



    @Operation(summary = "문의 수정",
            description = """
               문의를 수정합니다.
           
               ## 인증
               - 로그인 해야 수정 가능합니다.
               - 본인이 작성한 문의만 수정 가능합니다.
           """)
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200", description = "수정 성공", content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = UserLoginReqDto.class),
                    examples = @ExampleObject(value = UserSwaggerEx.INFORM_MODIFY)
            )),
            @ApiResponse(
                    responseCode = "400", description = "수정 권한이 없거나 문의가 없음"
                    , content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = UserLoginReqDto.class),
                    examples = @ExampleObject(value = UserSwaggerEx.BAD_REQUEST)
            )),
    })
    ResponseEntity<CommonResDto> modifyInform(@AuthenticationPrincipal TokenUserInfo userInfo,
                                              @RequestBody @Valid InformModiReqDto reqDto);




    @Operation(summary = "문의 삭제",
            description = """
               문의를 삭제합니다.
           
               ## 인증
               - 로그인 해야 삭제 가능합니다.
               - 본인이 작성한 문의만 삭제 가능합니다.
           """)
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200", description = "삭제 성공", content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = UserLoginReqDto.class),
                    examples = @ExampleObject(value = UserSwaggerEx.DELETE_INFORM)
            )),
            @ApiResponse(
                    responseCode = "400", description = "삭제 권한이 없거나 문의가 없음"
                    , content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = UserLoginReqDto.class),
                    examples = @ExampleObject(value = UserSwaggerEx.BAD_REQUEST)
            )),
    })
    ResponseEntity<CommonResDto> deleteInform(@AuthenticationPrincipal TokenUserInfo userInfo,
                                              @PathVariable(name = "id") Long informId);




    @Operation(summary = "내 문의 조회",
            description = """
               내가 작성한 문의를 목록 조회합니다.
           
               ## 인증
               - 로그인 해야 조회 가능합니다.
           """)
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200", description = "조회 성공", content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = UserLoginReqDto.class),
                    examples = @ExampleObject(value = UserSwaggerEx.DELETE_INFORM)
            )),
            @ApiResponse(
                    responseCode = "400", description = "유효하지 않은 사용자임."
                    , content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = UserLoginReqDto.class),
                    examples = @ExampleObject(value = UserSwaggerEx.BAD_REQUEST)
            )),
    })
    ResponseEntity<CommonResDto> getMyInformList (@AuthenticationPrincipal TokenUserInfo userInfo,
                                                  @PathVariable(name = "answered") String answered,
                                                  @RequestParam(value = "page", defaultValue = "0") int page,
                                                  @RequestParam(value = "size", defaultValue = "5") int size,
                                                  @RequestParam(value = "sort", defaultValue = "desc") String sort);




    @Operation(summary = "문의 상세 조회",
            description = """
               내가 작성한 문의를 상세 조회합니다.
           
               ## 인증
               - 로그인 해야 조회 가능합니다.
               - 내가 작성한 문의만 상세 조회 가능합니다.
           """)
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200", description = "조회 성공", content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = UserLoginReqDto.class),
                    examples = @ExampleObject(value = UserSwaggerEx.INFORM_LIST)
            )),
            @ApiResponse(
                    responseCode = "400", description = "유효하지 않은 입력값."
                    , content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = UserLoginReqDto.class),
                    examples = @ExampleObject(value = UserSwaggerEx.BAD_REQUEST)
            )),
    })
    ResponseEntity<CommonResDto> getMyInformDetail(@AuthenticationPrincipal TokenUserInfo userInfo,
                                                   @PathVariable(name = "id") Long informId);




    @Operation(summary = "사용자 신고",
            description = """
               사용자를 신고합니다.
           
               ## 인증
               - 로그인 해야 신고 가능합니다.
           """)
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201", description = "신고 성공", content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = UserLoginReqDto.class),
                    examples = @ExampleObject(value = UserSwaggerEx.REPORT)
            )),
            @ApiResponse(
                    responseCode = "400", description = "유효하지 않은 입력값 또는 사용자"
                    , content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = UserLoginReqDto.class),
                    examples = @ExampleObject(value = UserSwaggerEx.BAD_REQUEST)
            )),
    })
    ResponseEntity<CommonResDto> createReport(@AuthenticationPrincipal TokenUserInfo userInfo,
                                              @RequestBody @Valid ReportSaveReqDto reqDto);



}
