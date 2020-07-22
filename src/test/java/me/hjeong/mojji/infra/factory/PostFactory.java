package me.hjeong.mojji.infra.factory;

import lombok.RequiredArgsConstructor;
import me.hjeong.mojji.module.post.Post;
import me.hjeong.mojji.module.post.repository.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Component
@RequiredArgsConstructor
public class PostFactory {

    @Autowired PostRepository postRepository;
    @Autowired CategoryFactory categoryFactory;
    @Autowired AccountFactory accountFactory;

    public Post createPost(String title) {
        Post post = Post.builder()
                .title(title)
                .seller(accountFactory.getOne())
                .category(categoryFactory.getOne())
                .createdDateTime(LocalDateTime.now())
                .build();
        post.publishCreatedEvent();
        return postRepository.save(post);
    }

    public Post getOne() {
        List<Post> posts = postRepository.findAll();
        if(posts.isEmpty()) {
            return createPost("도토리 다 판다");
        } else {
            return posts.get(0);
        }
    }

}