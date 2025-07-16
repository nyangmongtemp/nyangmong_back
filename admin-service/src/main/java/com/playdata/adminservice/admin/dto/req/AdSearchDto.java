package com.playdata.adminservice.admin.dto.req;

import lombok.Data;

import java.time.LocalDate;

@Data
public class AdSearchDto {
    private String title;
    private Boolean active;
    private LocalDate startDate;
    private LocalDate endDate;
}