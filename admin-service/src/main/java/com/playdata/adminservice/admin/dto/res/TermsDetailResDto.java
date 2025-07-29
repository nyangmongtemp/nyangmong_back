package com.playdata.adminservice.admin.dto.res;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class TermsDetailResDto {

    private String title; // 제목
    private String content; // 내용
    private String adminName; // 관리자 이름
    private LocalDateTime createAt; // 등록날짜
    private LocalDateTime updateAt; // 수정날짜

}
