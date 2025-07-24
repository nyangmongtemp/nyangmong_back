package com.playdata.adminservice.admin.dto.res;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class InformDetailResDto {

    private String title; // 제목
    private String content; // 내용
    private String reply; // 답변
    private boolean answered; // 답변여부
    private LocalDateTime createAt; // 등록날짜
    private String userName; // 사용자 이름
    private String userEmail; // 사용자 이메일

}
