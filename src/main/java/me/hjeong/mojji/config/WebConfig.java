package me.hjeong.mojji.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {
    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/search").setViewName("search");
        registry.addViewController("/forgot").setViewName("account/forgot");
        registry.addViewController("/test").setViewName("test");
    }
}
