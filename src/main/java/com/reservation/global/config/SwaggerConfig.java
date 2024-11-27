package com.reservation.global.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI openAPI() {
        Info info = new Info()
                .title("Hospital Go. API")
                .description("전체 API 총 20개가 개발되었습니다.");

        return new OpenAPI()
                .components(new Components())
                .info(info);
    }
}
