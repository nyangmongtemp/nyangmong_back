package com.playdata.animalboardservice.service;

import com.playdata.animalboardservice.common.auth.TokenUserInfo;
import com.playdata.animalboardservice.common.dto.CommonResDto;
import com.playdata.animalboardservice.common.enumeration.ErrorCode;
import com.playdata.animalboardservice.common.exception.CommonException;
import com.playdata.animalboardservice.common.util.ImageValidation;
import com.playdata.animalboardservice.dto.SearchDto;
import com.playdata.animalboardservice.dto.req.AnimalInsertRequestDto;
import com.playdata.animalboardservice.dto.req.AnimalUpdateRequestDto;
import com.playdata.animalboardservice.dto.req.ReservationReqDto;
import com.playdata.animalboardservice.dto.res.AnimalListResDto;
import com.playdata.animalboardservice.entity.Animal;
import com.playdata.animalboardservice.entity.ReservationStatus;
import com.playdata.animalboardservice.repository.AnimalRepository;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import java.io.File;
import java.io.IOException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
@Slf4j
public class AnimalService {

    private final AnimalRepository animalRepository;
    private final RedisTemplate<String, String> redisTemplate;

    // application.yml에서 설정한 이미지 저장 경로를 주입받음
    @Value("${imagePath.url}")
    private String imageSaveUrl;

    private static final String BOARD_TYPE = "animal"; // 게시판 구분용 상수 (조회수 Redis 키 구분에 사용)

    /**
     * 분양 게시물 목록 조회 (검색 및 페이징 포함)
     *
     * @param searchDto 검색 필터 조건
     * @param pageable 페이징 조건 (페이지 번호, 사이즈, 정렬 등)
     * @return AnimalListResDto로 매핑된 Page 객체 반환
     */
    public Page<AnimalListResDto> findStrayAnimalList(SearchDto searchDto, Pageable pageable) {
        // animalRepository에서 커스텀 쿼리로 조건에 맞는 목록을 조회
        Page<Animal> animalList = animalRepository.findList(searchDto, pageable);

        // Entity → DTO로 변환 (View에 필요한 필드만 노출)
        return animalList.map(animal ->
                AnimalListResDto.builder()
                        .animal(animal)
                        .build()
        );
    }

    /**
     * 게시물 상세 조회 (조회수 중복 방지 및 증가 포함)
     *
     * @param postId 게시물 ID
     * @param email 로그인 사용자 이메일 (null 가능)
     * @param request 사용자 요청 정보 (IP, User-Agent 추출용)
     * @return 조회된 Animal Entity
     */
    public Animal findByAnimal(Long postId, String email, HttpServletRequest request) {
        // 게시물 존재 여부 확인 (예외 처리 포함)
        Animal animal = animalRepository.findByPostIdAndActiveTrue(postId);
        Optional.ofNullable(animal)
                .orElseThrow(() -> new CommonException(ErrorCode.DATA_NOT_FOUND));

        // 사용자 식별 정보 생성
        String ip = extractClientIp(request);
        String userAgent = request.getHeader("User-Agent");

        // Redis 중복 조회 방지를 위한 Key 생성
        String redisKey = generateRedisKey(email, ip, userAgent, BOARD_TYPE, postId);

        // Redis에 기록이 없으면 첫 조회 → 조회수 증가 처리
        increaseViewCountIfFirstTime(redisKey, animal);

        return animal;
    }

    /**
     * 게시물 등록 (동물 분양 게시글 생성)
     *
     * @param userInfo JWT 토큰에서 파싱된 로그인 사용자 정보 (userId 포함)
     * @param animalRequestDto 클라이언트로부터 받은 게시글 요청 DTO
     * @param thumbnailImage 썸네일 이미지 (Multipart 형식)
     */
    @Transactional
    public void insertAnimal(TokenUserInfo userInfo, @Valid AnimalInsertRequestDto animalRequestDto, MultipartFile thumbnailImage) {
        Long userId = userInfo.getUserId(); // 사용자 ID 추출
        // 이미지 존재유무
        if (thumbnailImage == null || thumbnailImage.isEmpty()) {
            throw new CommonException(ErrorCode.EMPTY_FILE);
        }
        // 이미지 유효성 검사 (용량, 확장자 등)
        ImageValidation.validateImageFile(thumbnailImage);
        // 이미지 저장 후, 저장된 파일명 반환
        String newThumbnailImage = setProfileImage(thumbnailImage);
        // DTO → Entity 변환 후 저장
        animalRepository.save(animalRequestDto.toEntity(userId, newThumbnailImage, userInfo.getNickname()));
    }

    /**
     * 분양 게시글 수정
     * @param postId 게시판 번호
     * @param animalRequestDto 수정할 데이터 DTO
     * @param thumbnailImage 저장할 썸네일 이미지
     * @return
     */
    @Transactional
    public void updateAnimal(Long postId, AnimalUpdateRequestDto animalRequestDto, MultipartFile thumbnailImage, TokenUserInfo userInfo) {
        // 조회
        Animal animal = animalRepository.findByPostIdAndActiveTrue(postId);
        Optional.ofNullable(animal).orElseThrow(() -> new CommonException(ErrorCode.DATA_NOT_FOUND));

        // 글쓴사람과 로그인한 사람이 같은지 비교
        if (!userInfo.getUserId().equals(animal.getUserId())) {
            throw new CommonException(ErrorCode.UNAUTHORIZED);
        }

        // 이미지 유효성 검사 (용량, 확장자 등)
        ImageValidation.validateImageFile(thumbnailImage);
        // 이미지 저장 후, 저장된 파일명 반환
        String newThumbnailImage = setProfileImage(thumbnailImage);

        // 수정
        animal.updateAnimal(animalRequestDto, newThumbnailImage);
    }

    /**
     * 분양게시굴 삭제
     * @param postId 게시판 번호
     * @return
     */
    @Transactional
    public void deleteAnimal(Long postId, TokenUserInfo userInfo) {
        // 조회
        Animal animal = animalRepository.findByPostIdAndActiveTrue(postId);
        Optional.ofNullable(animal).orElseThrow(() -> new CommonException(ErrorCode.DATA_NOT_FOUND));

        // 글쓴사람과 로그인한 사람이 같은지 비교
        if (!userInfo.getUserId().equals(animal.getUserId())) {
            throw new CommonException(ErrorCode.UNAUTHORIZED);
        }

        // 삭제
        animal.deleteAnimal();
    }

    /**
     * 분양 동물 예약상태 변경
     * @param postId 게시판 번호
     * @param userInfo 로그인한 유저 정보
     */
    @Transactional
    public void reservationStatusAnimal(Long postId, TokenUserInfo userInfo, ReservationReqDto reservationReqDto) {
        Animal animal = animalRepository.findByPostIdAndActiveTrue(postId);
        Optional.ofNullable(animal).orElseThrow(() -> new CommonException(ErrorCode.DATA_NOT_FOUND));
        if (!userInfo.getUserId().equals(animal.getUserId())) {
            throw new CommonException(ErrorCode.UNAUTHORIZED);
        }
        animal.reservationStatusAnimal(reservationReqDto.getReservationStatus());
    }

    /**
     * 회원 탈퇴 시, 회원의 id를 줌 --> 회원의 모든 게시물 삭제 처리
     * @param userId
     * @return
     */
    @Transactional
    public CommonResDto deleteUserAll(Long userId) {
        // 해당 유저가 작성한 게시물 목록 조회
        List<Animal> animalList = animalRepository.findByUserId(userId)
                .orElse(Collections.emptyList()); // 게시물이 없는 경우 빈 리스트 반환

        // 게시물이 하나도 없는 경우
        if (animalList.isEmpty()) {
            return new CommonResDto(HttpStatus.OK, "삭제할 게시물이 없습니다.", true);
        }

        // 모든 게시물에 대해 소프트 삭제 처리
        animalList.forEach(Animal::deleteAnimal);

        return new CommonResDto(HttpStatus.OK, "회원님의 모든 게시물을 삭제하였습니다.", true);
    }

    /**
     * 회원의 닉네임이 변경될 경우, 해당 회원이 작성한 모든 게시물의 작성자 닉네임도 함께 변경합니다.
     *
     * @param userId 닉네임을 변경한 회원의 ID
     * @param nickname 닉네임(한글)
     * @return 닉네임 변경 성공 응답
     */
    @Transactional
    public CommonResDto changeUserNickname(Long userId, String nickname) {
        // 해당 회원의 게시글 전체 조회
        List<Animal> animalList = animalRepository.findByUserId(userId)
                .orElse(Collections.emptyList());

        // 게시글이 없으면 바로 응답
        if (animalList.isEmpty()) {
            return new CommonResDto(HttpStatus.OK, "변경할 게시글이 없습니다.", true);
        }

        // 모든 게시글의 작성자 닉네임을 변경
        animalList.forEach(animal -> animal.changeNickname(nickname));

        return new CommonResDto(HttpStatus.OK, "회원님의 모든 게시글의 닉네임이 변경되었습니다.", true);
    }


    /**
     * Redis를 활용하여 하루 1회만 조회수 증가 처리
     *
     * @param redisKey Redis 중복 조회 방지용 키
     * @param animal 조회 대상 엔티티 (조회수 업데이트 대상)
     */
    private void increaseViewCountIfFirstTime(String redisKey, Animal animal) {
        // Redis에 키가 없을 경우만 조회수 증가
        if (!redisTemplate.hasKey(redisKey)) {
            // 현재 조회수를 1 증가시킨 후 저장
            animal.viewCountUp(animal.getViewCount() + 1);
            animalRepository.save(animal);

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
    private String generateRedisKey(String email, String ip, String userAgent, String boardType, Long postId) {
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
     * 이미지 저장 처리
     *
     * 서버에 이미지를 저장하고, 저장된 파일명을 반환함
     *
     * @param imageFile Multipart 파일 객체
     * @return 저장된 파일명 (DB에 저장될 상대 경로)
     */
    private String setProfileImage(MultipartFile imageFile) {
        String profileImagePath = null;

        if (imageFile != null && !imageFile.isEmpty()) {
            try {
                String originalFilename = imageFile.getOriginalFilename();

                // UUID + 원본 파일명으로 저장 (중복 방지)
                String fileName = UUID.randomUUID() + "_" + originalFilename;

                // 저장 경로 확인 후 디렉토리 생성
                File dir = new File(imageSaveUrl);
                if (!dir.exists()) dir.mkdirs();

                // 파일 저장
                File dest = new File(imageSaveUrl, fileName);
                imageFile.transferTo(dest);

                // 저장된 상대 파일명 반환 (DB 저장용)
                profileImagePath = fileName;
            } catch (IOException e) {
                log.error("이미지 저장 중 오류 발생", e);
                throw new CommonException(ErrorCode.FILE_SERVER_ERROR);
            }
        }

        return profileImagePath;
    }
}