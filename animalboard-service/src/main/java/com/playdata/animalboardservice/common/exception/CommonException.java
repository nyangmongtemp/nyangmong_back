package com.playdata.animalboardservice.common.exception;

import com.playdata.animalboardservice.common.enumeration.ErrorCode;
import lombok.Getter;

/**
 * 공통 Custom Exception
 */
@Getter
public class CommonException extends RuntimeException {

    private final ErrorCode errorCode;

    private final String message;

    public CommonException(ErrorCode errorCode) {
        this.errorCode = errorCode;
        this.message = null;
    }

    public CommonException(ErrorCode errorCode, String message) {
        this.errorCode = errorCode;
        this.message = message;
    }
}