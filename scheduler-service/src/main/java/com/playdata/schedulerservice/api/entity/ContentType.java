package com.playdata.schedulerservice.api.entity;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 컨텐츠 구분
 */
@Getter
@AllArgsConstructor
public enum ContentType {

    @JsonProperty("12")
    TOURIST("관광지", "12"),

    @JsonProperty("14")
    CULTURAL("문화시설", "14"),

    @JsonProperty("15")
    EVENT("축제공연행사", "15"),

    @JsonProperty("28")
    SPORTS("레포츠", "28"),

    @JsonProperty("32")
    ACCOMMODATION("숙박", "32"),

    @JsonProperty("38")
    SHOPPING("쇼핑", "38"),

    @JsonProperty("39")
    RESTAURANT("음식점", "39");

    // 한글 설명
    private final String desc;

    // DB나 JSON에서 들어오는 코드값
    private final String code;

    // 코드 → enum 값 매핑용 Map
    private static final Map<String, ContentType> CODE_MAP = Stream.of(values())
            .collect(Collectors.toMap(ContentType::getCode, Function.identity()));

    /**
     * JSON 문자열을 enum으로 변환할 때 사용하는 메서드
     * 예: "Y" → ContentType.Y
     */
    @JsonCreator
    public static ContentType from(String value) {
        if (value == null || value.isEmpty()) {
            return null;
        }
        return Optional.ofNullable(CODE_MAP.get(value))
                .orElseThrow(() -> new IllegalArgumentException("Invalid ContentType: " + value));
    }

}