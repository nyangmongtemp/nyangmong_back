package com.playdata.animalboardservice.entity;

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
 * 성별 구분 - M(수컷), F(암컷), Q(미상)
 */
@Getter
@AllArgsConstructor
public enum SexCode {

    @JsonProperty("M")
    M("수컷", "M"),

    @JsonProperty("F")
    F("암컷", "F"),

    @JsonProperty("Q")
    Q("미상", "Q");

    // 한글 설명
    private final String desc;

    // DB나 JSON에서 들어오는 코드값
    private final String code;

    // 코드 → enum 값 매핑용 Map
    private static final Map<String, SexCode> CODE_MAP = Stream.of(values())
            .collect(Collectors.toMap(SexCode::getCode, Function.identity()));

    /**
     * JSON 문자열을 enum으로 변환할 때 사용하는 메서드
     * 예: "M" → SexCode.M
     */
    @JsonCreator
    public static SexCode from(String value) {
        return Optional.ofNullable(CODE_MAP.get(value))
                .orElseThrow(() -> new IllegalArgumentException("Invalid SexCode: " + value));
    }
}