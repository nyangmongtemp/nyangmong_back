package com.playdata.schedulerservice.api.util;

import com.playdata.schedulerservice.api.entity.AnimalHospital;
import com.playdata.schedulerservice.api.entity.PetCulture;
import org.springframework.batch.item.file.mapping.FieldSetMapper;
import org.springframework.batch.item.file.transform.FieldSet;
import org.springframework.validation.BindException;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class PetCultureFielSetMapper implements FieldSetMapper<PetCulture> {

    // yyyy-MM-dd 형식의 날짜를 처리하기 위한 포맷터
    private final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    // yyyy-MM-dd HH:mm:ss 형식의 날짜-시간을 처리하기 위한 포맷터
    private final DateTimeFormatter dateTimeFormatterWithSeconds = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    // yyyy-MM-dd HH:mm 형식의 날짜-시간(초 없음)을 처리하기 위한 포맷터
    private final DateTimeFormatter dateTimeFormatterWithoutSeconds = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    /**
     * CSV의 한 줄(FieldSet)을 받아 AnimalHospital 객체로 매핑합니다.
     */
    @Override
    public PetCulture mapFieldSet(FieldSet fieldSet) throws BindException {
        PetCulture petCulture = new PetCulture();

        petCulture.setFacilityName(fieldSet.readString(0));         // 시설명
        petCulture.setCategoryOne(fieldSet.readString(1));          // 카테고리 1명
        petCulture.setCategoryTwo(fieldSet.readString(2));          // 카테고리 2명
        petCulture.setCategoryThree(fieldSet.readString(3));        // 카테고리 3명
        petCulture.setSido(fieldSet.readString(4));                 // 시도명
        petCulture.setSigungu(fieldSet.readString(5));              // 시군구명
        petCulture.setLegalDong(fieldSet.readString(6));            // 법정동명
        petCulture.setLiName(fieldSet.readString(7));               // 리명
        petCulture.setLnbrNo(fieldSet.readString(8));               // 번지번호
        petCulture.setRoadName(fieldSet.readString(9));             // 도로명
        petCulture.setBuildingNo(fieldSet.readString(10));          // 건물번호
        petCulture.setMapx(parseDouble(fieldSet.readString(11)));   // 위치위도
        petCulture.setMapy(parseDouble(fieldSet.readString(12)));   // 위치경도
        petCulture.setZipNo(fieldSet.readString(13));               // 우편번호
        petCulture.setRoadAddress(fieldSet.readString(14));         // 도로명주소명
        petCulture.setFullAddress(fieldSet.readString(15));         // 지번주소
        petCulture.setTelNum(fieldSet.readString(16));              // 전화번호
        petCulture.setUrl(fieldSet.readString(17));                 // 홈페이지 URL
        petCulture.setRestInfo(fieldSet.readString(18));            // 휴무일안내내용
        petCulture.setOperTime(fieldSet.readString(19));            // 운영시간
        petCulture.setParking(fieldSet.readString(20));             // 주차가능여부
        petCulture.setPrice(fieldSet.readString(21));               // 이용가격내용
        petCulture.setPetWith(fieldSet.readString(22));             // 반려동물가능여부
        petCulture.setPetInfo(fieldSet.readString(23));             // 반려동물정보내용
        petCulture.setPetSize(fieldSet.readString(24));             // 입장가능반려동물크기값
        petCulture.setPetRestrict(fieldSet.readString(25));         // 반려동물제한사항내용
        petCulture.setInPlace(fieldSet.readString(26));             // 내부장소동반가능여부
        petCulture.setOutPlace(fieldSet.readString(27));            // 외부장소동반가능여부
        petCulture.setInfoDesc(fieldSet.readString(28));                // 시설정보설명
        petCulture.setExtraFee(fieldSet.readString(29));            // 반려동물동반추가요금값
        petCulture.setLastUpdate(parseLocalDate(fieldSet.readString(30)));  // 최종수정일자

        return petCulture;
    }

    /**
     * 날짜 문자열을 LocalDate로 파싱 (예: "2025-07-01")
     * 파싱 실패 시 null 반환
     */
    private LocalDate parseLocalDate(String value) {
        if (value != null && !value.isBlank()) {
            try {
                return LocalDate.parse(value, dateFormatter);
            } catch (DateTimeParseException ignored) {
                // 날짜 형식이 잘못된 경우 null로 반환 (무시)
            }
        }
        return null;
    }

    /**
     * 날짜-시간 문자열을 LocalDateTime으로 파싱
     * 초가 포함된 형식 또는 생략된 형식 모두 지원
     */
    private LocalDateTime parseLocalDateTime(String value) {
        if (value != null && !value.isBlank()) {
            try {
                return LocalDateTime.parse(value, dateTimeFormatterWithSeconds);
            } catch (DateTimeParseException e) {
                try {
                    return LocalDateTime.parse(value, dateTimeFormatterWithoutSeconds);
                } catch (DateTimeParseException ignored) {
                    // 둘 다 실패하면 null 반환
                }
            }
        }
        return null;
    }

    /**
     * 문자열을 Double로 변환
     * 실패하면 null 반환
     */
    private Double parseDouble(String value) {
        if (value != null && !value.isBlank()) {
            try {
                return Double.parseDouble(value);
            } catch (NumberFormatException ignored) {
                // 숫자 형식이 아니면 null
            }
        }
        return null;
    }

    /**
     * 문자열을 Integer로 변환
     * 실패하면 null 반환
     */
    private Integer parseInteger(String value) {
        if (value != null && !value.isBlank()) {
            try {
                return Integer.parseInt(value);
            } catch (NumberFormatException ignored) {
                // 숫자 형식이 아니면 null
            }
        }
        return null;
    }

}
