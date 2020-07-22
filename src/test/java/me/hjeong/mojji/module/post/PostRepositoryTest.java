package me.hjeong.mojji.module.post;

import me.hjeong.mojji.infra.config.EventHandlerConfig;
import me.hjeong.mojji.module.account.Account;
import me.hjeong.mojji.module.category.Category;
import me.hjeong.mojji.module.notification.Notification;
import me.hjeong.mojji.infra.factory.AccountFactory;
import me.hjeong.mojji.infra.factory.CategoryFactory;
import me.hjeong.mojji.infra.factory.NotificationFactory;
import me.hjeong.mojji.infra.factory.PostFactory;
import me.hjeong.mojji.infra.MyDataJpaTest;
import me.hjeong.mojji.infra.mail.EmailMessage;
import me.hjeong.mojji.infra.mail.EmailService;
import me.hjeong.mojji.module.notification.NotificationRepository;
import me.hjeong.mojji.module.post.Post;
import me.hjeong.mojji.module.post.repository.PostRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;

import java.util.List;
import java.util.stream.Collectors;

import static org.hamcrest.CoreMatchers.hasItems;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.then;

@MyDataJpaTest
@Import(EventHandlerConfig.class)
class PostRepositoryTest {

    @Autowired
    PostRepository postRepository;
    @Autowired PostFactory postFactory;

    @Autowired AccountFactory accountFactory;
    @Autowired CategoryFactory categoryFactory;
    @Autowired NotificationFactory notificationFactory;
    @Autowired NotificationRepository notificationRepository;

    @MockBean EmailService emailService;

    @Test
    void findAllOrderByCreatedDateTimeDesc() {
        for(int i=0; i<30; ++i) {
            postFactory.createPost("게시물-" + i);
        }
        List<Post> posts = postRepository.findFirst9ByOrderByCreatedDateTimeDesc();
        assertThat(posts, hasSize(9));
    }

    @Test
    @DisplayName("특정 유저가 설정한 카테고리의 새 글이 올라올 때 이벤트 핸들러가 잘 작동하는가")
    void testPostCreatedEventListener() {
        Integer initSize = notificationFactory.getTotalSize();

        // when
        Category category = categoryFactory.getOne();
        Account account = accountFactory.getOne();
        account.setNotiByWeb(true);
        account.setNotiByEmail(true);
        account.getCategories().add(category);

        Post post = postFactory.getOne();
        post.setCategory(category);
        postRepository.save(post);

        // then
        assertEquals(initSize + 1, notificationFactory.getTotalSize());
        List<Notification> notifications = notificationRepository.findAllByAccount(account);
        List<Account> accounts = notifications.stream().map(Notification::getAccount).collect(Collectors.toList());
        assertThat(accounts, hasItems(account));
        then(emailService).should().sendEmail(any(EmailMessage.class));
    }
}