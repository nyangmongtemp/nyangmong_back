package com.playdata.boardservice.common.exception;

import com.playdata.boardservice.common.dto.ErrorResponse;
import com.playdata.boardservice.common.enumeration.ErrorCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(CommonException.class)
    public ResponseEntity<ErrorResponse> handleCommonException(CommonException ex) {
        ErrorCode errorCode = ex.getErrorCode();
        return ResponseEntity
                .status(errorCode.getStatus())
                // 기존 코드 -> errorCode.getMessage() 라서 커스텀 메시지 사용이 불가능해서 수정함.
                .body(new ErrorResponse(errorCode.getCode(), ex.getMessage()));
    }
}
