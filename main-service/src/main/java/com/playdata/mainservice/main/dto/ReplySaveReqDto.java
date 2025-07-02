package com.playdata.mainservice.main.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Builder
public class ReplySaveReqDto {

    private Long commentId;

    private String content;

}
