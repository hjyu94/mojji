package me.hjeong.mojji.infra.config;

import lombok.RequiredArgsConstructor;
import me.hjeong.mojji.module.chat.interceptor.ChatMessageInterceptor;
import me.hjeong.mojji.module.notification.NotificationInterceptor;
import org.springframework.boot.autoconfigure.security.StaticResourceLocation;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Configuration
@RequiredArgsConstructor
public class WebConfig implements WebMvcConfigurer {

    private final NotificationInterceptor notificationInterceptor;
    private final ChatMessageInterceptor chatMessageInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        List<String> excludePaths = Arrays.stream(StaticResourceLocation.values())
                .flatMap(StaticResourceLocation::getPatterns)
                .collect(Collectors.toList());
        excludePaths.add("/node_modules/**");
        excludePaths.add("/upload/**");

        registry.addInterceptor(notificationInterceptor)
                .excludePathPatterns(excludePaths);

        registry.addInterceptor(chatMessageInterceptor)
                .excludePathPatterns(excludePaths);
    }

}
