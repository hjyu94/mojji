package me.hjeong.mojji.post;

import me.hjeong.mojji.config.DataJpaConfig;
import me.hjeong.mojji.domain.Post;
import me.hjeong.mojji.factory.PostFactory;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasSize;

@DataJpaTest
@Import(DataJpaConfig.class)
class PostRepositoryTest {

    @Autowired PostRepository postRepository;
    @Autowired PostFactory postFactory;

    @Test
    void findAllOrderByCreatedDateTimeDesc() {
        for(int i=0; i<30; ++i) {
            postFactory.createPost("게시물-" + i);
        }
        List<Post> posts = postRepository.findFirst6ByOrderByCreatedDateTimeDesc();
        assertThat(posts, hasSize(6));
    }

}