package com.example.techcomp.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsConfig {

    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**")
                        .allowedOrigins("http://localhost:3000", "http://192.168.56.1:3000") // Разрешенные источники
                        .allowedMethods("GET", "POST", "PUT", "DELETE") // Разрешенные методы
                        .allowedHeaders("*") // Разрешенные заголовки
                        .allowCredentials(true); // Разрешить отправку куки
            }
        };
    }
}