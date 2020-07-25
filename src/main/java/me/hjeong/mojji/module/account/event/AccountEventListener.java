package me.hjeong.mojji.module.account.event;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.hjeong.mojji.module.account.Account;
import me.hjeong.mojji.module.notification.NotificationService;
import me.hjeong.mojji.module.notification.NotificationType;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Async
@Slf4j
@Component
@RequiredArgsConstructor
@Transactional
public class AccountEventListener {

    private final NotificationService notificationService;

    @EventListener
    public void handleAccountCreatedEvent(AccountCreatedEvent accountCreatedEvent) {
        Account newAccount = accountCreatedEvent.getAccount();
        log.info("user (id: {}, name: {}) created", newAccount.getId(), newAccount.getNickname());
        notificationService.createNotification(newAccount, "가입완료", "가입을 축하합니다", NotificationType.ACCOUNT_CREATED, null);
    }

}
