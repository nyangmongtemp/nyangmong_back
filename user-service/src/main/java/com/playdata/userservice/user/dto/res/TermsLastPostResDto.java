package com.playdata.userservice.user.dto.res;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class TermsLastPostResDto {

    private Long id;
    private String title; // 제목
    private String content; // 내용
    private LocalDateTime createAt; // 등록날짜
    private LocalDateTime updateAt; // 수정날짜

}
