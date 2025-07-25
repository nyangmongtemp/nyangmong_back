package com.playdata.adminservice.admin.service;

import com.playdata.adminservice.admin.dto.req.AdminBoardReqDto;
import com.playdata.adminservice.admin.dto.res.AdminBoardResDto;
import com.playdata.adminservice.admin.entity.Category;
import com.playdata.adminservice.admin.repository.AdminBoardRepository;
import com.playdata.adminservice.admin.repository.AnimalRepository;
import com.playdata.adminservice.admin.repository.InformationBoardRepository;
import com.playdata.adminservice.admin.repository.IntroductionBoardRepository;
import com.playdata.adminservice.common.dto.CommonResDto;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

/**
 * AdminBoardService
 * - 관리자용 통합 게시판 서비스 클래스
 * - 정보/소개/유기동물 게시판을 통합 조회, 상세, 삭제 처리
 */
@Service
@RequiredArgsConstructor
public class AdminBoardService {

    private final AdminBoardRepository adminBoardRepository;
    private final InformationBoardRepository informationBoardRepository;
    private final IntroductionBoardRepository introductionBoardRepository;
    private final AnimalRepository animalRepository;

    /**
     * 게시글 목록 조회 (검색 조건 + 페이징 처리)
     *
     * @param cond     검색 조건 DTO (카테고리, 제목, 작성자, 날짜, 정렬기준 등)
     * @param pageable 페이징 및 정렬 정보
     * @return CommonResDto<Page<AdminBoardResDto>> 게시글 목록 응답
     */
    public CommonResDto getBoards(AdminBoardReqDto cond, Pageable pageable) {
        Page<AdminBoardResDto> boards = adminBoardRepository.search(cond, pageable);
        return new CommonResDto(HttpStatus.OK, "게시글 목록 조회 완료", boards);
    }

    /**
     * 게시글 상세 조회
     *
     * @param category 게시판 카테고리 (INFORMATION, INTRODUCTION, ANIMAL)
     * @param postId   게시글 ID
     * @return CommonResDto<AdminBoardResDto> 게시글 상세 정보 응답
     */
    @Transactional
    public CommonResDto getBoardDetail(String category, Long postId) {
        AdminBoardResDto detail = adminBoardRepository.findBoardDetailById(category, postId);
        return new CommonResDto(HttpStatus.OK, "게시글 상세 조회 완료", detail);
    }

    /**
     * 게시글 삭제 처리 (soft delete)
     * - active 필드를 false로 설정하여 비활성화
     * - 존재하지 않는 게시글이나 잘못된 카테고리 처리 시 예외 발생
     *
     * @param category 게시판 카테고리 (INFORMATION, INTRODUCTION, ANIMAL)
     * @param postId   게시글 ID
     * @return CommonResDto<Void> 삭제 완료 메시지
     */
    @Transactional
    public CommonResDto deleteBoard(String category, Long postId) {
        // 문자열 category를 enum으로 변환
        Category enumCategory = Category.from(category);
        String tableCategory = enumCategory.getTableCategory();

        // 카테고리별 분기 처리
        switch (tableCategory) {
            case "INFORMATION" -> {
                informationBoardRepository.findById(postId).ifPresentOrElse(
                        board -> board.setActive(false), // soft delete 처리
                        () -> { throw new IllegalArgumentException("게시글을 찾을 수 없습니다."); }
                );
            }
            case "INTRODUCTION" -> {
                introductionBoardRepository.findById(postId).ifPresentOrElse(
                        board -> board.setActive(false),
                        () -> { throw new IllegalArgumentException("게시글을 찾을 수 없습니다."); }
                );
            }
            case "ANIMAL" -> {
                animalRepository.findById(postId).ifPresentOrElse(
                        animal -> animal.setActive(false),
                        () -> { throw new IllegalArgumentException("게시글을 찾을 수 없습니다."); }
                );
            }
            default -> throw new IllegalArgumentException("지원하지 않는 카테고리입니다.");
        }

        return new CommonResDto(HttpStatus.OK, "게시글 삭제 완료", null);
    }

}