package com.playdata.boardservice.common.configs;

import org.owasp.html.HtmlPolicyBuilder;
import org.owasp.html.PolicyFactory;
import org.owasp.html.Sanitizers;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.regex.Pattern;

@Configuration
public class XssConfig {

    private static final Pattern TARGET_BLANK =
            Pattern.compile("^_blank$", Pattern.CASE_INSENSITIVE);

    @Bean
    public PolicyFactory xssPolicy() {
        PolicyFactory base = new HtmlPolicyBuilder()
                // ===== 허용 태그 =====
                .allowElements(
                        "h1","h2","h3","h4","h5","h6",
                        "p","strong","i","u","s",
                        "a","figure","img","blockquote",
                        "ul","li","ol",
                        "label","input","span",
                        "br","pre","code",
                        "table","thead","tbody","tfoot","tr","th","td"
                )
                // ===== URL 프로토콜 =====
                .allowUrlProtocols("http","https","data")
                // ===== <img> 속성 =====
                .allowAttributes("src","width","height","alt").onElements("img")
                // data:image/svg+xml 같은 경우를 막고 싶다면 별도 정규식으로 src를 제한하거나 data 허용을 빼세요.
                // ===== <a> 속성 =====
                .allowAttributes("href","title").onElements("a")
                .allowAttributes("target").matching(TARGET_BLANK).onElements("a")
                .requireRelNofollowOnLinks()
                // ===== class 허용 (필요 태그만 지정) =====
                .allowAttributes("class").onElements(
                        "span","p","div","table","td","th","figure","blockquote","code","pre"
                )
                // ===== input 허용 시 최소 속성만 =====
                .allowAttributes("type","value","checked","disabled","name").onElements("input")
                // 이벤트 핸들러(on*)나 style 같은 위험 속성은 기본적으로 허용되지 않습니다.
                .toFactory();

        // CSS style 속성을 제한적으로 허용하고 싶다면 Sanitizers.STYLES를 and()
        return base.and(Sanitizers.STYLES);
    }


}
