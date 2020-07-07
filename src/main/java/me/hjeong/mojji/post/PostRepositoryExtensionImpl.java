package me.hjeong.mojji.post;

import com.querydsl.core.QueryResults;
import com.querydsl.core.types.Predicate;
import com.querydsl.jpa.JPQLQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import me.hjeong.mojji.domain.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;

import java.util.List;

import static me.hjeong.mojji.domain.QPost.post;

public class PostRepositoryExtensionImpl extends QuerydslRepositorySupport implements PostRepositoryExtension {

    private final JPAQueryFactory queryFactory;

    public PostRepositoryExtensionImpl(JPAQueryFactory queryFactory) {
        super(Post.class);
        this.queryFactory = queryFactory;
    }

    @Override
    public Page<Post> findByKeywords(Pageable pageable, List<String> keywords) {
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

    @Override
    public List<Post> findPostsBySellerAndIsSold(Account account, boolean isSold) {
        return queryFactory.selectFrom(post)
                .where(post.seller.eq(account).and(post.isSold.eq(isSold)))
                .fetch();
    }
}
