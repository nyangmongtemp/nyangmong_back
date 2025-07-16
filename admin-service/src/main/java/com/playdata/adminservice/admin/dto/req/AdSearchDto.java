package com.playdata.adminservice.admin.dto.req;

import lombok.Data;
import java.time.LocalDate;

/**
 * 광고 검색 요청 시 사용하는 DTO 클래스
 * 검색 조건으로 제목, 활성 상태, 시작일, 종료일을 입력받음
 */
@Data
public class AdSearchDto {

    // 광고 제목 (부분 검색 가능)
    private String title;

    // 광고 활성화 여부 (true: 활성 광고만, false: 비활성 광고만, null: 전체)
    private Boolean active;

    // 광고 시작일 이후의 광고 검색 (포함)
    private LocalDate startDate;

    // 광고 종료일 이전의 광고 검색 (포함)
    private LocalDate endDate;
}