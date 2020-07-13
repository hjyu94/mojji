package me.hjeong.mojji.post.event;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.hjeong.mojji.account.AccountRepository;
import me.hjeong.mojji.domain.Account;
import me.hjeong.mojji.domain.Post;
import me.hjeong.mojji.notification.NotificationService;
import me.hjeong.mojji.post.PostRepository;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;

@Async
@Slf4j
@Component
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PostEventListener {

    private final NotificationService notificationService;
    private final AccountRepository accountRepository;
    private final PostRepository postRepository;

    @EventListener
    public void handlePostCreatedEvent(PostCreatedEvent postCreatedEvent) {
        Post post = postCreatedEvent.getPost();
        log.info(post.getTitle() + " is created.");

        Post foundPost = postRepository.findById(post.getId()).orElseThrow(()-> new NoSuchElementException("해당하는 게시물을 찾을 수 없습니다."));
        List<Account> accounts = accountRepository.findAnyAccountsByCategoriesAndStations(Set.of(foundPost.getCategory()), foundPost.getStations());
        accounts.forEach(account -> {
            if (account.isNotiByEmail()) {
                log.info("noti by email to {}: {}", account.getNickname(), account.getEmail());
                notificationService.sendNotificationEmail(account, post);
            }
            if (account.isNotiByWeb()) {
                log.info("noti by web to {}: {}", account.getNickname(), account.getEmail());
                notificationService.createNotification(account, post);
            }
        });
    }

}