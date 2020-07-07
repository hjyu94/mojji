package me.hjeong.mojji.post;

import com.querydsl.core.QueryResults;
import com.querydsl.core.types.Predicate;
import com.querydsl.jpa.JPQLQuery;
import me.hjeong.mojji.domain.Post;
import me.hjeong.mojji.domain.QCategory;
import me.hjeong.mojji.domain.QPost;
import me.hjeong.mojji.domain.QStation;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;

import java.util.List;

public class PostRepositoryExtensionImpl extends QuerydslRepositorySupport implements PostRepositoryExtension {

    public PostRepositoryExtensionImpl() {
        super(Post.class);
    }

    @Override
    public Page<Post> findByKeywords(Pageable pageable, List<String> keywords) {
        QPost post = QPost.post;

        Predicate[] predicates = new Predicate[keywords.size()];
        for(int i=0; i<keywords.size(); ++i) {
            String keyword = keywords.get(i);
            predicates[i] = post.category.title.containsIgnoreCase(keyword)
                .or(post.stations.any().region.containsIgnoreCase(keyword))
                .or(post.stations.any().line.containsIgnoreCase(keyword))
                .or(post.stations.any().name.containsIgnoreCase(keyword))
                .or(post.title.containsIgnoreCase(keyword))
                .or(post.body.containsIgnoreCase(keyword));
        }

        JPQLQuery<Post> postJPQLQuery = from(post)
                .where(predicates)
                .leftJoin(post.category, QCategory.category).fetchJoin()
                .leftJoin(post.stations, QStation.station).fetchJoin()
                .distinct();
        JPQLQuery<Post> pageableQuery = getQuerydsl().applyPagination(pageable, postJPQLQuery);
        QueryResults<Post> fetchResults = pageableQuery.fetchResults();
        return new PageImpl<>(fetchResults.getResults(), pageable, fetchResults.getTotal());
    }
}
