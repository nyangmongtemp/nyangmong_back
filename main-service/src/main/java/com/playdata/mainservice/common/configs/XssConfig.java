package com.playdata.mainservice.common.configs;

import org.owasp.html.HtmlPolicyBuilder;
import org.owasp.html.PolicyFactory;
import org.owasp.html.Sanitizers;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.regex.Pattern;

@Configuration
public class XssConfig {

    // React가 막아주는 것
    // <div>{userInput}</div> 처럼 텍스트로 출력할 때는 < > 등이 자동 이스케이프 → 태그가 “그림 문자”가 됨 → XSS 실행 방지.

    // React가 못 막는 것
    // dangerouslySetInnerHTML / innerHTML 로 원시 HTML을 넣었을 때의 모든 동작
    // <form>, <input>, <button>, <a> 같은 상호작용 요소의 기본 행동(제출/네비게이션)은 그대로 발생.
    // 스크립트 실행을 막았다 해도 피싱/데이터 외부 전송은 가능.
    // 그래서 입력

    // a 태그의 target 속성 값으로 _blank 만 혀용하기 위한 정규식 패턴
    private static final Pattern TARGET_BLANK =
            Pattern.compile("^_blank$", Pattern.CASE_INSENSITIVE);

    // CKEditor 허용 설정
    @Bean("htmlPolicy")
    public PolicyFactory htmlPolicy() {
        // HtmlPolicyBuilder로 '허용할 것들'을 정의하고 toFactory()로 정책(PolicyFactory) 생성
        PolicyFactory base = new HtmlPolicyBuilder()
                // 허용 태그
                .allowElements(
                        "h1","h2","h3","h4","h5","h6", // 제목 태그
                        "p","strong","i","u","s", // 문단 / 텍스트 강조 태그
                        "a","figure","img","blockquote", // 링크 / 이미지 / 인용문 태그
                        "ul","li","ol", // 목록 관련 태그
                        // 추후 제거 해야함
                        // 가능하면 label/input 제거 권장 : 공격자가 임의로 UI에 입력폼 자체를 만들어서 값을 집어 넣을 수 있음
                        "span",
                        "br","pre","code", // 줄바꿈 / 코드 블록
                        "table","thead","tbody","tfoot","tr","th","td" // 표 관련 태그
                )
                // URL 프로토콜
                // data:image/svg+xml 같은 경우를 막고 싶다면 별도 정규식으로 src를 제한하거나 data 허용 x
                .allowUrlProtocols("http","https","data") // href/src 등에 허용할 프로토콜(자바스크립트: 같은 건 불허)
                // img 속성
                .allowAttributes("src","width","height","alt").onElements("img") // img 태그에서 허용할 속성
                // a 태그 속성
                .allowAttributes("href","title").onElements("a") // 링크에서 허용할 기본 속성
                .allowAttributes("target").matching(TARGET_BLANK).onElements("a") // target은 _blank만 허용
                .requireRelNofollowOnLinks() // a 태그에 rel="nofollow"를 강제해 검색엔진 크롤링 영향 최소화
                // class 허용 (필요 태그만 지정)
                .allowAttributes("class").onElements(
                        "span","p","div","table","td","th","figure","blockquote","code","pre"
                )
                // input 허용 시 최소 속성만
                .allowAttributes("type","value","checked","disabled","name").onElements("input") // input 태그 속성 제한
                // (onerror 같은 이벤트 핸들러나 style은 기본적으로 허용되지 않음)
                .toFactory(); // 여기까지 정의한 것을 PolicyFactory로 최종 변환

        // CSS style 속성을 제한적으로 허용하려면 Sanitizers.STYLES를 and()로 결합
        return base.and(Sanitizers.STYLES);
    }

    // 제목/닉네임 등 HTML 전부 불허 정책(태그 싹 제거 → 텍스트만 남김)
    @Bean("plainTextPolicy")
    public PolicyFactory plainTextPolicy() {
        return new HtmlPolicyBuilder().toFactory(); // 아무 태그/속성도 허용하지 않음
    }


}
