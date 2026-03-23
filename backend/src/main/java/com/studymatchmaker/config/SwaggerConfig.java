package com.studymatchmaker.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {
    @Bean
    public OpenAPI api() {
        return new OpenAPI().info(new Info()
                .title("Study Matchmaker API")
                .description("Backend API for matching students, groups, chat, reviews, and reminders")
                .version("1.0.0"));
    }
}
