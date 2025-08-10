package com.playdata.boardservice.board.service;

import com.playdata.boardservice.board.dto.*;
import com.playdata.boardservice.board.dto.req.BoardSaveReqDto;
import com.playdata.boardservice.board.dto.req.LikeComCountReqDto;
import com.playdata.boardservice.board.dto.res.*;
import com.playdata.boardservice.board.entity.Board;
import com.playdata.boardservice.board.entity.Category;
import com.playdata.boardservice.board.repository.BoardRepository;
import com.playdata.boardservice.client.MainServiceClient;
import com.playdata.boardservice.common.auth.TokenUserInfo;
import com.playdata.boardservice.common.configs.AwsS3Config;
import com.playdata.boardservice.common.dto.CommonResDto;
import com.playdata.boardservice.common.enumeration.ErrorCode;
import com.playdata.boardservice.common.exception.CommonException;
import com.playdata.boardservice.common.util.HtmlSanitizer;
import com.playdata.boardservice.common.util.ImageValidation;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;

import static com.playdata.boardservice.board.entity.Category.INTRODUCTION;

@Service
@Slf4j
@RequiredArgsConstructor
public class BoardService {

    private final BoardRepository boardRepository;

    private final RedisTemplate<String, String> redisTemplate;

    private final MainServiceClient mainServiceClient;

    // xss 필터 정화 클래스
    private final HtmlSanitizer htmlPolicy;
    private final HtmlSanitizer plainTextPolicy;

    // 이미지 저장 경로
    @Value("${imagePath.thumbnail.url}")
    private String thumbnailImagePath;

    private final AwsS3Config s3Config;

    private List<Category> categoryList = List.of(Category.FREE, INTRODUCTION, Category.QUESTION, Category.REVIEW);


    /**
     *
     * @param boardSaveReqDto
     * @param thumbnailImage
     * @param userInfo
     * @return
     */
    // 게시판 게시물 등록
    @Transactional
    public CommonResDto create(BoardSaveReqDto boardSaveReqDto,
                               MultipartFile thumbnailImage,
                               TokenUserInfo userInfo, Category category) {

        // 썸네일 경로를 저장할 변수
        String savedPath = setThumbnailImage(thumbnailImage);


        // 소개 게시판 썸네일 이미지 필수 검증
        if (category == INTRODUCTION && (savedPath == null || savedPath.isEmpty())) {
            throw new CommonException(ErrorCode.EMPTY_FILE, "이미지는 필수 입니다.");
        }

        // DTO → toEntity() 로 변환 -> DB
        Board entity = boardSaveReqDto.toEntity(userInfo.getUserId(), userInfo.getNickname(), savedPath, htmlPolicy, plainTextPolicy);

        // DB에 저장
        boardRepository.save(entity);

        // 성공 응답 반환
        return new CommonResDto(HttpStatus.CREATED, "게시물 등록 성공", entity.getPostId());

    }

    /**
     *
     * @param modiDto
     * @param thumbnailImage
     * @param userInfo
     * @param category
     * @param postId
     */
    // 게시물 수정 (공통)
    @Transactional
    public void boardModify(BoardModiDto modiDto,
                            MultipartFile thumbnailImage,
                            TokenUserInfo userInfo,
                            Category category,
                            Long postId) {

        // 썸네일 저장 경로 변수
        String savedPath = setThumbnailImage(thumbnailImage);

        // 게시글 조회 (없으면 예외)
        Board board = boardRepository.findByPostIdAndCategoryAndActiveTrue(postId, category)
                .orElseThrow(() -> new CommonException(ErrorCode.DATA_NOT_FOUND, "게시글이 존재하지 않습니다."));

        // 작성자 검증
        if (!board.getUserId().equals(userInfo.getUserId())) {
            throw new CommonException(ErrorCode.UNAUTHORIZED, "작성자만 작성할 수 있습니다.");
        }

        if (category == INTRODUCTION) {

            // 정보 게시판의 카테고리를 설정
        } else if (category == Category.QUESTION || category == Category.REVIEW || category == Category.FREE) {

            if (savedPath != null) {
                // 새 이미지가 있으면 교체
                board.boardModify(modiDto, savedPath, htmlPolicy, plainTextPolicy);
                // DB에 썸네일 이미지가 있는 게시글인데 수정 후 썸네일 이미지를 삭제했다.
            } else if (board.getThumbnailImage() != null && (thumbnailImage == null || thumbnailImage.isEmpty())) {

                // 기존 이미지가 있었고, 새 이미지가 없으면 → 삭제
                // 삭제 시 저장 디렉토리에서도 이미지 삭제
                File oldFile = new File(thumbnailImagePath + File.separator + board.getThumbnailImage());
                if (oldFile.exists()) oldFile.delete();
                // 변경사항 저장
                board.boardModify(modiDto, savedPath, htmlPolicy, plainTextPolicy);
            }
        }
        // 본문 및 썸네일 수정
        board.boardModify(modiDto, savedPath, htmlPolicy, plainTextPolicy);
    }

    /**
     *
     * @param userInfo
     * @param category
     * @param postId
     */
    // 게시물 삭제
    @Transactional
    public void deleteBoard(TokenUserInfo userInfo, Category category, Long postId) {

        // 게시글 조회 (없으면 예외)
        Board board = boardRepository.findByPostIdAndCategoryAndActiveTrue(postId, category)
                .orElseThrow(() -> new CommonException(ErrorCode.DATA_NOT_FOUND));

        // 작성자 검증
        if (!board.getUserId().equals(userInfo.getUserId())) {
            throw new CommonException(ErrorCode.UNAUTHORIZED);
        }

        // active = false로 비활성화 처리
        board.boardDelete();
    }

    /**
     *
     * @param boardSearchDto
     * @param category
     * @param pageable
     * @return
     */
    // 게시판 게시물 목록 조회
    public Page<LikeComResDto> findInformationBoardList(BoardSearchDto boardSearchDto,
                                                        Category category,
                                                        Pageable pageable) {

        // 검색 조건과 페이징 정보를 통해 DB 에서 게시물 목록 조회
        Page<Board> boardList = boardRepository.findList(boardSearchDto,
                category,
                pageable);

        List<LikeComCountReqDto> likeCom = boardList.stream().map(board -> {
            // ReqDto 에서 category, postId 를 뽑아서 List로 만들겠다. (category는 string 변환)
            return new LikeComCountReqDto(String.valueOf(board.getCategory()), board.getPostId());
        }).collect(Collectors.toList());

        List<LikeComCountResDto> listLikeCommentCount = mainServiceClient.getListLikeCommentCount(likeCom);
        List<LikeComResDto> result = boardList.stream().map(inform -> {
            for (LikeComCountResDto likeComCountResDto : listLikeCommentCount) {
                if (inform.getCategory().equals(Category.valueOf(likeComCountResDto.getCategory())) &&
                        inform.getPostId().equals(likeComCountResDto.getContentId())) {
                    return LikeComResDto.fromEntity(inform, likeComCountResDto.getLikeCount(), likeComCountResDto.getCommentCount());
                }
            }
            return null;
        })
                .filter(Objects::nonNull)
                .collect(Collectors.toList());

        Page<LikeComResDto> dtoPage = new PageImpl<>(
                result,
                boardList.getPageable(),
                boardList.getTotalElements()
        );
        // Entity → DTO 변환
//        return informationBoardList.map(InformationBoardListResDto::new);
        return dtoPage;
    }

    /**
     *
     * @param category
     * @param postId
     * @param email
     * @param request
     * @return
     */
    // 게시판 게시물 상세 조회
    public CommonResDto boardDetail(Category category, Long postId, String email, HttpServletRequest request) {

            // 게시물 조회 (null 방지)
            Board board = boardRepository.findByPostIdAndActiveIsTrue(postId)
                    .orElseThrow(() -> new CommonException(ErrorCode.DATA_NOT_FOUND, "찾고있는 게시물이 없습니다."));

            // 사용자 식별 정보 생성
            String ip = extractClientIp(request);
            String userAgent = request.getHeader("User-Agent");

            // Redis 중복 조회 방지를 위한 Key 생성
            String redisKey = generateRedisKey(email, ip, userAgent, category, postId);

            // Redis에 기록이 없으면 첫 조회 → 조회수 증가 처리
            increaseViewCountFirstTime(redisKey, board);

            // 화면단으로 보낼 DTO로 변환
            BoardResDto resDto = board.fromEntity(board);

            return new CommonResDto(HttpStatus.OK, "소개 게시물 조회 성공", resDto);
    }

    /**
     *
     * @return
     */
    // 정보 게시판 메인 최근 게시물 조회
    public List<BoardListResDto> findInformationMainList() {
        List<Board> boardList = boardRepository.findMainList();

        return boardList.stream()
                .map(board -> BoardListResDto.builder()
                        .board(board) // 엔티티 -> DTO 변환
                        .build())
                .collect(Collectors.toList());
    }

    /**
     *
     * @return
     */
    // 정보 게시판 메인 인기 게시물 조회
    public List<BoardListResDto> findPopularInformationBoard() {
        List<Board> board = boardRepository.findPopularList(10, 7); // 최근 7일 상위 10개

        return board.stream()
                .map(boardList -> BoardListResDto.builder()
                        .board(boardList) // 엔티티 → DTO 변환
                        .build())
                .toList();
    }

    /**
     *
     * @param userId
     */
    // 회원 탈퇴 시, 회원의 id를 줌 --> 회원의 모든 게시물 삭제 처리 (active = false)
    @Transactional
    public void deleteUserFindBoard(Long userId) {

        boardRepository.findByUserId(userId)
                .forEach(Board::boardDelete);
    }

    /**
     *
     * @param userId
     * @param encodedNickname
     */
    // 회원이 닉네임 변경 시 --> 회원의 모든 게시물의 nickname값 변경
    @Transactional
    public void modifyUserFindBoard(Long userId, String encodedNickname) {

        boardRepository.findByUserId(userId)
                .forEach(InformationBoard -> InformationBoard.nicknameModify(encodedNickname));
    }

    /**
     *
     * @return
     */
    // 소개 게시판 좋아요순 3개 목록 조회
    public List<IntroductionMainListResDto> findIntroductionMainList() {
        // 1. Feign 으로 좋아요 많은 게시글 리스트 가져오기
        List<LikeComCountResDto> introductionLikeCountList = mainServiceClient.getMainIntroduction();

        // 2. postId만 추출
        List<Long> postIds = introductionLikeCountList.stream()
                .map(LikeComCountResDto::getContentId)
                .collect(Collectors.toList());

        // 3. postId → likeCount/ commentCount 맵핑
        Map<Long, LikeComCountResDto> likeCountMap = introductionLikeCountList.stream()
                .collect(Collectors.toMap(
                        LikeComCountResDto::getContentId,
                        dto -> dto
                ));

        // 4. DB 에서 postId로 게시글 조회
        List<Board> introductionBoards = boardRepository.findPopularIntro(postIds);

        // 5. 게시글 + 좋아요/댓글 정보 조합 후 DTO 변환
        return introductionBoards.stream()
                .limit(3)
                .map(board -> {
                    LikeComCountResDto likeDto = likeCountMap.get(board.getPostId());
                    return IntroductionMainListResDto.builder()
                            .introductionBoard(board)
                            .likeCount(likeDto.getLikeCount())
                            .commentCount(likeDto.getCommentCount())
                            .build();
                })
                .collect(Collectors.toList());
    }

    /**
     *
     * @param userId
     * @param category
     * @param pageable
     * @return
     */
    // 내 게시물 조회
    public CommonResDto findMyPost(Long userId, Category category, Pageable pageable) {

            Page<Board> boardList
                    = boardRepository.findMyPost(userId, category, pageable);

            List<LikeComCountReqDto> likeCom = boardList.stream().map(board -> {
                // ReqDto 에서 category, postId 를 뽑아서 List로 만들겠다. (category는 string 변환)
                return new LikeComCountReqDto(String.valueOf(board.getCategory()), board.getPostId());
            }).collect(Collectors.toList());

            List<LikeComCountResDto> listLikeCommentCount = mainServiceClient.getListLikeCommentCount(likeCom);
            List<LikeComResDto> result = boardList.stream().map(inform -> {
                        for (LikeComCountResDto likeComCountResDto : listLikeCommentCount) {
                            if (inform.getCategory().equals(Category.valueOf(likeComCountResDto.getCategory())) &&
                                    inform.getPostId().equals(likeComCountResDto.getContentId())) {
                                return LikeComResDto.fromEntity(inform, likeComCountResDto.getLikeCount(), likeComCountResDto.getCommentCount());
                            }
                        }
                        return null;
                    })
                    .filter(Objects::nonNull)
                    .collect(Collectors.toList());

            Page<LikeComResDto> pages = new PageImpl<>(
                    result,
                    boardList.getPageable(),
                    boardList.getTotalElements()
            );
            return new CommonResDto(HttpStatus.OK, "내 정보 게시물 모두 찾음", pages);
    }


    /**
     *
     * @param input
     * @return
     */
    // 입력받은 카테고리가 유효하냐 (contains)
    private boolean isValidCategory(Category input) {
        return categoryList.contains(input);
    }

    /**
     *
     * @param thumbnailImage
     * @return
     */
    // 썸네일 이미지 저장 메소드
    private String setThumbnailImage(MultipartFile thumbnailImage) {

        // 썸네일 이미지를 저장할 경로
        String savePath = null;

        // 썸네일 이미지가 있다면 저장
        if (thumbnailImage != null && !thumbnailImage.isEmpty()) {
            try {
                // 이미지 검증
                ImageValidation.validateImageFile(thumbnailImage);

                String originalFilename = thumbnailImage.getOriginalFilename();

                // UUID + 원본 파일명으로 저장 (중복 방지)
                String fileName = UUID.randomUUID() + "_" + originalFilename;

                // s3 버킷에 이미지 저장하고 저장된 경로를 받아오기
                savePath = s3Config.uploadToS3Bucket(thumbnailImage.getBytes(), fileName);
            } catch (IOException e) {
                // 예외 발생 시 에러 로그 남기고 실패 응답
                log.error("썸네일 저장 실패: {}", e.getMessage());
                throw new CommonException(ErrorCode.FILE_SERVER_ERROR, "저장 중 오류 발생");
            }
        }
        return savePath;
    }

    /**
     * 사용자의 실제 IP 주소 추출
     *
     * 프록시, 로드밸런서 등을 통해 들어오는 요청 고려
     *
     * @param request HttpServletRequest 객체
     * @return 추출된 IP 주소 (최종 사용자)
     */
    private String extractClientIp(HttpServletRequest request) {
        // X-Forwarded-For 헤더는 프록시를 통한 실제 사용자 IP를 포함
        String ip = request.getHeader("X-Forwarded-For");
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr(); // 최종 수단으로 실제 접속된 IP 사용
        }

        // 여러 IP가 있을 경우 첫 번째 IP만 사용
        if (ip != null && ip.contains(",")) {
            ip = ip.split(",")[0].trim();
        }

        return ip;
    }

    /**
     * Redis Key를 생성하는 로직
     *
     * 로그인 사용자는 이메일 기준, 비로그인 사용자는 IP + 브라우저 정보로 구분
     *
     * @param email 로그인 사용자 이메일 (nullable)
     * @param ip 사용자 IP 주소
     * @param userAgent 사용자 브라우저 정보
     * @param boardType 게시판 타입 (ex. animal)
     * @param postId 게시물 ID
     * @return 고유 Redis Key
     */
    private String generateRedisKey(String email, String ip, String userAgent, Category boardType, Long postId) {
        StringBuilder key = new StringBuilder("viewCount:");
        key.append(boardType).append(":").append(postId).append(":");

        // 로그인 사용자는 이메일 기반으로 구분
        if (email != null && !email.isEmpty()) {
            key.append("email:").append(email);
        } else {
            // 비로그인 사용자는 IP + UserAgent 해시로 구분
            key.append("ip:").append(ip != null ? ip : "unknown")
                    .append(":ua:").append(userAgent != null ? userAgent.hashCode() : "unknown");
        }

        return key.toString();
    }

    /**
     * Redis를 활용하여 하루 1회만 조회수 증가 처리
     *
     * @param redisKey Redis 중복 조회 방지용 키
     * @param Board 조회 대상 엔티티 (조회수 업데이트 대상)
     */
    private void increaseViewCountFirstTime(String redisKey, Board board) {
        // Redis에 키가 없을 경우만 조회수 증가
        if (!redisTemplate.hasKey(redisKey)) {
            // 현재 조회수를 1 증가시킨 후 저장
            board.viewCountUp(board.getViewCount() + 1);
            boardRepository.save(board);

            // Redis에 키 등록 (value: "1") → 자정 만료
            redisTemplate.opsForValue().set(redisKey, "1");

            // 자정까지 유효하도록 만료 시간 설정
            redisTemplate.expireAt(redisKey,
                    java.util.Date.from(LocalDate.now()
                            .plusDays(1) // 다음날
                            .atStartOfDay(java.time.ZoneId.systemDefault()) // 자정
                            .toInstant()));
        }
    }

}







