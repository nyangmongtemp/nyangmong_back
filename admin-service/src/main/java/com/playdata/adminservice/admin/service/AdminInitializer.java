package com.playdata.adminservice.admin.service;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
// 자동으로 기본 배너 값을 넣어주기 위해 사용하는 컴포넌트
public class AdminInitializer{

    private final BannerService bannerService;
    
    // 테이블이 없다면 생성 후에, 자동으로 기본 배너를 넣어주는 서비스
    @EventListener(ApplicationReadyEvent.class)
    public void run(ApplicationReadyEvent event) {
        bannerService.init();
    }

}
