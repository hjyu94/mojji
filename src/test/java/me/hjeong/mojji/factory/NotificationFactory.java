package me.hjeong.mojji.factory;

import me.hjeong.mojji.domain.Notification;
import me.hjeong.mojji.notification.NotificationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class NotificationFactory {

    @Autowired
    NotificationRepository notificationRepository;

    public Integer getTotalSize() {
        List<Notification> all = notificationRepository.findAll();
        return all.size();
    }

}
