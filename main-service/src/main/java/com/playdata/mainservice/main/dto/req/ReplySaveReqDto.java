package com.playdata.mainservice.main.dto.req;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class ReplySaveReqDto {

    @Valid
    private Long commentId;

    @Valid
    private String content;

}
