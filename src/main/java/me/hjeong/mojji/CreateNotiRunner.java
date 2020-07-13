package me.hjeong.mojji;

import lombok.RequiredArgsConstructor;
import me.hjeong.mojji.account.AccountRepository;
import me.hjeong.mojji.domain.Account;
import me.hjeong.mojji.domain.Post;
import me.hjeong.mojji.notification.NotificationService;
import me.hjeong.mojji.post.PostRepository;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.util.List;

//@Component
@RequiredArgsConstructor
public class CreateNotiRunner implements ApplicationRunner {

    private final NotificationService notificationService;
    private final AccountRepository accountRepository;
    private final PostRepository postRepository;
    private final String nickname = "1234";

    @Override
    public void run(ApplicationArguments args) throws Exception {
        Account account = accountRepository.findByNickname(nickname);
        List<Post> posts = postRepository.findAll();
        posts.forEach(post -> {
            notificationService.createNotification(account, post);
        });
    }

}
