package com.playdata.adminservice.admin.dto.res;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 광고 응답에 사용되는 DTO 클래스
 * 클라이언트에게 광고 정보를 전달할 때 사용됨
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AdResDto {

    // 광고 고유 식별자
    private Long id;

    // 광고 썸네일 이미지 URL
    private String thumbnailImage;

    // 광고 제목
    private String title;

    // 광고 설명
    private String description;

    // 광고 활성화 여부 (true: 노출, false: 비노출)
    private Boolean active;

    // 광고 순서 번호 (정렬에 사용됨)
    private Integer orderNum;

    // 광고 시작 날짜
    private LocalDate startDate;

    // 광고 종료 날짜
    private LocalDate endDate;

    // 광고 생성일시
    //@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDateTime createdAt;

    // 광고 최종 수정일시
    //@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDateTime updatedAt;
}