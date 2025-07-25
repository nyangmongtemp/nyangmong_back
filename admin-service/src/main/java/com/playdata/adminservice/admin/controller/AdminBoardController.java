package com.playdata.adminservice.admin.controller;

import com.playdata.adminservice.admin.dto.req.AdminBoardReqDto;
import com.playdata.adminservice.admin.service.AdminBoardService;
import com.playdata.adminservice.common.dto.CommonResDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * AdminBoardController
 * - 관리자 게시판 통합 관리 컨트롤러
 * - 정보게시판, 소개게시판, 유기동물게시판을 통합적으로 조회/상세/삭제 처리
 * - 공통 응답 포맷 (CommonResDto) 사용
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/boards")
public class AdminBoardController {

    private final AdminBoardService adminBoardService;

    /**
     * 게시글 리스트 조회 (카테고리/제목/작성자/날짜/정렬 기준 등 필터 포함)
     *
     * @param cond     게시글 검색 조건 DTO (카테고리, 키워드 등)
     * @param pageable 페이지네이션 및 정렬 정보
     * @return CommonResDto<Page<AdminBoardResDto>>
     */
    @GetMapping
    public ResponseEntity<CommonResDto> getBoards(
            @ModelAttribute AdminBoardReqDto cond,
            Pageable pageable
    ) {
        CommonResDto result = adminBoardService.getBoards(cond, pageable);
        return ResponseEntity.ok(result);
    }

    /**
     * 게시글 상세 조회
     *
     * @param category 게시글 카테고리 (INFORMATION, INTRODUCTION, ANIMAL 등)
     * @param postId   게시글 ID
     * @return CommonResDto<AdminBoardResDto>
     */
    @GetMapping("/{category}/{postId}")
    public ResponseEntity<CommonResDto> findDetail(
            @PathVariable String category,
            @PathVariable Long postId
    ) {
        CommonResDto result = adminBoardService.getBoardDetail(category, postId);
        return ResponseEntity.ok(result);
    }

    /**
     * 게시글 삭제 (soft delete - active = false 처리)
     *
     * @param category 게시글 카테고리
     * @param postId   게시글 ID
     * @return CommonResDto<Void>
     */
    @DeleteMapping("/{category}/{postId}")
    public ResponseEntity<CommonResDto> deleteBoard(
            @PathVariable String category,
            @PathVariable Long postId
    ) {
        CommonResDto result = adminBoardService.deleteBoard(category, postId);
        return ResponseEntity.ok(result);
    }
}