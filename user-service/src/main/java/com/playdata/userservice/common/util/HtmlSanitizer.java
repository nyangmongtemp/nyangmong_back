package com.playdata.userservice.common.util;

import org.owasp.html.PolicyFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component
public class HtmlSanitizer {

    // XssConfig 에서 만든 PolicyFactory 빈이 주입됨
    private final PolicyFactory htmlPolicy;
    private final PolicyFactory plainTextPolicy;

    // Qualifier 2개의 bean 값 생성자 생성
    // Qualifier : 같은 타입의 빈이 여러 개 있을 때, 어느 걸 주입할지 지정 하는 어노테이션
    public HtmlSanitizer(@Qualifier("htmlPolicy") PolicyFactory htmlPolicy,
                         @Qualifier("plainTextPolicy") PolicyFactory plainTextPolicy) {
        this.htmlPolicy = htmlPolicy;
        this.plainTextPolicy = plainTextPolicy;
    }

    // CKEditor 정화기
    public String sanitizeHtml(String raw) { // 외부에서 호출하는 정화 메서드
        return raw == null ? null : htmlPolicy.sanitize(raw); // null 이면 그대로, 아니면 정책에 따라 안전하게 변환
    }

    // 제목/닉네임 등 HTML 전부 불허(텍스트만) 정화기
    public String sanitizeText(String raw) {
        return raw == null ? null : plainTextPolicy.sanitize(raw);
    }


}
