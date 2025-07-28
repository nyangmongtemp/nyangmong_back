package com.playdata.adminservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@EnableJpaRepositories(basePackages = {
        "com.playdata.animalboardservice.repository",     // AnimalRepository 위치
        "com.playdata.boardservice.board.repository",        // IntroductionBoardRepository 위치
        "com.playdata.boardservice.board.repository",      // InformationBoardRepository 위치
        "com.playdata.adminservice.admin.repository"       // AdminBoardRepository or QueryDSL용
})
@EntityScan(basePackages = {
        "com.playdata.animalboardservice.entity",
        "com.playdata.communityservice.entity",
        "com.playdata.informationservice.entity",
        "com.playdata.adminservice.admin.entity"
})
@SpringBootApplication
@EnableJpaAuditing
public class AdminServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(AdminServiceApplication.class, args);
    }

}
