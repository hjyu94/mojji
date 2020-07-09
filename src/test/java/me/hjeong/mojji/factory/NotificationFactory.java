package me.hjeong.mojji.factory;

import me.hjeong.mojji.domain.Account;
import me.hjeong.mojji.domain.Category;
import me.hjeong.mojji.domain.Notification;
import me.hjeong.mojji.notification.NotificationRepository;
import me.hjeong.mojji.notification.NotificationType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class NotificationFactory {

    @Autowired NotificationRepository notificationRepository;

    public Integer getTotalSize() {
        List<Notification> all = notificationRepository.findAll();
        return all.size();
    }

    public Notification createNotification(Account account, NotificationType type) {
        Notification notification = Notification.builder()
                .title("알림 메세지 제목임당")
                .account(account)
                .type(type)
                .build();
        return notificationRepository.save(notification);
    }

}
