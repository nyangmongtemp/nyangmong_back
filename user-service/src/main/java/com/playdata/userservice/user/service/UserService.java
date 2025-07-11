package com.playdata.userservice.user.service;

import com.playdata.userservice.client.AnimalBoardServiceClient;
import com.playdata.userservice.client.BoardServiceClient;
import com.playdata.userservice.client.MainServiceClient;
import com.playdata.userservice.common.auth.JwtTokenProvider;
import com.playdata.userservice.common.auth.TokenUserInfo;
import com.playdata.userservice.common.dto.CommonResDto;
import com.playdata.userservice.common.enumeration.ErrorCode;
import com.playdata.userservice.common.exception.CommonException;
import com.playdata.userservice.common.util.ImageValidation;
import com.playdata.userservice.user.dto.chat.res.UserChatInfoResDto;
import com.playdata.userservice.user.dto.message.req.UserMessageReqDto;
import com.playdata.userservice.user.dto.message.res.UserInfoResDto;
import com.playdata.userservice.user.dto.message.res.UserMessageResDto;
import com.playdata.userservice.user.dto.req.UserInfoModiReqDto;
import com.playdata.userservice.user.dto.req.UserLoginReqDto;
import com.playdata.userservice.user.dto.req.UserPasswordModiReqDto;
import com.playdata.userservice.user.dto.req.UserSaveReqDto;
import com.playdata.userservice.user.dto.res.UserEmailAuthResDto;
import com.playdata.userservice.user.dto.res.UserLoginResDto;
import com.playdata.userservice.user.dto.res.UserMyPageResDto;
import com.playdata.userservice.user.entity.Chat;
import com.playdata.userservice.user.entity.Message;
import com.playdata.userservice.user.entity.User;
import com.playdata.userservice.user.repository.ChatRepository;
import com.playdata.userservice.user.repository.MessageRepository;
import com.playdata.userservice.user.repository.UserRepository;
import jakarta.mail.MessagingException;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {
    
    // 이메일 전송 서비스
    private final MailSenderService mailSenderService;

    // 비밀번호 인코딩용
    private final PasswordEncoder passwordEncoder;

    private final UserRepository userRepository;
    private final MessageRepository messageRepository;
    private final ChatRepository chatRepository;

    // 별명이 변경되거나, 회원 탈퇴 시 모든 좋아요, 댓글, 대댓글의 정보 수정을 위한 페인 클라이언트
    private final MainServiceClient mainClient;
    private final AnimalBoardServiceClient animalClient;
    private final BoardServiceClient boardClient;

    // 로그인 토큰 발급용
    private final JwtTokenProvider jwtTokenProvider;

    // Redis 저장용 redisTemplate
    private final RedisTemplate<String, Object> redisTemplate;

    // Redis key 상수
    // 인증 코드 저장용
    private static final String VERIFICATION_CODE_KEY = "email_verify:code:";

    // 인증 코드 발급 횟수
    private static final String VERIFICATION_ATTEMPT_KEY = "email_verify:attempt:";

    // 인증 코드 발송 금지 상태
    private static final String VERIFICATION_BLOCK_KEY = "email_verify:block:";
    
    // 이미지 저장 경로 --> 추후에 yml에 있는 주소를 s3 주소로 바꿀 것
    @Value("${imagePath.url}")
    private String profileImageSaveUrl;

    /**
     *
     * @param userSaveReqDto  --> userName, nickname, password, email, address, phone
     * @param profileImage
     * @return
     */
    // 회원 가입
    public CommonResDto userCreate(UserSaveReqDto userSaveReqDto, MultipartFile profileImage) {

        String email = userSaveReqDto.getEmail();
        Optional<User> foundByEmail = userRepository.findByEmail(email);
        // 회원가입을 요청한 이메일로 이미 가입한 회원정보가 있는 경우
        if (foundByEmail.isPresent()) {
            // 회원가입 실패
            throw new CommonException(ErrorCode.DUPLICATED_DATA, "이미 존재하는 이메일입니다.");
        }

        // 이메일 유효성 검증을 통과한 경우
        // DB에 저장하기 위해 비밀번호 인코딩
        String password = userSaveReqDto.getPassword();

        String profileImagePath = null;
        // 이미지가 있는 경우 이미지를 지정한 경로에 저장
        if(profileImage != null) {
            // 이미지 진위여부 검증
            ImageValidation.validateImageFile(profileImage);

            profileImagePath = setProfileImage(profileImage);
        }
        else {
            profileImagePath = "default_user.png";
        }
        // DB에 저장을 위해 패스워드 인코딩
        String encodedPassword = passwordEncoder.encode(password);
        // 부가적인 정보를 담아서 User를 DB에 저장
        User createdUser = userSaveReqDto.toEntity(encodedPassword, profileImagePath);
        // DB에 저장
        userRepository.save(createdUser);

        CommonResDto resDto = new CommonResDto(HttpStatus.CREATED, "회원가입에 성공하였습니다", true);
        return resDto;
    }

    /**
     *
     * @param userLoginReqDto  --> email, password
     * @return
     */
    // 로그인 로직
    public CommonResDto login(UserLoginReqDto userLoginReqDto) {

        Optional<User> foundUser = userRepository.findByEmail(userLoginReqDto.getEmail());
        // 로그인 요청을 보낸 이메일이 DB에 존재하지 않는 경우
        if(!foundUser.isPresent()) {
            throw new EntityNotFoundException("회원가입이 되지 않은 이메일입니다.");
        }
        else {
            String pw = foundUser.get().getPassword();
            
            // 탈퇴한 회원인 경우 로그인 실패 처리
            if(!foundUser.get().isActive()) {
                throw new CommonException(ErrorCode.ACCOUNT_DISABLED);
            }
            // 비밀번호가 일치 하지 않는 경우
            if(!passwordEncoder.matches(userLoginReqDto.getPassword(), pw)) {
                throw new CommonException(ErrorCode.INVALID_PASSWORD);
            }
            // 유효한 회원이고, 비밀번호도 일치한 경우
            else{
                User user = foundUser.get();
                // Access Token 발급
                String token = jwtTokenProvider.createToken(user.getEmail(),
                        "USER", user.getNickname(), user.getUserId());
                
                // Refresh Token 발급
                String refreshToken =
                        jwtTokenProvider.createRefreshToken(user.getEmail(),
                                "USER", user.getUserId());
                // redis에 refresh 토큰 저장
                redisTemplate.opsForValue().set(
                        // key
                        "user:refresh:"+user.getUserId(),
                        // value
                        refreshToken,
                        // 만료 시간
                        2,
                        // 2일간 리프레쉬 토큰 저장
                        TimeUnit.DAYS);
                // 로그인 성공
                // token과 email을 화면단으로 리턴
                return new CommonResDto(HttpStatus.OK,
                        "로그인에 성공하였습니다.",
                        new UserLoginResDto(user.getEmail(),user.getNickname(),user.getProfileImage() ,token));
            }
        }

    }

    /**
     * 
     * @param email
     * @return
     */
    // 회원가입 시 이메일의 유효성을 확인하기 위해 인증번호를 발송하는 로직
    // 이메일 인증번호 발송 로직
    public CommonResDto sendVerifyEmailCode(String email) {

        // 차단 상태 확인
        // 이메일 인증번호 발송을 3회 이상한 경우
        if(isBlocked(email)){
            throw new CommonException(ErrorCode.ACCOUNT_LOCKED, "현재 인증 이메일 발송이 차단된 이메일입니다.");
        }
        Optional<User> foundEmail =
                userRepository.findByEmail(email);
        // 이미 존재하는 이메일인 경우 -> 회원가입 불가
        if (foundEmail.isPresent()) {
            // 이미 존재하는 이메일이라는 에러를 발생 -> controller가 이 에러를 처리
            throw new CommonException(ErrorCode.DUPLICATED_DATA, "이미 존재하는 이메일입니다.");
        }
        
        // 이메일로 인증번호 발송
        String authNum = sendEmailAuthCode(email, "CREATE");

        // 나중에 더미데이터를 편하게 넣기 위해서 인증번호를 로그로 남기기 위함
        // 실제 서비스에서는 아래의 return문에 authNum을 삭제해야함.
        return new CommonResDto(HttpStatus.OK, "회원가입 인증코드가 이메일로 발송되었습니다.", authNum);

    }

    /**
     * 
     * @param authResDto  --> email, authCode (인증번호)
     * @return
     */
    // 이메일로 발송된 인증코드 검증 로직
    public CommonResDto verifyEmailCode(UserEmailAuthResDto authResDto) {
        
        String email = authResDto.getEmail();
        String authCode = authResDto.getAuthCode();

        // redis에 저장된 인증 코드 조회
        String key = VERIFICATION_CODE_KEY + email;
        Object foundCode = redisTemplate.opsForValue().get(key);
        // 인증 코드 유효시간이 만료된 경우
        if(foundCode == null) {
            throw new CommonException(ErrorCode.BAD_REQUEST);
            //throw new CommonException(ErrorCode.EXPIRED_AUTH_CODE);
        }

        // 인증 시도 횟수 증가
        int attemptCount = incrementAttemptCount(email);

        // 조회한 코드와 사용자가 입력한 코드가 일치한 지 검증
        if(!foundCode.toString().equals(authCode)) {
            // 인증 코드를 틀린 경우
            if(attemptCount >= 3){
                // 최대 시도 횟수 초과 시 해당 이메일 인증 차단
                blockUser(email);
                throw new CommonException(ErrorCode.BAD_REQUEST);
                // throw new CommonException(ErrorCode.ACCOUNT_LOCKED, "현재 인증 이메일 발송이 차단된 이메일입니다.");
            }
            // 인증 횟수 차감하여 프론트로 메시지 전송
            int remainingAttempt = 3 - attemptCount;
            throw new CommonException(ErrorCode.BAD_REQUEST);
            /*throw new CommonException(ErrorCode.INVALID_AUTH_CODE,
                    (String.format("인증코드가 틀렸습니다. 인증 기회는 %d회 남았습니다.", remainingAttempt)));*/
        }

        log.info("이메일 인증 성공!, email: {}", email);

        // 인증 완료 했기 때문에, redis에 있는 인증 관련 데이터를 삭제하자.
        redisTemplate.delete(key);

        return new CommonResDto(HttpStatus.OK, "인증되었습니다.", true);
    }

    /**
     *
     * @param userInfo
     * @param modiDto  --> nickname, phone, address
     * @param profileImage
     * @return
     */
    // 프사, 닉네임, 주소, 전화번호를 변경하는 로직
    public boolean modiUserCommonInfo(TokenUserInfo userInfo, UserInfoModiReqDto modiDto, MultipartFile profileImage) {

        Optional<User> byId = userRepository.findById(userInfo.getUserId());
        if(!byId.isPresent() || !byId.get().isActive()) {
            throw new CommonException(ErrorCode.UNKNOWN_HOST, "변경을 진행할 회원이 존재하지 않습니다.");
        }
        User foundUser = byId.get();
        String newProfileImage = null;
        // 변경할 프로필 이미지가 왔다면, 새로 저장
        if(profileImage != null) {
            // 이미지 진위 여부 검증
            ImageValidation.validateImageFile(profileImage);
            // 이미지를 원하는 디렉토리로 UUID를 붙여서 저장
            newProfileImage = setProfileImage(profileImage);
            // main-service로 댓글, 대댓글의 profileImage를 변경된 이미지로 변경 요청
            ResponseEntity<?> res = mainClient.modifyProfileImage(userInfo.getUserId(), newProfileImage);

            if(res.getStatusCode() != HttpStatus.OK) {
                throw new CommonException(ErrorCode.INTERNAL_SERVER_ERROR);
            }
        }
        // nickname을 변경하는 경우
        if(newProfileImage != null) {
            // feign 요청 시 한글을 PathVariable로 쓰지 못해서, 인코딩 변환
            String encodedNickname = URLEncoder.encode(modiDto.getNickname(), StandardCharsets.UTF_8);
            // 댓글, 대댓글에 nickname 값을 변경시키기 위한 feign 요청
            ResponseEntity<?> response
                    = mainClient.modifyNickname(userInfo.getUserId(), encodedNickname);
            ResponseEntity<?> res1 = animalClient.modifyNickname(userInfo.getUserId(), encodedNickname);
            ResponseEntity<?> res2 = boardClient.modifyNickname(userInfo.getUserId(), encodedNickname);
            // 댓글, 대댓글의 nickname 값 수정 중 오류 발생
            // 또는 다른 게시판의 nickname값 수정 중 오류 발생
            if(response.getStatusCode() != HttpStatus.OK
                    || res1.getStatusCode() != HttpStatus.OK
                    || res2.getStatusCode() != HttpStatus.OK) {
                throw new CommonException(ErrorCode.INTERNAL_SERVER_ERROR);
            }
        }
        
        // DB로 변경된 값을 저장
        foundUser.modifyCommonUserInfo(modiDto, newProfileImage);
        userRepository.save(foundUser);
        return true;
    }

    /**
     * 
     * @param newEmail
     * @param userInfo
     * @return
     */
    // 이메일 변경을 요청하여 검사 후 인증 이메일 전송 로직
    public CommonResDto modiUserEmail(String newEmail, TokenUserInfo userInfo) {

        Optional<User> byEmail = userRepository.findByEmail(newEmail);
        // 이메일 변경 요청을 보낸 사용자가 DB에 이미 존재하는 경우
        if(byEmail.isPresent()) {
            throw new CommonException(ErrorCode.DUPLICATED_DATA, "이미 존재하는 이메일입니다.");
        }
        String authCode = sendEmailAuthCode(newEmail, "MODIFY");

        // 나중에 더미데이터를 편하게 넣기 위해서 인증번호를 로그로 남기기 위함
        // 실제 서비스에서는 아래의 return문에 authNum을 삭제해야함.
        return new CommonResDto(HttpStatus.OK, "인증코드가 새로운 이메일로 발송되었습니다.", authCode);
    }

    /**
     * 
     * @param authResDto  --> email, authCode (인증코드)
     * @param userInfo
     * @return
     */
    // 이메일 변경 요청의 인증 여부를 확인하는 메소드
    // 완료되면 바로 이메일을 변경함.
    public CommonResDto verifyUserNewEmail(UserEmailAuthResDto authResDto, TokenUserInfo userInfo) {
        
        // 이메일 인증 처리
        CommonResDto resDto = verifyEmailCode(authResDto);
        
        Optional<User> foundUser = userRepository.findById(userInfo.getUserId());
        // 이메일을 변경할 사용자가 존재 및 활성화 되었는지 확인
        if(!foundUser.isPresent() || !foundUser.get().isActive()) {
            throw new CommonException(ErrorCode.UNKNOWN_HOST, "변경을 진행할 회원이 존재하지 않습니다.");
        }
        // 이메일 변경 요청 사용자의 유효성 확인 후
        // 해당 사용자의 이메일 변경 및 저장
        User user = foundUser.get();
        user.modifyEmail(authResDto.getEmail());
        userRepository.save(user);

        return resDto;
    }

    /**
     *
     * @param email
     * @return
     */
    // 비밀번호 변경 요청 인증 코드를 발송해주는 로직
    public CommonResDto sendEmailAuthCodeNewPw(String email) {

        Optional<User> byEmail = userRepository.findByEmail(email);
        // 비밀번호 변경 요청을 보낸 유저가 DB에 없는 경우
        if(!byEmail.isPresent() || !byEmail.get().isActive()) {
            throw new CommonException(ErrorCode.UNKNOWN_HOST, "변경을 진행할 회원이 존재하지 않습니다.");
        }
        // 이메일 전송
        String code = sendEmailAuthCode(email, "MODIFY");

        // 실제 배포 환경에서는 인증 코드는 빼고 리턴해야 함.
        // 개발 단계에서는 인증코드가 이메일로 전송된 인증코드와 일치하는 지 보기 위해서 리턴함.
        return new CommonResDto(HttpStatus.OK, "인증 코드가 이메일로 전송되었습니다.", code);
    }

    /**
     *
     * @param userId
     * @param reqDto  --> password
     * @return
     */
    // 비밀번호 변경 요청이 인증된 경우
    // 실제로 비밀번호를 변경해주는 로직
    public CommonResDto modifyNewPassword(Long userId, UserPasswordModiReqDto reqDto) {

        Optional<User> byEmail = userRepository.findById(userId);
        // 비밀번호를 변경하려는 유저가 DB에 없는 경우
        if(!byEmail.isPresent() || !byEmail.get().isActive()) {
            throw new CommonException(ErrorCode.UNKNOWN_HOST, "변경을 진행할 회원이 존재하지 않습니다.");
        }
        
        User user = byEmail.get();
        // 변경할 비밀번호를 인코딩
        String newEncodedPassword = passwordEncoder.encode(reqDto.getPassword());
        // DB에 변경된 비밀번호로 저장
        user.modifyPassword(newEncodedPassword);
        userRepository.save(user);

        return new CommonResDto(HttpStatus.OK, "비밀번호가 변경되었습니다. 다시 로그인 해주세요", true);
    }

    /**
     *
     * @param userId
     * @return
     */
    // 마이페이지 요청 -> 회원의 이메일, 전화번호, 프로필 이미지, 닉네임, 주소를 리턴
    public CommonResDto getMyPage(Long userId) {

        Optional<User> byEmail = userRepository.findById(userId);
        // 정보를 조회할 회원이 존재하지 않거나, 탈퇴한 회원인 경우
        if(!byEmail.isPresent() || !byEmail.get().isActive()) {
            throw new CommonException(ErrorCode.UNKNOWN_HOST, "회원이 존재하지 않습니다.");
        }
        User foundUser = byEmail.get();
        // 화면단으로 전송할 데이터를 담은 dto 변환
        UserMyPageResDto resDto = foundUser.toUserMyPageResDto();

        return new CommonResDto(HttpStatus.OK, "해당 유저의 정보를 찾음.", resDto);
    }

    /**
     *
     * @param userId
     * @return
     */
    // 회원 탈퇴를 담당하는 로직
    public CommonResDto resignUser(Long userId) {

        Optional<User> targetUser = userRepository.findById(userId);
        // 탈퇴를 진행할 사용자가 없거나, 이미 사용자가 탈퇴를 진행한 경우
        if(!targetUser.isPresent() || !targetUser.get().isActive()) {
            throw new CommonException(ErrorCode.UNKNOWN_HOST, "탈퇴를 진행할 회원이 존재하지 않습니다.");
        }
        User user = targetUser.get();
        // 회원의 비활성화 처리
        user.resignUser();
        // main-service로  회원이 작성한 댓글, 대댓글을 모두 비활성화 요청
        ResponseEntity<?> response = mainClient.deleteUser(userId);
        // animal-board-service로 요청
        ResponseEntity<?> res1 = animalClient.deleteUser(userId);
        // board-service로 요청
        ResponseEntity<?> res2 = boardClient.deleteUser(userId);
        
        // 댓글, 대댓글 비활성화 처리 중 오류 발생
        // 모든 게시물 비활성화 처리 중 오류 발생
        if(response.getStatusCode() != HttpStatus.OK
        || res1.getStatusCode() != HttpStatus.OK
        || res2.getStatusCode() != HttpStatus.OK) {
            throw new CommonException(ErrorCode.INTERNAL_SERVER_ERROR);
        }

        // 사용자의 모든 채팅방 삭제 필요
        Optional<List<Chat>> myActiveChat = chatRepository.findMyActiveChat(userId);
        // 사용자의 활성화된 채팅방이 있을때만, 비활성화 처리
        myActiveChat.ifPresent(chats -> chats.stream().forEach(Chat::deleteChat));

        // 회원의 비활성화 처리 DB로 저장
        userRepository.save(user);
        return new CommonResDto(HttpStatus.OK, "회원 탈퇴가 정상적으로 진행되었습니다.", null);
    }

    // 비밀번호 분실 시 임시비밀번호 발급을 위한 인증코드 발급 로직
    public CommonResDto forgetPasswordReq(String email) {

        Optional<User> byEmail = userRepository.findByEmail(email);
        // 임시 비밀번호 발급을 요청한 사용자의 이메일이 유효하지 않은 경우
        if(!byEmail.isPresent() || !byEmail.get().isActive()) {
            throw new CommonException(ErrorCode.NOT_FOUND);
        }

        String authCode = sendEmailAuthCode(email, "FORGET");

        return new CommonResDto(HttpStatus.OK, "인증 이메일이 전송됨", authCode);
    }

    // 임시 비밀번호를 발급 후 저장 및 이메일로 전송해주는 로직
    public CommonResDto authCodeAndRePw(UserEmailAuthResDto reqDto) {

        Optional<User> byEmail = userRepository.findByEmail(reqDto.getEmail());
        // 인증코드를 받은 이메일이 유효한 이메일인지 확인하는 메소드
        if(!byEmail.isPresent() || !byEmail.get().isActive()) {
            throw new CommonException(ErrorCode.BAD_REQUEST);
        }
        // 인증코드를 확인하는 메소드
        verifyEmailCode(reqDto);
        // 임시 비밀번호 발급
        String newPw = sendEmailAuthCode(reqDto.getEmail(), "NEW");
        log.info("임시 비밀번호는: " + newPw);
        // 신규 비밀번호를 인코딩 후, DB에 저장
        String encodedNewPW = passwordEncoder.encode(newPw);
        byEmail.get().modifyPassword(encodedNewPW);
        userRepository.save(byEmail.get());

        return new CommonResDto(HttpStatus.OK, "임시 비밀번호 발급 완료", newPw);
    }

    /**
     *
     * @param userId
     * @return
     */
    // 댓글, 대댓글 생성 시 main-service로 profileImage를 전송해주는 로직
    public String getProfileImage(Long userId) {

        Optional<User> foundUser = userRepository.findById(userId);
        // 이미지를 요청한 회원이 존재하지 않거나, 탈퇴한 회원인 경우
        if(!foundUser.isPresent() || !foundUser.get().isActive()) {
            throw new CommonException(ErrorCode.UNKNOWN_HOST, "회원이 존재하지 않습니다.");
        }
        // 유효한 회원인 경우, 이미지를 main-service로 전달
        User user = foundUser.get();
        return user.getProfileImage();
    }

    // 쪽지를 보내기 위한 사용자 검색에서 사용하는 서비스입니다.
    public CommonResDto searchUser(String keyword) {

        // keyword를 통한 쪽지를 보낼 수 있는 사용자 조회
        Optional<List<User>> userList = userRepository.findByKeyword(keyword);
        // 검색 결과가 없는 경우
        List<User> foundUsers = userList.orElseThrow(() -> new CommonException(ErrorCode.NOT_FOUND));
        // 검색 결과를 dto로 변환해서 리턴
        List<UserInfoResDto> resDto = foundUsers.stream().map(User::toMessageInfo
        ).collect(Collectors.toList());

        return new CommonResDto(HttpStatus.OK, "검색된 회원 목록 조회", resDto);
    }

    /**
     *
     * @param email
     * @return
     */
    // refresh Token을 통한 Access Token을 재발급 하는 로직
    public CommonResDto reProvideToken(String email) {

        Optional<User> foundUser = userRepository.findByEmail(email);
        // 토큰 발급을 요청한 회원이 유효하지 않은 회원인 경우
        if(!foundUser.isPresent() || !foundUser.get().isActive()) {
            throw new CommonException(ErrorCode.UNKNOWN_HOST, "토큰 발급을 진행할 회원이 존재하지 않습니다.");
        }
        User user = foundUser.get();
        // redis에 해당 유저의 refresh token 조회
        Object obj = redisTemplate.opsForValue().get("user:refresh:" + user.getUserId());

        // refresh 토큰이 만료된 경우
        if(obj == null){
            throw new CommonException(ErrorCode.SESSION_EXPIRED, "다시 로그인을 진행해주세요.");
        }
        // refresh 토큰이 유효한 경우
        // 새로운 Access Token 재발급
        String token
                = jwtTokenProvider.createToken(user.getEmail(), "USER", user.getNickname(), user.getUserId());

        return new CommonResDto(HttpStatus.OK, "토큰 재발급이 이루어졌습니다."
                , new UserLoginResDto(user.getEmail(), user.getNickname(), user.getProfileImage(), token));
    }

    // 쪽지 발송 로직
    public CommonResDto sendMessage(Long senderId, String requestNickname, UserMessageReqDto reqDto) {

        // 자기 자신에게 채팅방 여는 것은 방지
        if(senderId == reqDto.getReceiverId()) {
            throw new CommonException(ErrorCode.BAD_REQUEST);
        }
        String receiverNickname = findNicknameByUserID(reqDto.getReceiverId());
        Optional<Chat> foundChat = chatRepository.findByUserId(senderId, reqDto.getReceiverId());
        // 처음 채팅을 시작하는 거라면
        Chat chat = null;
        if(!foundChat.isPresent() || !foundChat.get().isActive()) {
            // 채팅방 생성
            chat = new Chat(senderId, reqDto.getReceiverId());
        }
        else {
            // 기존에 생성된 채팅방이 있는 경우
            chat = foundChat.get();
        }
        chatRepository.save(chat);
        // 새로운 메시지 생성
        Message message = new Message(senderId, reqDto.getReceiverId(), reqDto.getContent(), chat);

        UserMessageResDto resDto = messageRepository.save(message)
                .fromEntity(receiverNickname, requestNickname, requestNickname);

        return new CommonResDto(HttpStatus.CREATED, "메시지 전송됨", resDto);
    }

    // 채팅방 삭제
    public CommonResDto clearChat(Long userId, Long chatId) {

        Optional<Chat> byId = chatRepository.findById(chatId);
        // 삭제하려는 채팅방이 유효하지 않은 경우
        if(!byId.isPresent() || !byId.get().isActive()) {
            throw new CommonException(ErrorCode.NOT_FOUND);
        }
        Chat chat = byId.get();
        if(chat.getUserId1() != userId && chat.getUserId2() != userId) {
            throw new CommonException(ErrorCode.NOT_FOUND);
        }
        byId.get().deleteChat();
        chatRepository.save(chat);
        return new CommonResDto(HttpStatus.OK, "해당 채팅방 삭제됨.", true);
    }

    // 내 채팅방 목록 조회
    public CommonResDto findMyActiveChat(Long userId, String requestNickname) {

        Optional<List<Chat>> myActiveChat = chatRepository.findMyActiveChat(userId);
        if(!myActiveChat.isPresent()) {
            return new CommonResDto(HttpStatus.OK, "생성된 채팅방이 없습니다.", null);
        }
        List<UserChatInfoResDto> resDtos = myActiveChat.get().stream().map(chat -> {
                    String nickname1 = findNicknameByUserID(chat.getUserId1());
                    String nickname2 = findNicknameByUserID(chat.getUserId2());
                    UserMessageResDto messageDto = messageRepository
                            .findLastByChatId(chat.getChatId()).fromEntity(nickname1, nickname2, requestNickname);
                    return chat.toUserChatInfoResDto(nickname1, nickname2, requestNickname, messageDto);
                })
                .collect(Collectors.toList());

        return new CommonResDto(HttpStatus.OK, "사용자의 채팅방 모두 조회됨.", resDtos);
    }

    // 특정 채팅방의 채팅 내용 조회  --> 7일 간 생성된 것만
    @Transactional
    public CommonResDto getMyChatMessages(Long userId, String requestNickname, Long chatId) {

        Optional<Chat> byId = chatRepository.findById(chatId);
        // 조회하려는 채팅방이 유효하지 않는 경우
        if(!byId.isPresent() || !byId.get().isActive()) {
            throw new CommonException(ErrorCode.NOT_FOUND);
        }
        Chat chat = byId.get();
        // 조회하려는 채팅방이 내가 볼 수 있는 채팅방이 아닌 경우
        if(chat.getUserId1() != userId && chat.getUserId2() != userId) {
            throw new CommonException(ErrorCode.NOT_FOUND);
        }
        // 채팅방의 사용자의 nickname 조회
        String n1 = findNicknameByUserID(chat.getUserId1());
        String n2 = findNicknameByUserID(chat.getUserId2());
        LocalDateTime sevenDaysAgo = LocalDateTime.now().minusDays(7);
        Optional<List<Message>> messageList = messageRepository.findByChatId(chatId, sevenDaysAgo);
        if(!messageList.isPresent()) {
            return null;
        }
        List<UserMessageResDto> resDto = messageList.get().stream().map(message -> {
            return message
                    // 내가 읽지 않은 메시지는 읽음 처리
                    .setRead(userId)
                    .fromEntity(n1, n2, requestNickname);
        }).collect(Collectors.toList());

        return new CommonResDto(HttpStatus.OK, "채팅방의 7일간 메시지 조회됨.", resDto);
    }

    // fegin용 이메일을 통해 userId를 리턴하는 메소드입니다.
    public Long findByEmail(String email) {
        Optional<User> byEmail = userRepository.findByEmail(email);
        if(!byEmail.isPresent() || !byEmail.get().isActive()) {
            throw new CommonException(ErrorCode.NOT_FOUND);
        }
        return byEmail.get().getUserId();
    }

///////////  공통적으로 사용하는 공통 로직들입니다.


    /**
     *
     * @param imageFile
     * @return
     */
    // 프로필 이미지를 저장하는 로직
    private String setProfileImage(MultipartFile imageFile) {
        String profileImagePath = null;

        // profile image 저장 경로
        if (imageFile != null && !imageFile.isEmpty()) {
            try {
                // 로컬 저장 경로 (예: C:/uploads/profile 또는 /home/user/images/profile)
                String originalFilename = imageFile.getOriginalFilename();
                String fileName = UUID.randomUUID() + "_" + originalFilename;

                File dir = new File(profileImageSaveUrl);
                if (!dir.exists()) dir.mkdirs(); // 디렉토리 없으면 생성

                File dest = new File(profileImageSaveUrl, fileName);
                imageFile.transferTo(dest);

                profileImagePath = fileName; // 저장된 상대 경로만(UUID + 원 파일 이름) DB에 넣음
            } catch (IOException e) {
                // 저장 실패 처리
                e.printStackTrace();
                throw new CommonException(ErrorCode.FILE_SERVER_ERROR);
            }
        }
        return profileImagePath;
    }

    /**
     *
     * @param email
     * @param occasion  --> 회원가입 또는 개인정보 변경 여부
     * @return
     */
    // 인증코드 전송 및 redis에 해당 키값 저장을 담당하는 메소드
    private String sendEmailAuthCode(String email, String occasion) {
        String authNum;
        // 이메일 전송만을 담당하는 객체를 이용해서 이메일 로직 작성.
        try {
            // 회원가입용 이메일 전송
            if (occasion.equals("CREATE")) {
                authNum = mailSenderService.joinMain(email);
            }
            // 개인정보 변경용 이메일 전송
            else if(occasion.equals("MODIFY")) {
                authNum = mailSenderService.sendAuthCode(email);
            }
            else if (occasion.equals("FORGET")) {
                authNum = mailSenderService.sendAuthCodeForget(email);
            }
            else if (occasion.equals("NEW")) {
                authNum = mailSenderService.sendNewPasswordForget(email);
                log.info(authNum);
            } else {
                authNum = "";
                throw new CommonException(ErrorCode.BAD_REQUEST);
            }
        } catch (MessagingException e) {
            log.info(e.getMessage());
            throw new CommonException(ErrorCode.FILE_SERVER_ERROR);
        }

        // 인증 코드를 redis에 저장하자
        String key = VERIFICATION_CODE_KEY + email;
        // 인증코드의 유효 시간은 5분으로 지정
        redisTemplate.opsForValue().set(key, authNum, Duration.ofMinutes(5));
        return authNum;
    }

    /**
     *
     * @param email
     * @return
     */
    // 인증번호를 3회 이상 발송시킨 이메일인지 확인 여부
    private boolean isBlocked(String email) {
        // redis key 생성
        String key = VERIFICATION_BLOCK_KEY + email;
        // 현재 해당 이메일의 값이 존재하는지 -> 있으면 block 상태
        return redisTemplate.hasKey(key);
    }

    /**
     *
     * @param email
     */
    // 인증번호를 30분동안 3회이상 발송하지 못하게 하기 위한 로직
    private void blockUser(String email) {
        // redis key 생성
        String key = VERIFICATION_BLOCK_KEY + email;
        // 30분동안 해당 이메일의 key 값이 살아있도록 설정
        redisTemplate.opsForValue().set(key, "blocked", Duration.ofMinutes(30));
    }

    /**
     *
     * @param email
     * @return
     */
    // 이메일 발송을 요청하게 되면, redis에 있는 발송횟수 값을 하나 늘림.
    private int incrementAttemptCount(String email) {

        // redis key 생성
        String key = VERIFICATION_ATTEMPT_KEY + email;
        // redis에 있는 해당 email의 값 확인
        Object obj = redisTemplate.opsForValue().get(key);

        // 발송 횟수를 하나 늘려서 다시 redis에 저장
        int count = (obj != null) ? Integer.parseInt(obj.toString()) + 1 : 1;
        redisTemplate.opsForValue().set(key, String.valueOf(count), Duration.ofMinutes(1));

        return count;
    }

    // 사용자의 id를 통해 nickname을 리턴하는 메소드
    private String findNicknameByUserID(Long userId) {
        Optional<User> byId = userRepository.findById(userId);
        // 회원이 없는 경우
        if(!byId.isPresent() || !byId.get().isActive()) {
            throw new CommonException(ErrorCode.NOT_FOUND);
        }
        return byId.get().getNickname();
    }

}
