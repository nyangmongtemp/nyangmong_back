package com.playdata.adminservice.admin.dto.res;

import com.playdata.adminservice.admin.entity.Advertisement;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class AdResDto {

    private Long id;
    private String thumbnailImage;
    private String title;
    private String description;
    private Boolean active;
    private Integer orderNum;
    private LocalDate startDate;
    private LocalDate endDate;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    // 정적 팩토리 메서드
    public static AdResDto from(Advertisement ad) {
        return new AdResDto(
                ad.getId(),
                ad.getThumbnailImage(),
                ad.getTitle(),
                ad.getDescription(),
                ad.getActive(),
                ad.getOrderNum(),
                ad.getStartDate(),
                ad.getEndDate(),
                ad.getCreateAt(),
                ad.getUpdateAt()
        );
    }
}