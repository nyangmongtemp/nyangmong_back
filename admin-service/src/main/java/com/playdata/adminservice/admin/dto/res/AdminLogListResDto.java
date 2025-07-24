package com.playdata.adminservice.admin.dto.res;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class AdminLogListResDto {

    private Long logId; // 로그 고유 ID (기본키, 자동 증가)
    private Long adminId; // 관리자 ID
    private Long userId; // 사용자 ID
    private String adminIp; // 접속한 관리자의 IP
    private String userName; // 사용자 이름
    private String userEmail; // 사용자 이메일
    private String userNickName; // 사용자 닉네임
    private String adminName; // 관리자 이름
    private LocalDateTime createAt;// 생성 시간

}
