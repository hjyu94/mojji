package me.hjeong.mojji.infra.config;

import me.hjeong.mojji.module.account.repository.AccountRepository;
import me.hjeong.mojji.infra.mail.ConsoleEmailService;
import me.hjeong.mojji.infra.mail.EmailService;
import me.hjeong.mojji.module.notification.NotificationRepository;
import me.hjeong.mojji.module.notification.NotificationService;
import me.hjeong.mojji.module.post.repository.PostRepository;
import me.hjeong.mojji.module.post.event.PostEventListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.spring5.SpringTemplateEngine;

@TestConfiguration
@ComponentScan(basePackages = "me.hjeong.mojji.infra.factory")
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
