package com.playdata.animalboardservice.controller;

import com.playdata.animalboardservice.common.auth.JwtTokenProvider;
import com.playdata.animalboardservice.common.auth.TokenUserInfo;
import com.playdata.animalboardservice.dto.SearchDto;
import com.playdata.animalboardservice.dto.req.AnimalInsertRequestDto;
import com.playdata.animalboardservice.dto.req.AnimalUpdateRequestDto;
import com.playdata.animalboardservice.dto.req.ReservationReqDto;
import com.playdata.animalboardservice.dto.res.AnimalDetailResDto;
import com.playdata.animalboardservice.dto.res.AnimalListResDto;
import com.playdata.animalboardservice.entity.Animal;
import com.playdata.animalboardservice.service.AnimalService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/animal-board")
public class AnimalBoardController {

    private final AnimalService animalService;
    private final JwtTokenProvider jwtTokenProvider;

    /**
     * 분양 동물 게시판 목록 조회
     * @param searchDto 검색 조건 (ex: 품종, 지역 등)
     * @param pageable 페이지 정보 (size, page, sort 등)
     * @return 페이징된 동물 목록 데이터 (AnimalListResDto)
     */
    @GetMapping("/list")
    public ResponseEntity<Page<AnimalListResDto>> getAnimalList(SearchDto searchDto, Pageable pageable) {
        // 검색 조건과 페이지 정보를 바탕으로 목록 조회
        Page<AnimalListResDto> resDto = animalService.findStrayAnimalList(searchDto, pageable);
        return ResponseEntity.ok().body(resDto);
    }

    /**
     * 분양 게시물 상세 조회
     * @param postId 게시물 ID
     * @param authHeader Authorization 헤더 (Bearer {accessToken})
     * @param request 클라이언트 요청 정보(IP, 브라우저 등 추출용)
     * @return Animal 상세 정보
     */
    @GetMapping("/public/{postId}")
    public ResponseEntity<AnimalDetailResDto> getAnimal(@PathVariable Long postId,
            @RequestHeader(value = "Authorization", required = false) String authHeader,
            HttpServletRequest request) {

        String email = null;
        // Authorization 헤더가 존재하고 Bearer로 시작하는 경우
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7); // "Bearer " 이후의 토큰만 추출
            try {
                // 토큰에서 이메일 추출
                email = jwtTokenProvider.extractEmail(token);
            } catch (Exception e) {
                // JWT 파싱 실패 시 로그 기록 (비로그인 사용자로 처리)
            }
        }

        // 서비스 로직 호출 → 게시물 조회 및 조회수 증가 처리
        Animal animal = animalService.findByAnimal(postId, email, request);
        return ResponseEntity.ok().body(new AnimalDetailResDto(animal));
    }

    /**
     * 분양 게시물 등록
     * @param userInfo 토큰에 저장된 유저정보
     * @param animalRequestDto 셍성할 데이터 DTO
     * @param thumbnailImage 저장할 썸네일 이미지
     * @return
     */
    @PostMapping("")
    public ResponseEntity<AnimalInsertRequestDto> createAnimal(
            @AuthenticationPrincipal TokenUserInfo userInfo,
            @RequestPart("animalRequest") @Valid AnimalInsertRequestDto animalRequestDto,
            @RequestPart(value = "thumbnailImage") MultipartFile thumbnailImage) {
        animalService.insertAnimal(userInfo, animalRequestDto, thumbnailImage);
        return ResponseEntity.ok().build();
    }

    /**
     * 분양 게시글 수정
     * @param postId 게시판 번호
     * @param animalRequestDto 수정할 데이터 DTO
     * @param thumbnailImage 저장할 썸네일 이미지
     * @return
     */
    @PatchMapping("/{postId}")
    public ResponseEntity<Void> updateAnimal(@PathVariable Long postId,
            @AuthenticationPrincipal TokenUserInfo userInfo,
            @RequestPart("animalRequest") @Valid AnimalUpdateRequestDto animalRequestDto,
            @RequestPart(value = "thumbnailImage") MultipartFile thumbnailImage) {
        animalService.updateAnimal(postId, animalRequestDto, thumbnailImage, userInfo);
        return ResponseEntity.ok().build();
    }

    /**
     * 분양게시굴 삭제
     * @param postId 게시판 번호
     * @param userInfo 로그인한 유저 정보
     * @return
     */
    @DeleteMapping("/{postId}")
    public ResponseEntity<Void> deleteAnimal(@PathVariable Long postId, @AuthenticationPrincipal TokenUserInfo userInfo) {
        animalService.deleteAnimal(postId, userInfo);
        return ResponseEntity.ok().build();
    }

    /**
     * 분양 동물 예약상태 변경 api
     * @param postId 게시판 번호
     * @param userInfo 로그인한 유저 정보
     * @return
     */
    @PatchMapping("/reservation/{postId}")
    public ResponseEntity<?> reservationStatusAnimal(@PathVariable Long postId,
            @AuthenticationPrincipal TokenUserInfo userInfo,
            @RequestBody @Valid ReservationReqDto reservationReqDto) {
        animalService.reservationStatusAnimal(postId, userInfo, reservationReqDto);
        return ResponseEntity.ok().build();
    }
}
