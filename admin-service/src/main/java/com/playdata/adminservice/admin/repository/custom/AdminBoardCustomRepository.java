package com.playdata.adminservice.admin.repository.custom;

import com.playdata.adminservice.admin.dto.req.AdminBoardReqDto;
import com.playdata.adminservice.admin.dto.res.AdminBoardResDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * AdminBoard에 대한 사용자 정의 리포지토리 인터페이스
 * - QueryDSL 또는 복잡한 커스텀 쿼리 기능 제공을 위해 사용
 */
public interface AdminBoardCustomRepository {

    /**
     * 게시판 카테고리와 게시글 ID를 기반으로 단건 상세 조회
     *
     * @param category 게시판 카테고리 (예: "INFORMATION", "INTRODUCTION", "ANIMAL")
     * @param postId   게시글 ID
     * @return AdminBoardResDto 게시글 상세 정보 DTO (엔티티를 직접 반환하지 않고 DTO로 매핑)
     */
    AdminBoardResDto findBoardDetailById(String category, Long postId);

    /**
     * 게시판 조건 검색 및 페이징 결과 반환
     * - 제목, 작성자 ID, 활성 여부, 카테고리 등의 조건으로 필터링
     * - 게시판 종류(INFORMATION, INTRODUCTION, ANIMAL) 통합 검색
     * - 정렬 및 페이징 처리 포함
     *
     * @param cond     검색 조건이 담긴 DTO (AdminBoardReqDto)
     * @param pageable Spring Data의 페이징 객체 (페이지 번호, 사이즈, 정렬 기준 포함)
     * @return 조건에 맞는 게시글 DTO들의 페이징 결과 (Page<AdminBoardResDto>)
     */
    Page<AdminBoardResDto> search(AdminBoardReqDto cond, Pageable pageable);
}