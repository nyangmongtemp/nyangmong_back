package com.playdata.boardservice.board.entity;

import com.fasterxml.jackson.annotation.JsonCreator;

public enum Category {

    QUESTION, // 질문
    REVIEW, // 후기
    FREEDOM, // 자유
    INTRODUCTION; // 소개

    @JsonCreator
    public static Category from(String value) {
        return Category.valueOf(value.toUpperCase());
    }

}
