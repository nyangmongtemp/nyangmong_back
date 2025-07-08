package com.playdata.boardservice.board.service;

import com.playdata.boardservice.board.dto.*;
import com.playdata.boardservice.board.entity.Category;
import com.playdata.boardservice.board.entity.InformationBoard;
import com.playdata.boardservice.board.entity.IntroductionBoard;
import com.playdata.boardservice.board.repository.InformationBoardRepository;
import com.playdata.boardservice.board.repository.IntroductionBoardRepository;
import com.playdata.boardservice.common.auth.TokenUserInfo;
import com.playdata.boardservice.common.dto.CommonResDto;
import com.playdata.boardservice.common.enumeration.ErrorCode;
import com.playdata.boardservice.common.exception.CommonException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
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
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class BoardService {

    private final InformationBoardRepository informationBoardRepository;
    private final IntroductionBoardRepository introductionBoardRepository;
    private final RedisTemplate<String, String> redisTemplate;

    // 이미지 저장 경로
    @Value("${imagePath.thumbnail.url}")
    private String thumbnailImagePath;

    private List<Category> categoryList = List.of(Category.FREEDOM, Category.INTRODUCTION, Category.QUESTION, Category.REVIEW);


    // 질문, 후기, 자유 게시판 게시물 등록
    @Transactional
    public CommonResDto informationCreate(InformationBoardSaveReqDto informationSaveDto,
                                          MultipartFile thumbnailImage,
                                          TokenUserInfo userInfo) {

        if (!isValidCategory(informationSaveDto.getCategory())){
            throw new CommonException(ErrorCode.BAD_REQUEST);
        }

        Category cate = informationSaveDto.getCategory();

        // 카테고리 값 가져와서 null, 빈문자열 체크 후 값이 있다면 대문자로 변환
        String category = (informationSaveDto.getCategory() != null)
                ? informationSaveDto.getCategory().name().toUpperCase()
                : "default";

        // 썸네일 경로를 저장할 변수
        String savedPath = setThumbnailImage(thumbnailImage);

        // DTO → toEntity() 로 변환 -> DB
        InformationBoard entity = informationSaveDto.toEntity(userInfo.getUserId(), userInfo.getNickname(), savedPath);
        informationBoardRepository.save(entity);

        // 성공 응답 반환
        return new CommonResDto(HttpStatus.CREATED, "게시물 등록 성공", entity.getPostId());

    }

    // 소개 게시판 게시물 등록
    @Transactional
    public CommonResDto introductionCreate(IntroductionBoardSaveReqDto introductionSaveDto,
                                           MultipartFile thumbnailImage,
                                           TokenUserInfo userInfo) {

        if (thumbnailImage == null || thumbnailImage.isEmpty()) {
            throw new CommonException(ErrorCode.EMPTY_FILE, "썸네일 이미지는 필수 입니다.");
        }

        // 썸네일 경로를 저장할 변수
        String savedPath = setThumbnailImage(thumbnailImage);


        // DTO → Entity 변환 후 저장
        IntroductionBoard entity = introductionSaveDto.toEntity(userInfo.getUserId(), userInfo.getNickname(), savedPath);
        introductionBoardRepository.save(entity);

        // 성공 응답 반환
        return new CommonResDto(HttpStatus.CREATED, "게시물 등록 성공", entity.getPostId());

    }

    // 게시물 수정 (공통)
    @Transactional
    public void boardModify(BoardModiDto modiDto,
                            MultipartFile thumbnailImage,
                            TokenUserInfo userInfo,
                            Category category,
                            Long postId) {

        // 썸네일 저장 경로 변수
        String savedPath = setThumbnailImage(thumbnailImage);

        // 소개 게시판은 INTRODUCTION enum 으로 단일 구분
        if (category == Category.INTRODUCTION) {
            // 게시글 조회 (없으면 예외)
            IntroductionBoard board = introductionBoardRepository.findById(postId)
                    .orElseThrow(() -> new CommonException(ErrorCode.DATA_NOT_FOUND));

            // 작성자 검증 (자기 글만 수정 가능)
            if (!board.getUserId().equals(userInfo.getUserId())) {
                throw new CommonException(ErrorCode.UNAUTHORIZED);
            }

            // 썸네일은 필수이므로 없으면 예외
            if (savedPath == null) {
                throw new CommonException(ErrorCode.EMPTY_FILE, "썸네일 이미지를 첨부해주세요.");
            }

            // 본문 및 썸네일 수정
            board.boardModify(modiDto, savedPath);

            // 수정된 게시글 저장
            introductionBoardRepository.save(board);

            // 정보 게시판의 카테고리를 설정
        } else if (category == Category.QUESTION || category == Category.REVIEW || category == Category.FREEDOM) {

            // 게시글 조회 (없으면 예외)
            InformationBoard board = informationBoardRepository.findById(postId)
                    .orElseThrow(() -> new CommonException(ErrorCode.DATA_NOT_FOUND, "게시글이 존재하지 않습니다."));

            // 작성자 검증
            if (!board.getUserId().equals(userInfo.getUserId())) {
                throw new CommonException(ErrorCode.UNAUTHORIZED, "작성자만 작성할 수 있습니다.");
            }

            // content 수정
            board.boardModify(modiDto, savedPath);

            if (savedPath != null) {
                // 새 이미지가 있으면 교체
                board.boardModify(modiDto, savedPath);
                // DB에 썸네일 이미지가 있는 게시글인데 수정 후 썸네일 이미지를 삭제했다.
            } else if (board.getThumbnailImage() != null && (thumbnailImage == null || thumbnailImage.isEmpty())) {

                // 기존 이미지가 있었고, 새 이미지가 없으면 → 삭제
                // 삭제 시 저장 디렉토리에서도 이미지 삭제
                File oldFile = new File(thumbnailImagePath + File.separator + board.getThumbnailImage());
                if (oldFile.exists()) oldFile.delete();

                board.boardModify(modiDto, savedPath);
            }

            // 수정된 게시글 저장
            informationBoardRepository.save(board);

        } else {
            // 그 외 잘못된 카테고리는 예외
            throw new CommonException(ErrorCode.DATA_NOT_FOUND, "지원하지 않는 카테고리 입니다.");
        }
    }

    // 게시물 삭제 (공통)
    @Transactional
    public void deleteBoard(TokenUserInfo userInfo, Category category, Long postId) {
        // 소개 게시판인 경우
        if (category == Category.INTRODUCTION) {

            // 게시글 조회 (없으면 예외)
            IntroductionBoard board = introductionBoardRepository.findById(postId)
                    .orElseThrow(() -> new CommonException(ErrorCode.DATA_NOT_FOUND));

            // 작성자 검증 (다른 사람이 삭제 요청하면 차단)
            if (!board.getUserId().equals(userInfo.getUserId())) {
                throw new CommonException(ErrorCode.UNAUTHORIZED);
            }

            // 실제 삭제하지 않고 active 값을 false 로 변경 (소프트 삭제)
            board.boardDelete();

            // 변경된 상태를 DB에 저장
            introductionBoardRepository.save(board);
        }

        // 질문/후기/자유 게시판인 경우
        else if (category == Category.QUESTION || category == Category.REVIEW || category == Category.FREEDOM) {
            // 게시글 조회 (없으면 예외)
            InformationBoard board = informationBoardRepository.findById(postId)
                    .orElseThrow(() -> new CommonException(ErrorCode.DATA_NOT_FOUND));

            // 작성자 검증
            if (!board.getUserId().equals(userInfo.getUserId())) {
                throw new CommonException(ErrorCode.UNAUTHORIZED);
            }

            // active = false로 비활성화 처리
            board.boardDelete();

            // DB에 저장
            informationBoardRepository.save(board);
        } else { // 지원하지 않는 카테고리
            throw new CommonException(ErrorCode.DATA_NOT_FOUND, "지원하지 않은 카테고리 입니다.");
        }
    }

    // 정보 게시판 게시물 목록 조회
    public Page<InformationBoardListResDto> findInformationBoardList(BoardSearchDto boardSearchDto,
                                                                     Category category,
                                                                     Pageable pageable) {

        // 검색 조건과 페이징 정보를 통해 DB 에서 게시물 목록 조회
        Page<InformationBoard> informationBoardList = informationBoardRepository.findList(boardSearchDto,
                category,
                pageable);

        // Entity → DTO 변환
        return informationBoardList.map(InformationBoardListResDto::new);

    }

    // 소개 게시판 게시물 목록 조회
    public Page<IntroductionBoardListResDto> findIntroductionBoardList(BoardSearchDto boardSearchDto, Pageable pageable) {

        // 검색 조건과 페이징 정보를 통해 DB 에서 게시물 목록 조회
        Page<IntroductionBoard> introductionBoardList = introductionBoardRepository.findList(boardSearchDto, pageable);

        // Entity → DTO 변환
        return introductionBoardList.map(IntroductionBoardListResDto::new);

    }

    // 게시판 게시물 상세 조회
    public CommonResDto boardDetail(Category category, Long postId, String email, HttpServletRequest request) {


        // 입력 받은 카테고리 값이 설정 해둔 값과 일치 하지 않다면 예외
        if (!isValidCategory(category)) {
            throw new CommonException(ErrorCode.DATA_NOT_FOUND, "옳지 않은 카테고리 입니다.");
        }

        if (category == Category.INTRODUCTION) {
            // 게시물 조회 (null 방지)
            IntroductionBoard board = introductionBoardRepository.findById(postId)
                    .orElseThrow(() -> new CommonException(ErrorCode.DATA_NOT_FOUND, "찾고있는 게시물이 없습니다."));

            // 사용자 식별 정보 생성
            String ip = extractClientIp(request);
            String userAgent = request.getHeader("User-Agent");

            // Redis 중복 조회 방지를 위한 Key 생성
            String redisKey = generateRedisKey(email, ip, userAgent, category, postId);

            // Redis에 기록이 없으면 첫 조회 → 조회수 증가 처리
            increaseViewCountIntroductionFirstTime(redisKey, board);

            // 화면단으로 보낼 DTO로 변환
            IntroductionBoardResDto resDto = board.fromEntity(board);

            return new CommonResDto(HttpStatus.OK, "소개 게시물 조회 성공", resDto);
        } else {
            // null 이면 들어올 수 없으니까 에러 던짐
            InformationBoard board = informationBoardRepository.findById(postId)
                    .orElseThrow(() -> new CommonException(ErrorCode.DATA_NOT_FOUND, "찾고있는 게시물이 없습니다."));

            // 사용자 식별 정보 생성
            String ip = extractClientIp(request);
            String userAgent = request.getHeader("User-Agent");

            // Redis 중복 조회 방지를 위한 Key 생성
            String redisKey = generateRedisKey(email, ip, userAgent, category, postId);

            // Redis에 기록이 없으면 첫 조회 → 조회수 증가 처리
            increaseViewCountInformationFirstTime(redisKey, board);

            // 화면단으로 보낼 DTO로 변환
            InformationBoardResDto resDto = board.fromEntity(board);

            return new CommonResDto(HttpStatus.OK, "게시물 상세 조회 성공!", resDto);
        }
    }

    // 정보 게시판 메인 최근 게시물 조회
    public List<InformationBoardListResDto> findInformationMainList() {
        List<InformationBoard> informationBoardList = informationBoardRepository.findMainList();

        return informationBoardList.stream()
                .map(informationBoard -> InformationBoardListResDto.builder()
                        .informationBoard(informationBoard) // 엔티티 -> DTO 변환
                        .build())
                .collect(Collectors.toList());
    }

    // 소개 게시판 메인 최근 게시물 조회
    public List<IntroductionBoardListResDto> findIntroductionMainList() {
        List<IntroductionBoard> introductionBoardList = introductionBoardRepository.findMainList();

        return introductionBoardList.stream()
                .map(introductionBoard -> IntroductionBoardListResDto.builder()
                        .introductionBoard(introductionBoard) // 엔티티 -> DTO 변환
                        .build())
                .collect(Collectors.toList());


    }

    // 정보 게시판 메인 인기 게시물 조회
    public List<InformationBoardListResDto> findPopularInformationBoard() {
        List<InformationBoard> board = informationBoardRepository.findPopularList(10, 7); // 최근 7일 상위 10개

        return board.stream()
                .map(informationBoard -> InformationBoardListResDto.builder()
                        .informationBoard(informationBoard) // 엔티티 → DTO 변환
                        .build())
                .toList();
    }

    // 입력받은 카테고리가 유효하냐 (contains)
    private boolean isValidCategory(Category input) {
        return categoryList.contains(input);
    }


    private String setThumbnailImage(MultipartFile thumbnailImage) {

        // 썸네일 이미지를 저장할 경로
        String savePath = null;

        // 썸네일 이미지가 있다면 저장
        if (thumbnailImage != null && !thumbnailImage.isEmpty()) {
            try {
                // 원래 업로드된 파일명
                String originalName = thumbnailImage.getOriginalFilename();

                //고유한 파일명을 만드기 위해 UUID 사용
                String fileName = UUID.randomUUID() + "_" + originalName;

                // 카테고리별 폴더 구성
                File dir = new File(thumbnailImagePath);
                if (!dir.exists()) dir.mkdirs();

                // 최종 저장 경로
                File dest = new File(thumbnailImagePath, fileName);
                thumbnailImage.transferTo(dest);

                savePath = fileName;
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
     * @param InformationBoard 조회 대상 엔티티 (조회수 업데이트 대상)
     */
    private void increaseViewCountInformationFirstTime(String redisKey, InformationBoard informationBoard) {
        // Redis에 키가 없을 경우만 조회수 증가
        if (!redisTemplate.hasKey(redisKey)) {
            // 현재 조회수를 1 증가시킨 후 저장
            informationBoard.viewCountUp(informationBoard.getViewCount() + 1);
            informationBoardRepository.save(informationBoard);

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

    /**
     * Redis를 활용하여 하루 1회만 조회수 증가 처리
     *
     * @param redisKey Redis 중복 조회 방지용 키
     * @param IntroductionBoard 조회 대상 엔티티 (조회수 업데이트 대상)
     */
    private void increaseViewCountIntroductionFirstTime(String redisKey, IntroductionBoard introductionBoard) {
        // Redis에 키가 없을 경우만 조회수 증가
        if (!redisTemplate.hasKey(redisKey)) {
            // 현재 조회수를 1 증가시킨 후 저장
            introductionBoard.viewCountUp(introductionBoard.getViewCount() + 1);
            introductionBoardRepository.save(introductionBoard);

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







