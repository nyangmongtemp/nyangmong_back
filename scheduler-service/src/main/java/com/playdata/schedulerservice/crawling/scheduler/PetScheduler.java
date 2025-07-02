package com.playdata.schedulerservice.crawling.scheduler;

import com.playdata.schedulerservice.crawling.crawler.NaverPetEventCrawler;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PetScheduler {

    private final NaverPetEventCrawler crawler;

    /**
     * 매 1일마다 오전 1시에 박람회 크롤링
     */
    @Scheduled(cron = "0 0 1 */1 * *")
    public void crawlAndSaveEvents() {
        crawler.crawl();
    }
}
