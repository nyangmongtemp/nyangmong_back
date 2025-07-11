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
 * 예약 구분 - A(예약가능), R(에약중), C(분양완료)
 */
@Getter
@AllArgsConstructor
public enum ReservationStatus {

    @JsonProperty("A")
    A("예약가능", "A"),

    @JsonProperty("R")
    R("예약중", "R"),

    @JsonProperty("C")
    C("분양완료", "C");

    // 한글 설명
    private final String desc;

    // DB나 JSON에서 들어오는 코드값
    private final String code;

    // 코드 → enum 값 매핑용 Map
    private static final Map<String, ReservationStatus> CODE_MAP = Stream.of(values())
            .collect(Collectors.toMap(ReservationStatus::getCode, Function.identity()));

    /**
     * JSON 문자열을 enum으로 변환할 때 사용하는 메서드
     * 예: "Y" → NeuterYn.Y
     */
    @JsonCreator
    public static ReservationStatus from(String value) {
        return Optional.ofNullable(CODE_MAP.get(value))
                .orElseThrow(() -> new IllegalArgumentException("Invalid ReservationStatus: " + value));
    }

}
