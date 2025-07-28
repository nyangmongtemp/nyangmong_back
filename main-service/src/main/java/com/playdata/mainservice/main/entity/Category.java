package com.playdata.mainservice.main.entity;


public enum Category {

    QUESTION,
    FREE,
    REVIEW,
    ADOPT,
    INTRODUCTION;

    // 소문자 입력을 enum으로 변환하는 메서드
    public static Category fromString(String input) {
        for (Category category : Category.values()) {
            if (category.name().equalsIgnoreCase(input)) {
                return category;
            }
        }
        throw new IllegalArgumentException("존재하지 않는 카테고리입니다: " + input);
    }

}
