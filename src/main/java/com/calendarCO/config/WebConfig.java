package com.calendarCO.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        // Allow Angular on port 4200 to access API endpoints
        registry.addMapping("/api/**")
                .allowedOrigins("http://localhost:4200")
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                .allowedHeaders("*")
                .allowCredentials(true)
                .maxAge(3600);

        // Thymeleaf endpoints remain same-origin (or configure as needed)
        registry.addMapping("/calendar/**")
                .allowedOrigins("http://localhost:8080") // Your own server
                .allowedMethods("GET", "POST")
                .allowedHeaders("*");
    }
}