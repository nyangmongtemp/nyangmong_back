package com.playdata.adminservice.common.exception;

import com.playdata.adminservice.common.dto.CommonResDto;
import com.playdata.adminservice.common.dto.ErrorResponse;
import com.playdata.adminservice.common.enumeration.ErrorCode;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.NoHandlerFoundException;

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

    /**
     * 존재하지 않는 URL로 요청했을 때
     *
     * @param ex
     * @return
     */
    @ExceptionHandler(NoHandlerFoundException.class)
    public ResponseEntity<CommonResDto> handleNotFound(NoHandlerFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new CommonResDto(HttpStatus.NOT_FOUND, "잘못된 경로입니다. URL을 확인해주세요.", null));
    }

    /**
     * 허용되지 않은 HTTP 메서드 요청 시 (예: PUT이 아닌데 PUT 요청)
     *
     * @param ex
     * @return
     */
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<CommonResDto> handleMethodNotSupported(
            HttpRequestMethodNotSupportedException ex) {
        return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED)
                .body(new CommonResDto(HttpStatus.METHOD_NOT_ALLOWED, "허용되지 않은 HTTP 메서드입니다.", null));
    }
}
