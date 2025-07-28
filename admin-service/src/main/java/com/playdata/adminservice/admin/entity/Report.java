package com.playdata.adminservice.admin.entity;

import com.playdata.adminservice.common.entity.BaseTimeEntity;
import com.playdata.adminservice.admin.entity.ReportCategory;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
// 임의로 지정한 테이블 이름 -> 추후에 모든 서비스의 테이블 이름을 통일할 것!
@Table(name = "tbl_report")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Report extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long reportId;
    
    // 신고한 사람
    private Long reportUserId;
    
    // 신고 당한 사람
    private Long accusedUserId;

    private Long adminId;

    private String content;

    @Enumerated(EnumType.STRING)
    private ReportCategory category;
    
    // 신고 처리 여부
    private boolean treat;

    public Report(Long accusedUserId, String content, Long reportUserId, ReportCategory category) {
        this.accusedUserId = accusedUserId;
        this.content = content;
        this.reportUserId = reportUserId;
        this.category = category;
        this.treat = false;
        this.adminId = null;

    }
}
