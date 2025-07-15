package com.playdata.gatewayservice;

import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.TimeZone;

@SpringBootApplication
@Slf4j
public class GatewayServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(GatewayServiceApplication.class, args);
    }

    /**
     * 애플리케이션 실행 시 JVM의 기본 시간대를 'Asia/Seoul'로 설정하고,
     * 설정된 시간대 정보를 로그로 출력합니다.
     */
    @PostConstruct
    public void setDefaultTimeZone() {
        // 1. JVM의 기본 시간대를 'Asia/Seoul'로 설정
        TimeZone.setDefault(TimeZone.getTimeZone("Asia/Seoul"));

        // 2. 설정된 시간대 정보 로그로 확인
        TimeZone defaultTimeZone = TimeZone.getDefault();
        log.info(" Application's Default TimeZone has been set to: {} (ID: {})",
                defaultTimeZone.getDisplayName(),
                defaultTimeZone.getID());
    }

}
