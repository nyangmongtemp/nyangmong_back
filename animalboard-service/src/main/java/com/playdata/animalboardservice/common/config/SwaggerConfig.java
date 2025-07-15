package com.playdata.animalboardservice.common.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.servers.Server;
import java.util.List;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

//@OpenAPIDefinition(
//        info = @Info(
//                title = "AnimalBoard-Service API",
//                version = "1.0.0",
//                description = """
//                        MSA 기반 유기동물/분양동물 관리 서비스 API입니다.
//
//                        ## 주요 기능
//                        - 유기동물 목록 / 상세 조회
//                        - 분양동물 CRUD 기능
//                        """,
//                license = @License(
//                        name = "MIT License",
//                        url = "https://opensource.org/licenses/MIT"
//                )
//        ),
//        servers = {
//                @Server(url = "http://localhost:8000/animalboard-service", description = "로컬 개발 서버"),
////                @Server(url = "https://api.playdatashop9201.store", description = "운영 서버")
//        },
//        security = @SecurityRequirement(name = "bearerAuth")
//)
//@SecurityScheme(
//        name = "bearerAuth",
//        type = SecuritySchemeType.HTTP,
//        bearerFormat = "JWT",
//        scheme = "bearer",
//        description = "JWT 토큰을 입력하세요. (Bearer 접두사 제외)"
//)
@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .components(new Components())
                .info(apiInfo());
    }

    private Info apiInfo() {
        return new io.swagger.v3.oas.models.info.Info()
                .title("AnimalBoard-Service API")
                .description("nyangmong의 AnimalBoard 관련 api 모음 문서")
                .version("1.0.0");
    }

}
