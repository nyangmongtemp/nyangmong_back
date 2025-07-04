package com.playdata.boardservice.common.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.http.HttpStatus;

@Setter @Getter @ToString
@NoArgsConstructor
public class CommonResDto<T> {

    private int statusCode;
    private String statusMessage;
    private Object result;

    public CommonResDto(HttpStatus httpStatus, String statusMessage, Object result) {
        this.statusCode = httpStatus.value();
        this.statusMessage = statusMessage;
        this.result = result;
    }
}
