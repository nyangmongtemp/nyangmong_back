package com.playdata.adminservice.admin.controller;

import com.playdata.adminservice.admin.dto.board.AnimalSearchDto;
import com.playdata.adminservice.admin.dto.board.BoardSearchDto;
import com.playdata.adminservice.admin.dto.board.res.AnimalListResDto;
import com.playdata.adminservice.admin.dto.board.res.BoardListResDto;
import com.playdata.adminservice.admin.entity.Animal;
import com.playdata.adminservice.admin.entity.Category;
import com.playdata.adminservice.admin.service.AdminBoardService;
import com.playdata.adminservice.common.auth.JwtTokenProvider;
import com.playdata.adminservice.common.auth.TokenAdminInfo;
import com.playdata.adminservice.common.dto.CommonResDto;
import com.playdata.adminservice.common.enumeration.ErrorCode;
import com.playdata.adminservice.common.exception.CommonException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin/board")
@RequiredArgsConstructor
@Slf4j
@PreAuthorize("hasAnyRole('BOSS', 'CUSTOMER')")
public class AdminBoardController implements AdminBoardControllerDocs{

    private final AdminBoardService adminBoardService;
    private final JwtTokenProvider jwtTokenProvider;

    /**
     *
     * @param boardSearchDto
     * @param category
     * @param pageable
     * @return
     */
    // 게시판 게시물 목록 조회
    @GetMapping("/list/{category}")
    public ResponseEntity<Page<BoardListResDto>> getBoardList(BoardSearchDto boardSearchDto,
                                                              @PathVariable String category,
                                                              Pageable pageable) {

        // 대소문자 구분 없이 enum 변환
        Category categoryEnum = parseCategory(category);

        Page<BoardListResDto> resDto = adminBoardService.findInformationBoardList(boardSearchDto, categoryEnum, pageable);
        return new ResponseEntity<>(resDto, HttpStatus.OK);
    }

    /**
     * 분양 동물 게시판 목록 조회
     * @param searchDto 검색 조건 (ex: 품종, 지역 등)
     * @param pageable 페이지 정보 (size, page, sort 등)
     * @return 페이징된 동물 목록 데이터 (AnimalListResDto)
     */
    @GetMapping("/list")
    public ResponseEntity<CommonResDto> getAnimalList(AnimalSearchDto searchDto, Pageable pageable) {
        Page<AnimalListResDto> result = adminBoardService.findStrayAnimalList(searchDto, pageable);
        CommonResDto resDto = new CommonResDto(HttpStatus.OK, "목록 조회", result);
        return new ResponseEntity<>(resDto, HttpStatus.OK);
    }

    /**
     *
     * @param category
     * @param postId
     * @param authHeader
     * @param request
     * @return
     */
    // 게시물 상세 조회
    @GetMapping("/detail/{category}/{id}")
    public ResponseEntity<CommonResDto> getBoardDetail(@PathVariable String category,
                                            @PathVariable(name = "id") Long postId,
                                            @RequestHeader(value = "Authorization", required = false) String authHeader,
                                            HttpServletRequest request) {

        // 대소문자 구분 없이 enum 변환
        Category categoryEnum = parseCategory(category);

        String email = null;
        // Authorization 헤더가 존재하고 Bearer로 시작하는 경우
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7); // "Bearer " 이후의 토큰만 추출
            try {
                // 토큰에서 이메일 추출
                email = jwtTokenProvider.extractEmail(token);
            } catch (Exception e) {
                // JWT 파싱 실패 시 로그 기록 (비로그인 사용자로 처리)
                e.printStackTrace();
            }
        }

        CommonResDto resDto = adminBoardService.boardDetail(categoryEnum, postId, email, request);

        return new  ResponseEntity<>(resDto, HttpStatus.OK);
    }


    /**
     * 분양 게시물 상세 조회
     * @param postId 게시물 ID
     * @param authHeader Authorization 헤더 (Bearer {accessToken})
     * @param request 클라이언트 요청 정보(IP, 브라우저 등 추출용)
     * @return Animal 상세 정보
     */
    @GetMapping("/detail/{postId}")
    public ResponseEntity<CommonResDto> getAnimal(@PathVariable Long postId,
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
        Animal result = adminBoardService.findByAnimal(postId, email, request);
        CommonResDto resDto = new CommonResDto(HttpStatus.OK, "상세 조회", result);
        return new ResponseEntity<>(resDto, HttpStatus.OK);
    }

    /**
     *
     * @param category
     * @param postId
     * @param adminInfo
     * @return
     */
    // 삭제
    @DeleteMapping("/{category}/delete/{postId}")
    public ResponseEntity<CommonResDto> deleteBoard(@PathVariable String category,
                                         @PathVariable Long postId,
                                         @AuthenticationPrincipal TokenAdminInfo adminInfo) {

        // 대소문자 구분 없이 enum 변환
        Category categoryEnum = parseCategory(category);

        adminBoardService.deleteBoard(postId, categoryEnum, adminInfo);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    /**
     * URL 경로 변수로 들어온 문자열 category를 Category Enum 으로 변환한다.
     * 변환에 실패하면 BAD_REQUEST 예외를 발생시킨다.
     *
     * @param category 문자열 카테고리 (예: "FREE", "INTRODUCTION", "QUESTION", "REVIEW", "ANIMAL")
     * @return 변환된 Category Enum
     * @throws CommonException 변환 실패 시 발생 (BAD_REQUEST)
     */
    private Category parseCategory(String category) {
        try {
            return Category.valueOf(category.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new CommonException(ErrorCode.BAD_REQUEST);
        }
    }
}
