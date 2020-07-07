package me.hjeong.mojji.post;

import me.hjeong.mojji.domain.Post;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Arrays;
import java.util.List;

@DataJpaTest
class PostRepositoryTest {

    @Autowired PostRepository postRepository;

    @Test
    void findAllOrderByCreatedDateTimeDesc() {
        List<Post> posts = postRepository.findFirst6ByOrderByCreatedDateTimeDesc();
        System.out.println("******************" + Arrays.toString(posts.toArray()));
    }

}