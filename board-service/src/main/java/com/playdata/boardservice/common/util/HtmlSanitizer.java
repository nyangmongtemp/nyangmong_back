package com.playdata.boardservice.common.util;

import lombok.RequiredArgsConstructor;
import org.owasp.html.PolicyFactory;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class HtmlSanitizer {

    private final PolicyFactory policy;

    public String sanitize(String raw) {
        return raw == null ? null : policy.sanitize(raw);
    }


}
