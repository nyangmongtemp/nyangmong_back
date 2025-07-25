package com.playdata.adminservice.admin.repository.custom.Impl;

import com.playdata.adminservice.admin.dto.req.AdminBoardReqDto;
import com.playdata.adminservice.admin.dto.res.AdminBoardResDto;
import com.playdata.adminservice.admin.entity.Category;
import com.playdata.adminservice.admin.entity.QAnimal;
import com.playdata.adminservice.admin.entity.QInformationBoard;
import com.playdata.adminservice.admin.entity.QIntroductionBoard;
import com.playdata.adminservice.admin.repository.custom.AdminBoardCustomRepository;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.*;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static com.playdata.adminservice.admin.entity.QInformationBoard.informationBoard;
import static com.playdata.adminservice.admin.entity.QIntroductionBoard.introductionBoard;
import static com.querydsl.core.types.dsl.Expressions.constant;

/**
 * 게시판 통합 검색 및 상세 조회를 위한 QueryDSL Custom Repository 구현체
 */
@RequiredArgsConstructor
public class AdminBoardCustomRepositoryImpl implements AdminBoardCustomRepository {

    private final JPAQueryFactory queryFactory;

    // QueryDSL Q타입 단축 변수
    QInformationBoard info = informationBoard;
    QIntroductionBoard intro = introductionBoard;
    QAnimal animal = QAnimal.animal;

    /**
     * 게시판 통합 검색 (조건 기반 + 페이징 + 정렬)
     */
    @Override
    public Page<AdminBoardResDto> search(AdminBoardReqDto cond, Pageable pageable) {
        String categoryStr = cond.getCategory();

        // 1. category 값 전처리 (공백 제거, 빈 문자열 → null 처리)
        if (categoryStr != null) {
            categoryStr = categoryStr.trim();
            if (categoryStr.isEmpty()) {
                categoryStr = null;
            }
        }

        // 2. 문자열 category → Enum(Category) 변환
        Category categoryEnum = null;
        if (categoryStr != null) {
            try {
                categoryEnum = Category.valueOf(categoryStr.toUpperCase());
                System.out.println(">>> categoryEnum 변환 성공: " + categoryEnum);
            } catch (IllegalArgumentException e) {
                // 존재하지 않는 카테고리 입력 → 빈 결과 반환
                System.out.println(">>> categoryEnum 변환 실패: " + e.getMessage());
                return new PageImpl<>(Collections.emptyList(), pageable, 0);
            }
        } else {
            System.out.println(">>> 카테고리 없음, 전체 게시판 조회");
        }

        /**
         * 3. INFORMATION 게시판의 경우, 하위 카테고리도 함께 조회
         * (EX: INFORMATION -> GUIDE, NOTICE 등 포함)
         */
        List<Category> infoCategories = null;
        if (categoryEnum == null) {
            infoCategories = null; // 전체 조회
        } else if ("INFORMATION".equals(categoryEnum.getTableCategory())) {
            infoCategories = Category.getSubCategoriesOf(categoryEnum); // 하위 카테고리 목록
        } else {
            infoCategories = Collections.emptyList(); // INTRODUCTION/ANIMAL은 제외
        }

        /**
         * 4. INFORMATION 게시판 조건 구성 및 조회
         */
        BooleanExpression infoCategoryCondition = (infoCategories == null) ? null : info.category.in(infoCategories);
        BooleanExpression infoTitleCondition = containsTitle(cond.getTitle(), info.title);
        BooleanExpression infoUserCondition = eqUserId(cond.getUserId(), info.userId);
        BooleanExpression infoActiveCondition = eqActive(cond.getActive(), info.active);

        List<AdminBoardResDto> infoList = queryFactory
                .select(Projections.constructor(AdminBoardResDto.class,
                        info.postId,
                        info.category.stringValue(),
                        info.userId,
                        info.nickname,
                        info.title,
                        info.content,
                        info.thumbnailImage,
                        info.viewCount,
                        info.active,
                        info.createAt
                ))
                .from(info)
                .where(
                        infoTitleCondition,
                        infoCategoryCondition,
                        infoUserCondition,
                        infoActiveCondition
                )
                .fetch();

        /**
         * 5. INTRODUCTION 게시판 조건 구성 및 조회
         */
        BooleanExpression introCategoryCondition = (categoryEnum == null)
                ? null
                : (categoryEnum.getTableCategory().equals(Category.INTRODUCTION.getTableCategory())
                ? Expressions.TRUE // INTRODUCTION이면 모두 포함
                : Expressions.FALSE); // 아니면 제외

        BooleanExpression introTitleCondition = containsTitle(cond.getTitle(), intro.title);
        BooleanExpression introUserCondition = eqUserId(cond.getUserId(), intro.userId);
        BooleanExpression introActiveCondition = eqActive(cond.getActive(), intro.active);

        List<AdminBoardResDto> introList = queryFactory
                .select(Projections.constructor(AdminBoardResDto.class,
                        intro.postId,
                        constant(Category.INTRODUCTION.getTableCategory()), // 카테고리 상수 처리
                        intro.userId,
                        intro.nickname,
                        intro.title,
                        intro.content,
                        intro.thumbnailImage,
                        intro.viewCount,
                        intro.active,
                        intro.createAt
                ))
                .from(intro)
                .where(
                        introTitleCondition,
                        introCategoryCondition,
                        introUserCondition,
                        introActiveCondition
                )
                .fetch();

        /**
         * 6. ANIMAL 게시판 조건 구성 및 조회
         */
        BooleanExpression animalCategoryCondition = (categoryEnum == null)
                ? null
                : (categoryEnum.getTableCategory().equals(Category.ANIMAL.getTableCategory())
                ? Expressions.TRUE
                : Expressions.FALSE);

        BooleanExpression animalTitleCondition = containsTitle(cond.getTitle(), animal.title);
        BooleanExpression animalUserCondition = eqUserId(cond.getUserId(), animal.userId);
        BooleanExpression animalActiveCondition = eqActive(cond.getActive(), animal.active);

        List<AdminBoardResDto> animalList = queryFactory
                .select(Projections.constructor(AdminBoardResDto.class,
                        animal.postId,
                        constant(Category.ANIMAL.getTableCategory()), // 카테고리 상수 처리
                        animal.userId,
                        animal.nickname,
                        animal.title,
                        animal.content,
                        animal.thumbnailImage,
                        animal.viewCount,
                        animal.active,
                        animal.createAt
                ))
                .from(animal)
                .where(
                        animalTitleCondition,
                        animalCategoryCondition,
                        animalUserCondition,
                        animalActiveCondition
                )
                .fetch();

        /**
         * 7. 조회된 결과 병합
         */
        List<AdminBoardResDto> merged = new ArrayList<>();
        merged.addAll(infoList);
        merged.addAll(introList);
        merged.addAll(animalList);

        /**
         * 8. 정렬 조건 처리
         */
        String rawSortBy = cond.getSortBy();
        final String sortBy = (rawSortBy == null || rawSortBy.isEmpty()) ? "latest" : rawSortBy;

        // 생성일, 제목 기준 정렬
        merged.sort((a, b) -> {
            switch (sortBy.toLowerCase()) {
                case "oldest":
                    return a.getCreatedAt().compareTo(b.getCreatedAt());
                case "title":
                    return a.getTitle().compareToIgnoreCase(b.getTitle());
                case "latest":
                default:
                    return b.getCreatedAt().compareTo(a.getCreatedAt());
            }
        });

        /**
         * 9. 페이징 처리
         */
        int start = (int) pageable.getOffset(); // 현재 페이지의 시작 인덱스
        int end = Math.min(start + pageable.getPageSize(), merged.size()); // 마지막 인덱스 (리스트 크기 초과 방지)
        List<AdminBoardResDto> pageContent = start >= merged.size() ? Collections.emptyList() : merged.subList(start, end);

        return new PageImpl<>(pageContent, pageable, merged.size());
    }

    // ======== 조건절 생성 유틸 메서드 ========

    /**
     * 제목 검색 조건
     */
    private BooleanExpression containsTitle(String title, StringPath path) {
        return (title == null || title.isEmpty()) ? null : path.containsIgnoreCase(title);
    }

    /**
     * 작성자 ID 검색 조건
     */
    private BooleanExpression eqUserId(Long userId, NumberPath<Long> userIdPath) {
        return (userId == null) ? null : userIdPath.eq(userId);
    }

    /**
     * 활성화 여부 검색 조건
     */
    private BooleanExpression eqActive(Boolean active, BooleanPath activePath) {
        return (active == null) ? null : activePath.eq(active);
    }

    /**
     * 게시글 상세 조회 (카테고리에 따라 다른 테이블에서 조회)
     */
    @Override
    public AdminBoardResDto findBoardDetailById(String category, Long postId) {
        return switch (category.toUpperCase()) {
            case "INFORMATION" -> queryFactory
                    .select(Projections.constructor(AdminBoardResDto.class,
                            informationBoard.postId,
                            informationBoard.category.stringValue(),
                            informationBoard.userId,
                            informationBoard.nickname,
                            informationBoard.title,
                            informationBoard.content,
                            informationBoard.thumbnailImage,
                            informationBoard.viewCount,
                            informationBoard.active,
                            informationBoard.createAt
                    ))
                    .from(informationBoard)
                    .where(informationBoard.postId.eq(postId))
                    .fetchOne();

            case "INTRODUCTION" -> queryFactory
                    .select(Projections.constructor(AdminBoardResDto.class,
                            introductionBoard.postId,
                            introductionBoard.title.stringValue().as("category"), // category가 없으므로 title 활용
                            introductionBoard.userId,
                            introductionBoard.nickname,
                            introductionBoard.title,
                            introductionBoard.content,
                            introductionBoard.thumbnailImage,
                            introductionBoard.viewCount,
                            introductionBoard.active,
                            introductionBoard.createAt
                    ))
                    .from(introductionBoard)
                    .where(introductionBoard.postId.eq(postId))
                    .fetchOne();

            case "ANIMAL" -> queryFactory
                    .select(Projections.constructor(AdminBoardResDto.class,
                            animal.postId,
                            animal.petCategory.stringValue().as("category"),
                            animal.userId,
                            animal.nickname,
                            animal.title,
                            animal.content,
                            animal.thumbnailImage,
                            animal.viewCount,
                            animal.active,
                            animal.createAt
                    ))
                    .from(animal)
                    .where(animal.postId.eq(postId))
                    .fetchOne();

            default -> throw new IllegalArgumentException("지원하지 않는 게시판 카테고리입니다.");
        };
    }
}