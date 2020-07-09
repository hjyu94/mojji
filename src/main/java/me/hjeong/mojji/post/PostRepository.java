package me.hjeong.mojji.post;

import me.hjeong.mojji.domain.Post;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional(readOnly = true)
public interface PostRepository extends JpaRepository<Post, Long>, PostRepositoryExtension {
    // EntityGraphType.FETCH: 엔티티 그래프에 명시한 어트리뷰트는 EAGER 모드로 가지고 오고 나머지는 LAZY.
    @EntityGraph(attributePaths = {"category", "imgFileNames", "stations"}, type = EntityGraph.EntityGraphType.FETCH)
    List<Post> findFirst6ByOrderByCreatedDateTimeDesc();
}
