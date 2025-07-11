package com.playdata.boardservice.board.entity;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component // spring이 자동 인식하도록 component 등록
public class CategoryConverter implements Converter<String, Category> {
    @Override
    public Category convert(String category) {
        // 들어온 문자열을 대문자로 바꾼 후, 해당하는 Category enum 값으로 변환
        return Category.valueOf(category.toUpperCase());
    }
}
