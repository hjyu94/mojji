package me.hjeong.mojji.post;

import me.hjeong.mojji.domain.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional(readOnly = true)
public interface PostRepository extends JpaRepository<Post, Long>, PostRepositoryExtension {
    List<Post> findFirst6ByOrderByCreatedDateTimeDesc();
}
