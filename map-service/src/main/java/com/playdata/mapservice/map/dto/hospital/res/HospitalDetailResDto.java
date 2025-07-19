package com.playdata.mapservice.map.dto.hospital.res;

import jakarta.persistence.Column;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Builder
public class HospitalDetailResDto {

    private Long id;  // 번호
    private String serviceName; // 개방서비스명
    private String serviceId; // 개방서비스아이디
    private LocalDate approvalDate; // 인허가일자
    private String phoneNumber; // 소재지전화
    private Double siteArea; // 소재지면적
    private String postalCode; // 소재지우편번호
    private String fullAddress; // 소재지전체주소
    private String roadAddress; // 도로명전체주소
    private String roadPostalCode; // 도로명우편번호
    private String businessName; // 사업장명
    private LocalDateTime lastModified; // 최종수정시점
    private Double mapx; // 좌표정보x(epsg5174)
    private Double mapy; // 좌표정보y(epsg5174)

}
