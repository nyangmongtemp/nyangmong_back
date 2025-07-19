package com.playdata.mapservice.map.dto.hospital.res;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;

@Getter
@Builder
public class HospitalListDto {

    private Long hospitalId;

    private LocalDate approvalDate; // 인허가일자

    private String hospitalName;

    private String fullAddress;

}
