package me.hjeong.mojji.post.event;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.hjeong.mojji.account.AccountRepository;
import me.hjeong.mojji.config.AppProperties;
import me.hjeong.mojji.domain.Account;
import me.hjeong.mojji.domain.Notification;
import me.hjeong.mojji.domain.Post;
import me.hjeong.mojji.mail.EmailMessage;
import me.hjeong.mojji.mail.EmailService;
import me.hjeong.mojji.notification.NotificationRepository;
import me.hjeong.mojji.notification.NotificationType;
import me.hjeong.mojji.post.PostRepository;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Async
@Slf4j
@Component
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PostEventListener {

    private final AccountRepository accountRepository;
    private final PostRepository postRepository;
    private final NotificationRepository notificationRepository;
    private final AppProperties appProperties;
    private final TemplateEngine templateEngine;
    private final EmailService emailService;

    @EventListener
    public void handlePostCreatedEvent(PostCreatedEvent postCreatedEvent) {
        Post post = postCreatedEvent.getPost();
        log.info(post.getTitle() + " is created.");

        Post foundPost = postRepository.findById(post.getId()).orElseThrow();
        List<Account> accounts = accountRepository.findAnyAccountsByCategoriesAndStations(Set.of(foundPost.getCategory()), foundPost.getStations());
        accounts.forEach(account -> {
            if (account.isNotiByEmail()) {
                log.info("noti by email to {}: {}", account.getNickname(), account.getEmail());
                sendNotificationEmail(account, post);
            }
            if (account.isNotiByWeb()) {
                log.info("noti by web to {}: {}", account.getNickname(), account.getEmail());
                createNotification(account, post);
            }
        });
    }

    private void sendNotificationEmail(Account account, Post post) {
        Context context = new Context();
        context.setVariable("nickname", account.getNickname());
        context.setVariable("message", "내 동네, 내 관심 카테고리에 해당하는 새 판매글이 등록되었습니다.");
        context.setVariable("host", appProperties.getHost());
        context.setVariable("link", "/post/" + post.getEncodedURL());
        context.setVariable("linkName", "판매글 바로가기");
        String message = templateEngine.process("email", context);

        EmailMessage emailMessage = EmailMessage.builder()
                .to(account.getEmail())
                .subject("[모찌마켓] 관심 판매글 등록")
                .message(message)
                .build();
        emailService.sendEmail(emailMessage);
    }

    private void createNotification(Account account, Post post) {
        Notification notification = Notification.builder()
                .title(post.getTitle())
                .account(account)
                .createdDateTime(LocalDateTime.now())
                .message("내 동네, 내 관심 카테고리에 해당하는 새 판매글이 등록되었습니다.")
                .type(NotificationType.POST_CREATED)
                .link("/post/" + post.getEncodedURL())
                .build();
        notificationRepository.save(notification);
    }

}