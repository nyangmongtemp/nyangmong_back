package com.playdata.mainservice.main.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Builder
@AllArgsConstructor
public class ReplySaveReqDto {

    private Long commentId;

    private String content;

}
