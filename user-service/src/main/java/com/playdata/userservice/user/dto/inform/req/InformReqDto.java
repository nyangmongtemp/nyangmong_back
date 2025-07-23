package com.playdata.userservice.user.dto.inform.req;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class InformReqDto {

    @NotBlank
    private String title;
    @NotBlank
    private String content;
}
