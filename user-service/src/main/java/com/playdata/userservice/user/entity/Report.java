package com.playdata.userservice.user.entity;

import com.playdata.userservice.common.entity.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

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
    private PauseCategory PauseCategory;

    @Enumerated(EnumType.STRING)
    private ReportCategory category;

    // 정지를 한다면 언제까지 정지인지
    private Integer duration;
    
    // 신고 처리 여부
    private boolean treat;

    public Report(Long accusedUserId, String content, Long reportUserId, ReportCategory category) {
        this.accusedUserId = accusedUserId;
        this.content = content;
        this.reportUserId = reportUserId;
        this.category = category;
        this.PauseCategory = null;
        this.treat = false;
        this.duration = null;
        this.adminId = null;

    }
}
