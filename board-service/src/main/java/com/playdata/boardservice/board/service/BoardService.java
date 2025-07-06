package com.playdata.boardservice.board.service;

import com.playdata.boardservice.board.dto.*;
import com.playdata.boardservice.board.entity.Category;
import com.playdata.boardservice.board.entity.InformationBoard;
import com.playdata.boardservice.board.entity.IntroductionBoard;
import com.playdata.boardservice.board.repository.InformationBoardRepository;
import com.playdata.boardservice.board.repository.IntroductionBoardRepository;
import com.playdata.boardservice.common.auth.TokenUserInfo;
import com.playdata.boardservice.common.dto.CommonResDto;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class BoardService {

    private final InformationBoardRepository informationBoardRepository;
    private final IntroductionBoardRepository introductionBoardRepository;

    // 이미지 저장 경로
    @Value("${imagePath.thumbnail.url}")
    private String thumbnailImagePath;

    private List<String> categoryList = List.of("freedom", "introduction", "question", "review");

    // 질문, 후기, 자유 게시판 게시물 등록
    public CommonResDto informationCreate(InformationBoardSaveReqDto informationSaveDto
            , MultipartFile thumbnailImage
            , TokenUserInfo userInfo) {
        try {
            // 카테고리 값 가져와서 null, 빈문자열 체크 후 값이 있다면 대문자로 변환
            String category = (informationSaveDto.getCategory() != null && !informationSaveDto.getCategory().isBlank())
                    ? informationSaveDto.getCategory().trim().toUpperCase()
                    : "default";

//            StringUtils.isEmpty()

            // DTO → toEntity() 로 변환 -> DB
            informationSaveDto.setCategory(category);

            // 썸네일 경로를 저장할 변수
            String savedPath = null;

            // 썸네일 이미지가 있다면 저장
            if (thumbnailImage != null && !thumbnailImage.isEmpty()) {

                // 원래 업로드된 파일명
                String originalName = thumbnailImage.getOriginalFilename();

                // 고유한 파일명을 만들기 위해 UUID 사용
                String fileName = UUID.randomUUID() + "_" + originalName;

                // 카테고리별 폴더 구성
                File dir = new File(thumbnailImagePath, category);
                if (!dir.exists()) dir.mkdirs();

                // 최종 저장 경로
                File dest = new File(dir, fileName);
                thumbnailImage.transferTo(dest);

                // DB에는 상대 경로만 저장 (ex: 정보/uuid_고양이.jpg)
                savedPath = category + "/" + fileName;

                // DTO에 썸네일 경로 세팅
                informationSaveDto.setThumbnailImage(savedPath);
            }

            // DTO → toEntity() 로 변환 -> DB
            InformationBoard entity = informationSaveDto.toEntity(userInfo.getUserId(), userInfo.getNickname());
            informationBoardRepository.save(entity);

            // 성공 응답 반환
            return new CommonResDto(HttpStatus.CREATED, "게시물 등록 성공", entity.getPostId());

        } catch (IOException e) {
            // 예외 발생 시 에러 로그 남기고 실패 응답
            log.error("썸네일 저장 실패: {}", e.getMessage());
            return new CommonResDto(HttpStatus.INTERNAL_SERVER_ERROR, "파일 저장 중 오류 발생", null);
        }
    }

    // 소개 게시판 게시물 등록
    public CommonResDto introductionCreate(IntroductionBoardSaveReqDto introductionSaveDto
            , MultipartFile thumbnailImage
            , TokenUserInfo userInfo) {
        try {
            // 썸네일 경로를 저장할 변수
            String savedPath = null;

            // 썸네일 이미지가 있다면 저장
            if (thumbnailImage == null || thumbnailImage.isEmpty()) {
                throw new IllegalArgumentException("썸네일 이미지는 필수입니다.");
            }

            // 원래 업로드된 파일명
            String originalName = thumbnailImage.getOriginalFilename();

            // 고유한 파일명을 만들기 위해 UUID 사용
            String fileName = UUID.randomUUID() + "_" + originalName;

            // 카테고리 = "INTRODUCTION"
            String category = "INTRODUCTION";

            // 카테고리별 폴더 구성
            File dir = new File(thumbnailImagePath, category);
            if (!dir.exists()) dir.mkdirs();

            // 최종 저장 경로
            File dest = new File(dir, fileName);
            thumbnailImage.transferTo(dest);

            // DB에는 상대 경로만 저장 (ex: 정보/uuid_고양이.jpg)
            savedPath = category + "/" + fileName;

            // DTO에 썸네일 경로 세팅
            introductionSaveDto.setThumbnailImage(savedPath);


            // DTO → Entity 변환 후 저장
            IntroductionBoard entity = introductionSaveDto.toEntity(userInfo.getUserId(), userInfo.getNickname());
            introductionBoardRepository.save(entity);

            // 성공 응답 반환
            return new CommonResDto(HttpStatus.CREATED, "게시물 등록 성공", entity.getPostId());

        } catch (IOException e) { // 예외 발생 시 에러 로그 남기고 실패 응답
            log.error("썸네일 저장 실패: {}", e.getMessage());
            return new CommonResDto(HttpStatus.INTERNAL_SERVER_ERROR, "파일 저장 중 오류 발생", null);
        } catch (IllegalArgumentException e) { // 썸네일이 null 이거나 비어있을 때 여기서 응답
            return new CommonResDto(HttpStatus.BAD_REQUEST, e.getMessage(), null);
        }
    }

    // 게시물 수정 (공통)
    public void boardModify(BoardModiDto modiDto,
                            MultipartFile thumbnailImage,
                            TokenUserInfo userInfo,
                            Category category,
                            Long postId) {
        try {
            // 썸네일 저장 경로 변수 (null이면 수정 안 함)
            String savedPath = null;

            // 사용자가 새로운 썸네일 이미지를 첨부한 경우
            if (thumbnailImage != null && !thumbnailImage.isEmpty()) {
                String originalName = thumbnailImage.getOriginalFilename();
                String fileName = UUID.randomUUID() + "_" + originalName;
                String categoryStr = category.name();

                // 디렉토리가 없다면 생성 (서비스 운영중 파일이 폴더가 손상 되었을 확률도 있기 때문에 다시 설정)
                File dir = new File(thumbnailImagePath, categoryStr);
                if (!dir.exists()) dir.mkdirs();

                // 파일 저장
                File dest = new File(dir, fileName);
                thumbnailImage.transferTo(dest);

                // DB에 저장할 상대 경로 세팅
                savedPath = categoryStr + "/" + fileName;
            }

            // 소개 게시판은 INTRODUCTION enum 으로 단일 구분
            if (category == Category.INTRODUCTION) {
                // 게시글 조회 (없으면 예외)
                IntroductionBoard board = introductionBoardRepository.findById(postId)
                        .orElseThrow(() -> new IllegalArgumentException("해당 소개 게시글이 존재하지 않습니다."));

                // 작성자 검증 (자기 글만 수정 가능)
                if (!board.getUserId().equals(userInfo.getUserId())) {
                    throw new SecurityException("작성자만 수정할 수 있습니다.");
                }

                // 썸네일은 필수이므로 없으면 예외
                if (savedPath == null) {
                    throw new IllegalArgumentException("소개 게시판은 썸네일 이미지를 반드시 첨부해야 합니다.");
                }

                // 본문 및 썸네일 수정
                board.setContent(modiDto.getContent());
                board.setThumbnailImage(savedPath);

                // 수정된 게시글 저장
                introductionBoardRepository.save(board);

                // 정보 게시판의 카테고리를 설정
            } else if (category == Category.QUESTION || category == Category.REVIEW || category == Category.FREEDOM) {

                // 게시글 조회 (없으면 예외)
                InformationBoard board = informationBoardRepository.findById(postId)
                        .orElseThrow(() -> new IllegalArgumentException("해당 정보 게시글이 존재하지 않습니다."));

                // 작성자 검증
                if (!board.getUserId().equals(userInfo.getUserId())) {
                    throw new SecurityException("작성자만 수정할 수 있습니다.");
                }

                // content 수정
                board.setContent(modiDto.getContent());

                if (savedPath != null) {
                    // 새 이미지가 있으면 교체
                    board.setThumbnailImage(savedPath);
                } else if (board.getThumbnailImage() != null && (thumbnailImage == null || thumbnailImage.isEmpty())) {

                    // 기존 이미지가 있었고, 새 이미지가 없으면 → 삭제
                    // 삭제 시 저장 디렉토리에서도 이미지 삭제
                    File oldFile = new File(thumbnailImagePath + File.separator + board.getThumbnailImage());
                    if (oldFile.exists()) oldFile.delete();

                    board.setThumbnailImage(null);
                }

                // 수정된 게시글 저장
                informationBoardRepository.save(board);

            } else {
                // 그 외 잘못된 카테고리는 예외
                throw new IllegalArgumentException("지원하지 않는 게시판 카테고리입니다.");
            }

        } catch (IOException e) {
            // 파일 저장 실패 시 로그 출력 및 예외 던짐
            log.error("썸네일 저장 실패: {}", e.getMessage());
            throw new RuntimeException("파일 저장 중 오류 발생");
        } catch (SecurityException | IllegalArgumentException e) {
            // 유효성 or 인증 오류는 그대로 던짐
            throw e;
        } catch (Exception e) {
            // 예기치 못한 에러 처리
            log.error("게시글 수정 중 예외 발생: {}", e.getMessage());
            throw new RuntimeException("게시글 수정 중 오류 발생");
        }
    }

    // 게시물 삭제 (공통)
    public void deleteBoard(TokenUserInfo userInfo, Category category, Long postId) {
        // 소개 게시판인 경우
        if (category == Category.INTRODUCTION) {
            // 게시글 조회 (없으면 예외)
            IntroductionBoard board = introductionBoardRepository.findById(postId)
                    .orElseThrow(() -> new IllegalArgumentException("해당 소개 게시글이 존재하지 않습니다."));

            // 작성자 검증 (다른 사람이 삭제 요청하면 차단)
            if (!board.getUserId().equals(userInfo.getUserId())) {
                throw new SecurityException("작성자만 삭제할 수 있습니다.");
            }

            // 실제 삭제하지 않고 active 값을 false 로 변경 (소프트 삭제)
            board.setActive(false);

            // 변경된 상태를 DB에 저장
            introductionBoardRepository.save(board);
        }

        // 질문/후기/자유 게시판인 경우
        else if (category == Category.QUESTION || category == Category.REVIEW || category == Category.FREEDOM) {
            // 게시글 조회 (없으면 예외)
            InformationBoard board = informationBoardRepository.findById(postId)
                    .orElseThrow(() -> new IllegalArgumentException("해당 정보 게시글이 존재하지 않습니다."));

            // 작성자 검증
            if (!board.getUserId().equals(userInfo.getUserId())) {
                throw new SecurityException("작성자만 삭제할 수 있습니다.");
            }

            // 썸네일이 있다면 로컬에서 삭제
            if (board.getThumbnailImage() != null) {
                File file = new File(thumbnailImagePath + File.separator + board.getThumbnailImage());
                if (file.exists()) file.delete();
            }

            // active = false로 비활성화 처리
            board.setActive(false);

            // DB에 저장
            informationBoardRepository.save(board);
        }

        // 지원하지 않는 카테고리
        else {
            throw new IllegalArgumentException("지원하지 않는 게시판 카테고리입니다.");
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
    public CommonResDto boardDetail(String category, Long postId) {

        if(!isValidCategory(category)) {
            throw new IllegalArgumentException("옳지 않은 카테고리 입력값입니다.");
        }
        Category cate = Category.valueOf(category.toUpperCase());

        if(cate == Category.INTRODUCTION) {
            // null 방지
            Optional<IntroductionBoard> foundPost = introductionBoardRepository.findById(postId);
            if(!foundPost.isPresent()) {
                throw new EntityNotFoundException("조회하려는 게시물이 없습니다.");
            }
            IntroductionBoard board = foundPost.get();
            IntroductionBoardResDto resDto = board.fromEntity(board);

            return new CommonResDto(HttpStatus.OK, "소개 게시물 조회 성공", resDto);
        }
        else {
            // null 이면 들어올 수 없으니까 에러 던짐
            InformationBoard board = informationBoardRepository.findById(postId)
                    .orElseThrow(() -> new EntityNotFoundException("조회하려는 게시물이 없습니다."));
            InformationBoardResDto resDto = board.fromEntity(board);

            return new CommonResDto(HttpStatus.OK, "게시물 상세 조회 성공!", resDto);
        }
    }

    // 입력받은 카테고리가 유효하냐
    private boolean isValidCategory(String input) {
        return categoryList.contains(input);
    }
}




