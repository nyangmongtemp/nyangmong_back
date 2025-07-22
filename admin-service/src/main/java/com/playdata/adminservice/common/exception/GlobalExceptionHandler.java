package com.playdata.adminservice.common.exception;

import com.playdata.adminservice.common.dto.ErrorResponse;
import com.playdata.adminservice.common.enumeration.ErrorCode;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(CommonException.class)
    public ResponseEntity<ErrorResponse> handleCommonException(CommonException ex) {
        ErrorCode errorCode = ex.getErrorCode();
        return ResponseEntity
                .status(errorCode.getStatus())
                .body(new ErrorResponse(errorCode.getCode(), errorCode.getMessage()));
    }

    /**
     * @PreAuthorize("hasAnyRole('BOSS', 'CONTENT')") 권한 오류
     * 해당하는 권한이 맞지 않을시 발생시킬 에러
     *
     * @param ex
     * @return
     */
    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ErrorResponse> handleAccessDeniedException(AccessDeniedException ex) {
        return ResponseEntity
                .status(HttpStatus.FORBIDDEN)
                .body(new ErrorResponse(ErrorCode.NOT_ALLOWED_DATA.getCode(), "접근권한이 없는 관리자입니다."));
    }
}
