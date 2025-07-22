package com.playdata.mapservice.map.entity.CultureDetail;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.playdata.mapservice.map.entity.AddressCode;
import com.playdata.mapservice.map.entity.HospitalAddressCode;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Getter
@AllArgsConstructor
public enum CultureAddressCode {

    @JsonProperty("1")
    SEOUL("서울특별시", "1"),

    @JsonProperty("2")
    INCHEON("인천광역시", "2"),

    @JsonProperty("3")
    DAEJEON("대전광역시", "3"),

    @JsonProperty("4")
    DAEGU("대구광역시", "4"),

    @JsonProperty("5")
    GWANGJU("광주광역시", "5"),

    @JsonProperty("6")
    BUSAN("부산광역시", "6"),

    @JsonProperty("7")
    ULSAN("울산광역시", "7"),

    @JsonProperty("8")
    SEJONG("세종특별자치시", "8"),

    @JsonProperty("31")
    GYEONGGI("경기도", "31"),

    @JsonProperty("32")
    GANGWON("강원도", "32"),

    @JsonProperty("33")
    CHUNGBUK("충청북도", "33"),

    @JsonProperty("34")
    CHUNGNAM("충청남도", "34"),

    @JsonProperty("35")
    GYEONGBUK("경상북도", "35"),

    @JsonProperty("36")
    GYEONGNAM("경상남도", "36"),

    @JsonProperty("37")
    JEONBUK("전라북도", "37"),

    @JsonProperty("38")
    JEONNAM("전라남도", "38"),

    @JsonProperty("39")
    JEJU("제주특별자치도", "39");

    // 한글 설명
    private final String desc;

    // DB나 JSON에서 들어오는 코드값
    private final String code;

    // 코드 → enum 값 매핑용 Map
    private static final Map<String, CultureAddressCode> CODE_MAP = Stream.of(values())
            .collect(Collectors.toMap(CultureAddressCode::getCode, Function.identity()));

    /**
     * JSON 문자열을 enum으로 변환할 때 사용하는 메서드
     * 예: "Y" → ContentType.Y
     */
    @JsonCreator
    public static CultureAddressCode from(String value) {
        if (value == null || value.isEmpty()) {
            return null;
        }
        return Optional.ofNullable(CODE_MAP.get(value))
                .orElseThrow(() -> new IllegalArgumentException("Invalid ContentType: " + value));
    }

}
