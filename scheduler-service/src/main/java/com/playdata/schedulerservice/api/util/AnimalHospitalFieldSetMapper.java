package com.playdata.schedulerservice.api.util;

import com.playdata.schedulerservice.api.entity.AnimalHospital;
import org.springframework.batch.item.file.mapping.FieldSetMapper;
import org.springframework.batch.item.file.transform.FieldSet;
import org.springframework.validation.BindException;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

/**
 * AnimalHospitalFieldSetMapper
 * CSV 파일에서 읽은 각 행(row)을 AnimalHospital 객체로 변환해주는 클래스입니다.
 * 스프링 배치의 FlatFileItemReader와 함께 사용됩니다.
 */
public class AnimalHospitalFieldSetMapper implements FieldSetMapper<AnimalHospital> {

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
    public AnimalHospital mapFieldSet(FieldSet fieldSet) throws BindException {
        AnimalHospital hospital = new AnimalHospital();

        // 필드 번호(index)에 맞게 각 컬럼을 읽어와 세팅합니다.
        hospital.setId(Long.valueOf(fieldSet.readString(0)));                                // 행 번호
        hospital.setServiceName(fieldSet.readString(1));                                     // 개방서비스명
        hospital.setServiceId(fieldSet.readString(2));                                       // 개방서비스ID
        hospital.setRegionCode(fieldSet.readString(3));                                      // 자치단체 코드
        hospital.setManagementNumber(fieldSet.readString(4));                                // 관리번호

        // 날짜 문자열 → LocalDate 변환 (변환 실패 시 null 처리)
        hospital.setApprovalDate(parseLocalDate(fieldSet.readString(5)));                    // 인허가일자
        hospital.setPermitCancelDate(parseLocalDate(fieldSet.readString(6)));                // 인허가취소일자

        hospital.setBusinessStatusCode(fieldSet.readString(7));                              // 영업상태구분코드
        hospital.setBusinessStatusName(fieldSet.readString(8));                              // 영업상태명
        hospital.setDetailedBusinessStatusCode(fieldSet.readString(9));                      // 상세영업상태코드
        hospital.setDetailedBusinessStatusName(fieldSet.readString(10));                     // 상세영업상태명

        hospital.setClosureDate(parseLocalDate(fieldSet.readString(11)));                    // 폐업일자
        hospital.setTemporaryClosureStartDate(parseLocalDate(fieldSet.readString(12)));      // 휴업시작일자
        hospital.setTemporaryClosureEndDate(parseLocalDate(fieldSet.readString(13)));        // 휴업종료일자
        hospital.setReopeningDate(parseLocalDate(fieldSet.readString(14)));                  // 재개업일자

        hospital.setPhoneNumber(fieldSet.readString(15));                                    // 전화번호
        hospital.setSiteArea(parseDouble(fieldSet.readString(16)));                          // 소재지 면적
        hospital.setPostalCode(fieldSet.readString(17));                                     // 소재지 우편번호
        hospital.setFullAddress(fieldSet.readString(18));                                    // 소재지 전체 주소
        hospital.setRoadAddress(fieldSet.readString(19));                                    // 도로명 주소
        hospital.setRoadPostalCode(fieldSet.readString(20));                                 // 도로명 우편번호
        hospital.setBusinessName(fieldSet.readString(21));                                   // 사업장명

        hospital.setLastModified(parseLocalDateTime(fieldSet.readString(22)));               // 최종수정일자 (초 생략 가능)
        hospital.setDataUpdateType(fieldSet.readString(23));                                 // 데이터 갱신 구분
        hospital.setDataUpdateDate(parseLocalDate(fieldSet.readString(24)));                 // 데이터 갱신 일자
        hospital.setBusinessType(fieldSet.readString(25));                                   // 업태구분명

        hospital.setCoordinateX(parseDouble(fieldSet.readString(26)));                       // 좌표 X (경도)
        hospital.setCoordinateY(parseDouble(fieldSet.readString(27)));                       // 좌표 Y (위도)

        hospital.setTaskType(fieldSet.readString(28));                                       // 업무 구분
        hospital.setDetailedTaskType(fieldSet.readString(29));                               // 상세 업무 구분
        hospital.setRightsHolderNumber(fieldSet.readString(30));                             // 권리주체번호
        hospital.setTotalEmployees(parseInteger(fieldSet.readString(31)));                   // 총 종사자 수

        return hospital;
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