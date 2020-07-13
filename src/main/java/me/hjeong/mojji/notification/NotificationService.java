package me.hjeong.mojji.notification;

import lombok.RequiredArgsConstructor;
import me.hjeong.mojji.config.AppProperties;
import me.hjeong.mojji.domain.Account;
import me.hjeong.mojji.domain.Notification;
import me.hjeong.mojji.domain.Post;
import me.hjeong.mojji.mail.EmailMessage;
import me.hjeong.mojji.mail.EmailService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class NotificationService {

    private final NotificationRepository notificationRepository;
    private final AppProperties appProperties;
    private final TemplateEngine templateEngine;
    private final EmailService emailService;

    public void sendNotificationEmail(Account account, Post post) {
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

    public void createNotification(Account account, Post post) {
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

    public void markAsRead(List<Notification> notifications, boolean isCheck) {
        notifications.forEach(notification -> {
            notification.setChecked(isCheck);
        });
    }
}
