package com.playdata.adminservice.admin.dto.banner.req;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class OrderModiReqDto {

    @NotNull
    private Long bannerId;
    @NotNull
    private Integer order;

}
