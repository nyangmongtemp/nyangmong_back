package com.playdata.adminservice.admin.dto.req;

import lombok.Data;

import java.time.LocalDate;

@Data
public class AdRegisterReqDto {
    private String thumbnailImage;
    private String title;
    private String description;
    private Boolean active;
    private Integer orderNum;
    private LocalDate startDate;
    private LocalDate endDate;
}