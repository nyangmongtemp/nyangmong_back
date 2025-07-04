package com.playdata.animalboardservice.service;

import com.playdata.animalboardservice.common.enumeration.ErrorCode;
import com.playdata.animalboardservice.common.exception.CommonException;
import com.playdata.animalboardservice.dto.SearchDto;
import com.playdata.animalboardservice.dto.res.AnimalListResDto;
import com.playdata.animalboardservice.entity.Animal;
import com.playdata.animalboardservice.repository.AnimalRepository;
import jakarta.servlet.http.HttpServletRequest;
import java.time.LocalDate;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class AnimalService {

    private final AnimalRepository animalRepository;
    private final RedisTemplate<String, String> redisTemplate;

    private static final String BOARD_TYPE = "animal"; // 게시판 타입 지정

    /**
     * 분양 목록을 조회하고 응답 DTO로 매핑
     * @param searchDto 검색 조건
     * @param pageable 페이징 정보
     * @return 분양 동물 목록 페이지
     */
    public Page<AnimalListResDto> findStrayAnimalList(SearchDto searchDto, Pageable pageable) {
        // 검색 조건과 페이징 정보를 통해 DB에서 분양 동물 목록 조회
        Page<Animal> animalList = animalRepository.findList(searchDto, pageable);

        // Entity -> DTO 변환 후 반환
        return animalList.map(animal ->
                AnimalListResDto.builder()
                        .animal(animal)
                        .build()
        );
    }

    /**
     * 게시물 단건 조회 + 조회수 중복 방지 및 증가 처리
     * @param postId 게시물 ID
     * @param email 로그인 사용자 이메일 (비로그인일 경우 null)
     * @param request HttpServletRequest: IP와 브라우저 정보 추출용
     * @return Animal 엔티티
     */
    public Animal findByAnimal(Long postId, String email, HttpServletRequest request) {
        // DB에서 게시물 조회 (없으면 예외 발생)
        Animal animal = animalRepository.findByPostId(postId);
        Optional.ofNullable(animal).orElseThrow(() -> new CommonException(ErrorCode.DATA_NOT_FOUND));

        // 중복 방지를 위한 사용자 구분 정보 추출
        String ip = extractClientIp(request);
        String userAgent = request.getHeader("User-Agent");

        // Redis Key 구성
        String redisKey = generateRedisKey(email, ip, userAgent, BOARD_TYPE, postId);

        // Redis에 없으면 조회수 증가 및 Redis 등록
        increaseViewCountIfFirstTime(redisKey, animal);
        return animal;
    }

    /**
     * Redis에 처음 조회한 사용자만 조회수 증가 (자정 기준으로 키 만료)
     *
     * @param redisKey Redis에 저장할 키
     * @param animal   조회수 증가 대상 엔티티
     */
    private void increaseViewCountIfFirstTime(String redisKey, Animal animal) {
        if (!redisTemplate.hasKey(redisKey)) {
            // 조회수 1 증가 및 저장
            animal.viewCountUp(animal.getViewCount() + 1);
            animalRepository.save(animal);

            // Redis에 기록 ("1" 값 저장) + 자정까지 유효
            redisTemplate.opsForValue().set(redisKey, "1");
            redisTemplate.expireAt(redisKey,
                    java.util.Date.from(LocalDate.now().plusDays(1).atStartOfDay(
                            java.time.ZoneId.systemDefault()).toInstant()));
        }
    }

    /**
     * 사용자 고유 식별값을 위한 Redis Key 생성
     * 로그인 유저는 이메일 기반, 비로그인 유저는 IP + UserAgent 기반
     * @param email 로그인 이메일 (nullable)
     * @param ip 사용자 IP 주소
     * @param userAgent 브라우저 정보
     * @param boardType 게시판 타입 (ex. animal)
     * @param postId 게시물 ID
     * @return Redis Key
     */
    private String generateRedisKey(String email, String ip, String userAgent, String boardType, Long postId) {
        StringBuilder key = new StringBuilder("viewCount:");
        key.append(boardType).append(":").append(postId).append(":");

        // 로그인한 사용자라면 이메일 기준 키 생성
        if (email != null && !email.isEmpty()) {
            key.append("email:").append(email);
        } else {
            // 비로그인 사용자는 IP + 브라우저 정보 기반
            key.append("ip:").append(ip != null ? ip : "unknown")
                    .append(":ua:").append(userAgent != null ? userAgent.hashCode() : "unknown");
        }

        return key.toString();
    }

    /**
     * 클라이언트의 실제 IP 주소 추출
     * (프록시 서버나 로드밸런서를 통한 요청 고려)
     * @param request HttpServletRequest
     * @return IP 주소
     */
    private String extractClientIp(HttpServletRequest request) {
        String ip = request.getHeader("X-Forwarded-For");
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr(); // 마지막 수단으로 실제 연결된 IP 사용
        }
        // IP가 복수개일 경우 (ex. 프록시 체인), 첫 번째 값 사용
        if (ip != null && ip.contains(",")) {
            ip = ip.split(",")[0].trim();
        }
        return ip;
    }
}