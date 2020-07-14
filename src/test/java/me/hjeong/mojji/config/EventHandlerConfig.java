package me.hjeong.mojji.config;

import me.hjeong.mojji.account.AccountRepository;
import me.hjeong.mojji.mail.ConsoleEmailService;
import me.hjeong.mojji.mail.EmailService;
import me.hjeong.mojji.notification.NotificationRepository;
import me.hjeong.mojji.notification.NotificationService;
import me.hjeong.mojji.post.PostRepository;
import me.hjeong.mojji.post.event.PostEventListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.spring5.SpringTemplateEngine;

@TestConfiguration
@ComponentScan(basePackages = "me.hjeong.mojji.factory")
public class EventHandlerConfig {

    @Autowired NotificationRepository notificationRepository;
    @Autowired AccountRepository accountRepository;
    @Autowired PostRepository postRepository;

    @Bean
    AppProperties appProperties() {
        return new AppProperties();
    }

    @Bean
    TemplateEngine templateEngine() {
        return new SpringTemplateEngine();
    }

    @Bean
    EmailService emailService() {
        return new ConsoleEmailService();
    }

    @Bean
    NotificationService notificationService() {
        return new NotificationService(notificationRepository, appProperties(), templateEngine(), emailService());
    }

    @Bean
    PostEventListener postEventListener() {
        return new PostEventListener(notificationService(), accountRepository, postRepository);
    }
}
