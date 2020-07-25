package me.hjeong.mojji.module.post.event;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.hjeong.mojji.module.account.Account;
import me.hjeong.mojji.module.account.repository.AccountRepository;
import me.hjeong.mojji.module.notification.Notification;
import me.hjeong.mojji.module.notification.NotificationService;
import me.hjeong.mojji.module.notification.NotificationType;
import me.hjeong.mojji.module.post.Post;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Async
@Slf4j
@Component
@RequiredArgsConstructor
@Transactional
public class PostEventListener {

    private final NotificationService notificationService;
    private final AccountRepository accountRepository;

    @EventListener
    public void handlePostCreatedEvent(PostCreatedEvent postCreatedEvent) {
        Post post = postCreatedEvent.getPost();
        log.info("{} is created. id: {}", post.getTitle(), post.getId());

        List<Account> accounts = accountRepository.findAnyAccountsByCategoriesAndStations(Set.of(post.getCategory()), post.getStations());
        accounts.forEach(account -> {
            if (account.isNotiByEmail()) {
                log.info("noti by email to {}: {}", account.getNickname(), account.getEmail());
                notificationService.sendNotificationEmail(account, post);
            }
            if (account.isNotiByWeb()) {
                log.info("noti by web to {}: {}", account.getNickname(), account.getEmail());
                notificationService.createNotification(account, post.getTitle()
                        , "내 동네, 내 관심 카테고리에 해당하는 새 판매글이 등록되었습니다."
                        , NotificationType.POST_CREATED, post.getEncodedViewURL());
            }
        });
    }

}