package com.playdata.adminservice.admin.dto.res;

import com.playdata.adminservice.admin.entity.Terms;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class TermsDetailResDto {

    private String title; // 제목
    private String content; // 내용
    private LocalDateTime createAt; // 등록날짜
    private LocalDateTime updateAt; // 수정날짜

    @Builder
    public TermsDetailResDto(Terms terms) {
        this.title = terms.getTitle();
        this.content = terms.getContent();
        this.createAt = terms.getCreateAt();
        this.updateAt = terms.getUpdateAt();
    }

}
