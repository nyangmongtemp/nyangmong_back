package com.playdata.festivalservice;

import com.playdata.festivalservice.repository.FestivalRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class FestivalServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(FestivalServiceApplication.class, args);
	}


	@Bean
	public CommandLineRunner testDbConnection(FestivalRepository repository) {
		return args -> {
			long count = repository.count();
			System.out.println("✅ DB 연결 성공! Festival row count = " + count);
		};
	}
}
