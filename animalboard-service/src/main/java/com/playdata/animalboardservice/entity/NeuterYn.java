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
 * 중성화 구분 - Y(중성화), N(비중성화), U(미상)
 */
@Getter
@AllArgsConstructor
public enum NeuterYn {

    @JsonProperty("Y")
    Y("중성화", "Y"),

    @JsonProperty("N")
    N("비중성화", "N"),

    @JsonProperty("U")
    U("미상", "U");

    // 한글 설명
    private final String desc;

    // DB나 JSON에서 들어오는 코드값
    private final String code;

    // 코드 → enum 값 매핑용 Map
    private static final Map<String, NeuterYn> CODE_MAP = Stream.of(values())
            .collect(Collectors.toMap(NeuterYn::getCode, Function.identity()));

    /**
     * JSON 문자열을 enum으로 변환할 때 사용하는 메서드
     * 예: "Y" → NeuterYn.Y
     */
    @JsonCreator
    public static NeuterYn from(String value) {
        return Optional.ofNullable(CODE_MAP.get(value))
                .orElseThrow(() -> new IllegalArgumentException("Invalid NeuterYn: " + value));
    }

}
