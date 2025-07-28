package com.playdata.adminservice.admin.dto.req;

import jakarta.validation.constraints.*;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;

@Data
public class AdUpdateReqDto {


    private String title;

    private String description;

    private Boolean active;

    private Integer orderNum;

    private MultipartFile thumbnailImage;

    private LocalDate startDate;

    private LocalDate endDate;

    private String linkUrl;

    public Boolean Confirmed;
}