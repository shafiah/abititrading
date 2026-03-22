package com.abiti_app_service.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {

        registry.addResourceHandler("/vid/**")
                .addResourceLocations("file:/var/www/html/vid/");

        registry.addResourceHandler("/img/**")
                .addResourceLocations("file:/var/www/html/img/");

        registry.addResourceHandler("/pdf/**")
                .addResourceLocations("file:/var/www/html/pdf/");
    }
}