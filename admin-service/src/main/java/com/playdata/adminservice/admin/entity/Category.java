package com.playdata.adminservice.admin.entity;

import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.Getter;

import java.util.List;

/**
 * 게시판 카테고리 Enum
 * - 각 카테고리는 실제 게시판 종류를 의미
 * - 일부 카테고리는 동일한 테이블(INFORMATION)에 속함
 * - ANIMAL, INTRODUCTION은 독립 테이블
 * - INFORMATION은 실제 소분류가 아닌 검색용 가상 카테고리
 */
@Getter
public enum Category {

    // === INFORMATION 테이블에 속한 실제 카테고리들 ===
    QUESTION("INFORMATION"),     // 질문 게시판
    REVIEW("INFORMATION"),       // 후기 게시판
    FREE("INFORMATION"),         // 자유 게시판

    // === 독립 테이블 게시판 ===
    INTRODUCTION("INTRODUCTION"), //  소개 게시판
    ANIMAL("ANIMAL"),             //  동물 게시판

    // === 검색 전용 가상 카테고리 (대분류로 사용됨) ===
    INFORMATION("INFORMATION");   // 검색용 대분류

    private final String tableCategory;

    /**
     * @param tableCategory 게시판의 실제 테이블 명
     */
    Category(String tableCategory) {
        this.tableCategory = tableCategory;
    }

    /**
     * 문자열을 enum으로 변환 (대소문자 무시)
     * 예: "review" -> Category.REVIEW
     */
    @JsonCreator
    public static Category from(String value) {
        return Category.valueOf(value.toUpperCase());
    }

    /**
     * 해당 enum이 검색용 가상 카테고리(INFORMATION)인지 여부
     * @return true이면 검색용 대분류 (INFORMATION)
     */
    public boolean isSearchCategory() {
        return this == INFORMATION;
    }

    /**
     * 이 카테고리가 지정된 테이블에 속하는지 확인
     * 예: REVIEW -> belongsTo("INFORMATION") → true
     */
    public boolean isBelongTo(String table) {
        return this.tableCategory.equalsIgnoreCase(table);
    }

    /**
     * 대분류(INFORMATION)에 해당하는 소분류 카테고리 목록 반환
     * - getSubCategoriesOf(INFORMATION) → [QUESTION, REVIEW, FREE]
     * - getSubCategoriesOf(REVIEW) → [REVIEW]
     * - getSubCategoriesOf(ANIMAL) → []
     *
     * @param category 검색 기준이 되는 카테고리
     * @return 소속된 하위 카테고리 리스트
     */
    public static List<Category> getSubCategoriesOf(Category category) {
        if (category == INFORMATION) {
            return List.of(QUESTION, REVIEW, FREE);
        } else if ("INFORMATION".equals(category.getTableCategory())) {
            return List.of(category);
        }
        return List.of(); // ANIMAL, INTRODUCTION 등은 하위 없음
    }
}