package me.hjeong.mojji.infra.factory;

import lombok.RequiredArgsConstructor;
import me.hjeong.mojji.module.account.repository.AccountRepository;
import me.hjeong.mojji.module.account.Account;
import me.hjeong.mojji.module.notification.NotificationType;
import me.hjeong.mojji.module.post.Post;
import me.hjeong.mojji.module.notification.NotificationService;
import me.hjeong.mojji.module.post.repository.PostRepository;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;

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
            notificationService.createNotification(account, post.getTitle()
                    , "내 동네, 내 관심 카테고리에 해당하는 새 판매글이 등록되었습니다."
                    , NotificationType.POST_CREATED, post.getEncodedViewURL());
        });
    }

}
