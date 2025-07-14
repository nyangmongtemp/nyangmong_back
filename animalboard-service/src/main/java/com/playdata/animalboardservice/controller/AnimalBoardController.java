package com.playdata.animalboardservice.controller;

import com.playdata.animalboardservice.common.auth.JwtTokenProvider;
import com.playdata.animalboardservice.common.auth.TokenUserInfo;
import com.playdata.animalboardservice.common.dto.CommonResDto;
import com.playdata.animalboardservice.dto.SearchDto;
import com.playdata.animalboardservice.dto.req.AnimalInsertRequestDto;
import com.playdata.animalboardservice.dto.req.AnimalUpdateRequestDto;
import com.playdata.animalboardservice.dto.req.ReservationReqDto;
import com.playdata.animalboardservice.dto.res.AnimalDetailResDto;
import com.playdata.animalboardservice.dto.res.AnimalListResDto;
import com.playdata.animalboardservice.entity.Animal;
import com.playdata.animalboardservice.service.AnimalService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
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
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

// Swagger 전용 인터페이스를 하나 선언해서 비즈니스 로직 vs 문서화 로직을 분리
// 컨트롤러는 본연의 역할에만 집중
@Tag(name = "유기동물/분양 게시판(AnimalBoard)", description = "유기동물 / 분양동물 CRUD 관리하는 API")
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
    @Operation(
            summary = "분양동물 목록 조회 (페이징)",
            description = "페이징이 적용된 고객상담 목록을 조회한다."
    )
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
    @Operation(
            summary = "분양 게시물 상세 조회",
            description = """
        게시물 ID를 기반으로 분양 게시물의 상세 정보를 조회합니다.
        
        ## 인증
        - 로그인 하지 않은 사용자도 조회 가능합니다.
        - 로그인 상태인 경우, 사용자 정보를 함께 활용하여 개인화된 결과 제공 가능.
    """,
            tags = {"분양 게시물"},
            security = @SecurityRequirement(name = "bearerAuth") // 선택적 인증일 경우 제거해도 무방
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "조회 성공",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = CommonResDto.class),
                            examples = @ExampleObject(value = """
                {
                    "status": "OK",
                    "message": "상세 조회 성공",
                    "data": {
                        "id": 1,
                        "title": "강아지 분양",
                        "age": "1살",
                        "gender": "수컷",
                        "description": "건강하고 귀여운 강아지 분양합니다.",
                        "liked": true
                    }
                }
            """)
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "존재하지 않는 게시물 ID",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = AnimalDetailResDto.class),
                            examples = @ExampleObject(value = """
                {
                    "status": "404",
                    "message": "게시물을 찾을 수 없습니다."
                }
            """)
                    )
            )
    })
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
                log.error("e: ", e);
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

    // 회원 탈퇴 시, 회원의 id를 줌 --> 회원의 모든 게시물 삭제 처리 (active = false)
    @DeleteMapping("/deleteUser/{id}")
    ResponseEntity<?> deleteUser(@PathVariable("id") Long userId) {
        CommonResDto resDto = animalService.deleteUserAll(userId);
        return ResponseEntity.ok(resDto);
    }

    // 회원이 닉네임 변경 시 --> 회원의 모든 게시물의 nickname값 변경
    @PutMapping("/modifyNickname/{id}/{nickname}")
    ResponseEntity<?> modifyNickname(@PathVariable("id") Long userId, @PathVariable("nickname") String nickname) {
        CommonResDto resDto = animalService.changeUserNickname(userId, nickname);
        return ResponseEntity.ok(resDto);
    }
}
