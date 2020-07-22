package me.hjeong.mojji.infra.factory;

import me.hjeong.mojji.module.account.Account;
import me.hjeong.mojji.module.notification.Notification;
import me.hjeong.mojji.module.notification.NotificationRepository;
import me.hjeong.mojji.module.notification.NotificationType;
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
