package com.playdata.adminservice.admin.entity;

import com.playdata.adminservice.admin.dto.req.ReportUpdateReqDto;
import com.playdata.adminservice.common.entity.BaseTimeEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import jakarta.validation.constraints.Email;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(name = "tbl_user")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId; // 사용자 고유 ID (기본키, 자동 증가)

    private String userName; // 사용자 실명

    @Column(unique = true, nullable = false)
    @Email
    private String email; // 이메일 주소 (로그인 시 사용, 유일함)

    @Column(nullable = false)
    private String password; // 로그인 비밀번호 (암호화 저장)

    private String profileImage; // 프로필 이미지 경로 또는 URL

    private String nickname; // 닉네임 (커뮤니티 활동 시 사용)

    private String address; // 주소 (예: 서울시 강남구)

    private String phone; // 전화번호 (예: 010-1234-5678)

    private String socialId; // 소셜 로그인 ID (소셜 제공자에서 받은 고유값)

    private Long grade; // 활동 등급/점수 (커뮤니티 기여도 기반)

    private String socialProvider; // 소셜 로그인 제공자

    private boolean active; // 계정 활성화 여부 (true = 정상, false = 정지/탈퇴)

    private LocalDateTime passwordUpdatedAt; // 마지막 비밀번호 변경 일시

    private int passwordFaultCount; // 로그인 실패 횟수

    private int pauseCount; // 계정이 일시 정지된 횟수 (운영자 판단으로 정지된 기록)

    // 정지 풀리는 날짜
    private LocalDateTime releaseAt;

    @Transient
    private int reportCount; // 누적 신고 횟수 (욕설, 스팸 등으로 신고당한 횟수)

    public void updateUserReport(ReportUpdateReqDto reportUpdateReqDto) {
        this.pauseCount++;
        this.active = false;
        this.releaseAt = (reportUpdateReqDto.getReleaseAt() == 999) ? LocalDateTime.now().plusYears(999) : LocalDateTime.now().plusDays(reportUpdateReqDto.getReleaseAt());
    }
}