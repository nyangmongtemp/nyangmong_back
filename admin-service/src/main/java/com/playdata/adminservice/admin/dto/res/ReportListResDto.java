package com.playdata.adminservice.admin.dto.res;

import com.playdata.adminservice.admin.entity.PauseCategory;
import com.playdata.adminservice.admin.entity.Report;
import com.playdata.adminservice.admin.entity.ReportCategory;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Transient;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ReportListResDto {

    private Long reportId; // 신고 게시글 번호
    private String content; // 내용
    private ReportCategory category; // 신고사유
    private LocalDateTime createAt; // 등록날짜

    private String reportUserName; // 신고한 사람 이름
    private String reportUserEmail; // 신고한 사람 이메일

    private String accuseUserName; // 신고당한 사람 이름
    private String accuseUserEmail; // 신고당한 사람 이메일

}
