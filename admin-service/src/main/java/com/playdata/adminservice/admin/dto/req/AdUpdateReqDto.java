package com.playdata.adminservice.admin.dto.req;

import com.fasterxml.jackson.annotation.JsonFormat;
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

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate startDate;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate endDate;

    private String linkUrl;

    public Boolean Confirmed;
}