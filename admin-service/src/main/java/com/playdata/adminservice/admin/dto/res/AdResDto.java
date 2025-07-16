package com.playdata.adminservice.admin.dto.res;

import com.playdata.adminservice.admin.entity.Advertisement;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder

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
}