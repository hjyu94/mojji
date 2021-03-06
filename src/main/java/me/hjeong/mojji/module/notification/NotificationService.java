package me.hjeong.mojji.module.notification;

import lombok.RequiredArgsConstructor;
import me.hjeong.mojji.infra.config.AppProperties;
import me.hjeong.mojji.module.account.Account;
import me.hjeong.mojji.module.post.Post;
import me.hjeong.mojji.infra.mail.EmailMessage;
import me.hjeong.mojji.infra.mail.EmailService;
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
        context.setVariable("link", post.getEncodedViewURL());
        context.setVariable("linkName", "판매글 바로가기");
        String message = templateEngine.process("email", context);

        EmailMessage emailMessage = EmailMessage.builder()
                .to(account.getEmail())
                .subject("[모찌마켓] 관심 판매글 등록")
                .message(message)
                .build();
        emailService.sendEmail(emailMessage);
    }

    public void createNotification(Account account, String title, String message, NotificationType type, String link) {
        Notification notification = Notification.builder()
                .account(account)
                .createdDateTime(LocalDateTime.now())
                .title(title)
                .message(message)
                .type(type)
                .link(link)
                .build();
        notificationRepository.save(notification);
    }

    public void markAsRead(List<Notification> notifications, boolean isCheck) {
        notifications.forEach(notification -> {
            notification.setChecked(isCheck);
        });
    }

    public void deleteNotifications(List<String> notificationIdList) {
        notificationIdList.forEach(strId -> {
            long id = Long.parseLong(strId);
            notificationRepository.deleteById(id);
        });
    }

    public void deleteAllByAccount(Account account) {
        List<Notification> notis = notificationRepository.findAllByAccount(account);
        notis.forEach(noti -> notificationRepository.delete(noti));
    }
}
