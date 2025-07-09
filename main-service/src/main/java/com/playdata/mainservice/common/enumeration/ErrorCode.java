package com.playdata.mainservice.common.enumeration;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

/**
 * 공통 Error Code Enum
 */
@Getter
@AllArgsConstructor
public enum ErrorCode {

    /*******************************************************************************************************************
     *                          400 BAD_REQUEST: 잘못된 요청 (유효성 검증 실패, 필수값 누락 등)
     ******************************************************************************************************************/
    BAD_REQUEST(HttpStatus.BAD_REQUEST, "DEFAULT-001", "잘못된 요청입니다."),

    INVALID_PARAMETER(HttpStatus.BAD_REQUEST, "PARAM-001", "입력 데이터가 올바르지 않습니다."),
    MISSING_REQUIRED_PARAMETER(HttpStatus.BAD_REQUEST, "PARAM-002", "필수 입력 데이터가 누락되었습니다."),
    INVALID_PARAMETER_SIZE(HttpStatus.BAD_REQUEST, "PARAM-003", "입력 가능한 최대 사이즈를 초과하였습니다."),
    INVALID_PARAMETER_TYPE(HttpStatus.BAD_REQUEST, "PARAM-004", "입력 데이터 형식이 올바르지 않습니다."),
    INVALID_PARAMETER_DATE(HttpStatus.BAD_REQUEST, "PARAM-005", "입력한 날짜의 범위가 올바르지 않습니다."),
    DUPLICATED_DATA(HttpStatus.BAD_REQUEST, "PARAM-006", "동일한 데이터가 존재합니다."),
    OVER_MAX_DATA(HttpStatus.BAD_REQUEST, "PARAM-007", "데이터의 등록 가능한 최대 개수를 초과하였습니다."),

    // @Valid Annotation 사용 시 'INVALID_FIELD_' 다음에 오는 텍스트는 사용한 어노테이션명과 동일하게 정의해야 함
    INVALID_FIELD_NOTNULL(HttpStatus.BAD_REQUEST, "VALID-001", "입력 데이터는 NULL을 허용하지 않습니다."), // @NotNull
    INVALID_FIELD_NOTEMPTY(HttpStatus.BAD_REQUEST, "VALID-002", "입력 데이터는 NULL 또는 빈 값을 허용하지 않습니다."), // @NotEmpty
    INVALID_FIELD_NOTBLANK(HttpStatus.BAD_REQUEST, "VALID-003", "입력 데이터는 NULL 또는 빈 값 또는 공백을 허용하지 않습니다."), // @NotBlank
    INVALID_FIELD_SIZE(HttpStatus.BAD_REQUEST, "VALID-004", "입력 데이터가 입력 가능한 사이즈의 범위를 초과하였습니다."), // @Size
    INVALID_FIELD_RANGE(HttpStatus.BAD_REQUEST, "VALID-005", "입력 데이터가 입력 가능한 사이즈의 범위를 초과하였습니다."), // @Range
    INVALID_FIELD_MIN(HttpStatus.BAD_REQUEST, "VALID-006", "입력 데이터가 최소 입력값보다 작습니다."), // @Min
    INVALID_FIELD_MAX(HttpStatus.BAD_REQUEST, "VALID-007", "입력 데이터가 최대 입력값보다 큽니다."), // @Max
    INVALID_FIELD_DECIMALMIN(HttpStatus.BAD_REQUEST, "VALID-008", "입력 데이터가 최소 입력값보다 작습니다."), // @DecimalMin
    INVALID_FIELD_DECIMALMAX(HttpStatus.BAD_REQUEST, "VALID-009", "입력 데이터가 최대 입력값보다 큽니다."), // @DecimalMax
    INVALID_FIELD_DIGITS(HttpStatus.BAD_REQUEST, "VALID-010", "입력 데이터가 입력 가능한 사이즈의 범위를 초과하였습니다."), // @Digits
    INVALID_FIELD_PATTERN(HttpStatus.BAD_REQUEST, "VALID-011", "입력 데이터의 형식이 올바르지 않습니다."), // @Pattern
    INVALID_FIELD_URL(HttpStatus.BAD_REQUEST, "VALID-012", "입력 데이터의 URL 형식이 올바르지 않습니다."), // @URL (hibernate validator)
    INVALID_FIELD_EMAIL(HttpStatus.BAD_REQUEST, "VALID-013", "입력 데이터의 EMAIL 형식이 올바르지 않습니다."), // @Email
    INVALID_FIELD_POSITIVE(HttpStatus.BAD_REQUEST, "VALID-014", "입력 데이터는 음수 또는 0을 허용하지 않습니다."), // @Positive
    INVALID_FIELD_POSITIVEORZERO(HttpStatus.BAD_REQUEST, "VALID-015", "입력 데이터는 음수를 허용하지 않습니다."), // @PositiveOrZero



    /*******************************************************************************************************************
     *                          401 UNAUTHORIZED: 인증 실패 (로그인하지 않은 사용자, 로그인 실패 등)
     ******************************************************************************************************************/
    UNAUTHORIZED(HttpStatus.UNAUTHORIZED, "DEFAULT-002", "인증에 실패하였습니다."),

    LOGIN_REQUIRED(HttpStatus.UNAUTHORIZED, "ACCOUNT-001", "로그인을 해주세요."),
    UNKNOWN_HOST(HttpStatus.UNAUTHORIZED, "ACCOUNT-002", "호스트를 확인할 수 없습니다."),
    SESSION_EXPIRED(HttpStatus.UNAUTHORIZED, "ACCOUNT-003", "세션이 만료되었습니다."),
    ACCOUNT_NOT_FOUND(HttpStatus.UNAUTHORIZED, "ACCOUNT-004", "아이디 혹은 패스워드를 다시 확인해 주세요."), // 계정이 존재하지 않을 때
    ACCOUNT_LOCKED(HttpStatus.UNAUTHORIZED, "ACCOUNT-005", "계정이 잠겼습니다."),
    ACCOUNT_DISABLED(HttpStatus.UNAUTHORIZED, "ACCOUNT-006", "계정이 비활성화 되었습니다."),
    INVALID_PASSWORD(HttpStatus.UNAUTHORIZED, "ACCOUNT-007", "아이디 혹은 패스워드를 다시 확인해 주세요."), // 패스워드가 일치하지 않을 때
    EXPIRED_PASSWORD(HttpStatus.UNAUTHORIZED, "ACCOUNT-008", "임시 패스워드가 만료되었습니다."),
    INVALID_AUTH_CODE(HttpStatus.UNAUTHORIZED, "ACCOUNT-009", "인증코드가 일치하지 않습니다."),
    EXPIRED_AUTH_CODE(HttpStatus.UNAUTHORIZED, "ACCOUNT-010", "인증코드가 만료되었습니다."),
    AUTH_CODE_NOT_FOUND(HttpStatus.UNAUTHORIZED, "ACCOUNT-011", "인증코드가 존재하지 않습니다."),
    AUTH_ACCOUNT_NOT_FOUND(HttpStatus.UNAUTHORIZED, "ACCOUNT-012", "로그인을 해주세요."), // 인증할 계정이 존재하지 않을 때
    AUTH_OTP_NOT_FOUND(HttpStatus.UNAUTHORIZED, "ACCOUNT-013", "OTP 개인키가 존재하지 않습니다."),



    /*******************************************************************************************************************
     *                       403 FORBIDDEN: 인가 실패 (자원에 대한 권한 없음, 삭제/수정 권한 없음 등)
     ******************************************************************************************************************/
    FORBIDDEN(HttpStatus.FORBIDDEN, "DEFAULT-003", "접근할 수 없습니다."),

    NOT_ALLOWED_IP(HttpStatus.FORBIDDEN, "AUTHORITY-001", "접근할 수 없는 IP 입니다."),
    NOT_ALLOWED_MENU(HttpStatus.FORBIDDEN, "AUTHORITY-002", "접근할 수 없는 메뉴 입니다."),
    NOT_ALLOWED_DATA(HttpStatus.FORBIDDEN, "AUTHORITY-003", "접근할 수 없는 데이터 입니다."),
    NO_INSERT_PERMISSION(HttpStatus.FORBIDDEN, "AUTHORITY-004", "등록 권한이 없습니다."),
    NO_UPDATE_PERMISSION(HttpStatus.FORBIDDEN, "AUTHORITY-005", "수정 권한이 없습니다."),
    NO_DELETE_PERMISSION(HttpStatus.FORBIDDEN, "AUTHORITY-006", "삭제 권한이 없습니다."),
    NOT_ALLOWED_API(HttpStatus.FORBIDDEN, "AUTHORITY-007", "접근할 수 없는 API 입니다."),



    /*******************************************************************************************************************
     *                                          404 NOT_FOUND: 리소스를 찾을 수 없음
     ******************************************************************************************************************/
    NOT_FOUND(HttpStatus.NOT_FOUND, "DEFAULT-004", "리소스를 찾을 수 없습니다."),

    PAGE_NOT_FOUND(HttpStatus.NOT_FOUND, "GET-001", "요청 페이지가 존재하지 않습니다."),
    DATA_NOT_FOUND(HttpStatus.NOT_FOUND, "GET-002", "요청 데이터가 존재하지 않습니다."),



    /*******************************************************************************************************************
     *                               405 METHOD_NOT_ALLOWED: 허용되지 않은 Request Method 호출
     ******************************************************************************************************************/
    METHOD_NOT_ALLOWED(HttpStatus.METHOD_NOT_ALLOWED, "DEFAULT-005", "허용되지 않은 Method 입니다."),



    /*******************************************************************************************************************
     *                                      409 CONFLICT: 요청과 리소스 상태 충돌
     ******************************************************************************************************************/
    CONFLICT(HttpStatus.CONFLICT, "DEFAULT-009", "요청이 리소스의 현재 상태와 충돌합니다."),

    CANNOT_DELETE_AUTHORITY(HttpStatus.CONFLICT, "DELETE-001", "The permission cannot be deleted because there is an account assigned to the permission you are trying to delete."),



    /*******************************************************************************************************************
     *                              413 PAYLOAD_TOO_LARGE: 파일 사이즈 및 개수 초과 등
     ******************************************************************************************************************/
    PAYLOAD_TOO_LARGE(HttpStatus.PAYLOAD_TOO_LARGE, "DEFAULT-006", "요청 엔티티가 서버에 정의된 제한보다 큽니다."),



    /*******************************************************************************************************************
     *                              415 UNSUPPORTED_MEDIA_TYPE: 지원하지 않는 미디어 타입
     ******************************************************************************************************************/
    UNSUPPORTED_MEDIA_TYPE(HttpStatus.UNSUPPORTED_MEDIA_TYPE, "DEFAULT-007", "지원하지 않는 미디어 타입입니다."),



    /*******************************************************************************************************************
     *                                      500 INTERNAL_SERVER_ERROR: 서버 에러
     ******************************************************************************************************************/
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "DEFAULT-008", "내부 서버에 오류가 발생했습니다."),

    // 로그인 2차 인증 OTP 관련 에러
    LOGIN_OTP_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "OTP-001", "OTP 생성에 오류가 발생했습니다."),

    // CSRF 토큰 관련 에러
    MISSING_JWT_TOKEN(HttpStatus.INTERNAL_SERVER_ERROR, "JWT-001", "JWT Token이 존재하지 않습니다."),
    INVALID_JWT_TOKEN(HttpStatus.INTERNAL_SERVER_ERROR, "JWT-002", "JWT Token이 유효하지 않습니다."),



    /*******************************************************************************************************************
     *                                                      파일 관련 에러 //TODO: 파일 업로드/다운로드 리팩토링 시 관련 에러코드 재정의 필요
     ******************************************************************************************************************/
    FILE_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "FILE-001", "파일 서버에 오류가 발생했습니다."),

    FILE_NOT_FOUND(HttpStatus.INTERNAL_SERVER_ERROR, "FILE-002", "요청 파일이 서버에 존재하지 않습니다."),
    EMPTY_FILE(HttpStatus.INTERNAL_SERVER_ERROR, "FILE-003", "파일이 비어있습니다."),
    INVALID_FILE_NAME(HttpStatus.INTERNAL_SERVER_ERROR, "FILE-004", "파일명이 올바르지 않습니다."),
    FILE_SIZE_EXCEEDED(HttpStatus.PAYLOAD_TOO_LARGE, "FILE-005", "업로드 가능한 파일 사이즈를 초과하였습니다."),
    FILE_COUNT_EXCEEDED(HttpStatus.PAYLOAD_TOO_LARGE, "FILE-006", "업로드 가능한 파일 개수를 초과하였습니다."),
    UNSUPPORTED_FILE_TYPE(HttpStatus.UNSUPPORTED_MEDIA_TYPE, "FILE-007", "업로드 할 수 없는 파일 형식입니다."),
    MIME_FILE_TYPE(HttpStatus.BAD_REQUEST, "FILE-008", "업로드한 파일은 이미지가 아닙니다. (contentType 검사 실패)."),
    MATCHES_FILE_TYPE(HttpStatus.UNSUPPORTED_MEDIA_TYPE, "FILE-009", "허용되지 않는 이미지 확장자입니다."),

    /*******************************************************************************************************************
     *                                                      메일 관련 에러
     ******************************************************************************************************************/
    MAIL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "MAIL-001", "메일 서버에 오류가 발생했습니다."),

    MISSING_MAIL_RECIPIENT(HttpStatus.INTERNAL_SERVER_ERROR, "MAIL-002", "메일 수신자 정보가 누락되었습니다."),


    ;

    private final HttpStatus status;
    private final String code;
    private final String message;
}