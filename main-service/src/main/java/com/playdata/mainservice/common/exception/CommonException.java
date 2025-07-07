package com.playdata.mainservice.common.exception;

import com.playdata.mainservice.common.enumeration.ErrorCode;
import lombok.Getter;

/**
 * 공통 Custom Exception
 */
@Getter
public class CommonException extends RuntimeException {

    private final ErrorCode errorCode;

    public CommonException(ErrorCode errorCode) {
        super(errorCode.getMessage()); // Exception.getMessage()로 메시지를 전달
        this.errorCode = errorCode;
    }

    public CommonException(ErrorCode errorCode, String customMessage) {
        super(customMessage); // Exception.getMessage()에 커스텀 메시지 전달
        this.errorCode = errorCode;
    }
}