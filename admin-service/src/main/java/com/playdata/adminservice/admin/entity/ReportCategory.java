package com.playdata.adminservice.admin.entity;

public enum ReportCategory {

    COMMENT,    // 댓글
    BOARD,    // 게시물
    CHAT;    // 쪽지

    // 소문자 입력을 enum으로 변환하는 메서드
    public static ReportCategory fromString(String input) {
        for (ReportCategory category : ReportCategory.values()) {
            if (category.name().equalsIgnoreCase(input)) {
                return category;
            }
        }
        throw new IllegalArgumentException("존재하지 않는 신고 카테고리입니다: " + input);
    }

}
