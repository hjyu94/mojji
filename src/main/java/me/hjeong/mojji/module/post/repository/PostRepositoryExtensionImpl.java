package me.hjeong.mojji.module.post.repository;

import com.querydsl.core.QueryResults;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.JPQLQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import me.hjeong.mojji.module.account.Account;
import me.hjeong.mojji.module.category.Category;
import me.hjeong.mojji.domain.*;
import me.hjeong.mojji.module.post.Post;
import me.hjeong.mojji.module.station.Station;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;

import java.util.List;
import java.util.Set;

import static me.hjeong.mojji.domain.QPost.post;

public class PostRepositoryExtensionImpl extends QuerydslRepositorySupport implements PostRepositoryExtension {

    private final JPAQueryFactory queryFactory;

    public PostRepositoryExtensionImpl(JPAQueryFactory queryFactory) {
        super(Post.class);
        this.queryFactory = queryFactory;
    }

    @Override
    public Page<Post> findByKeywords(Pageable pageable, List<String> keywords) {
        BooleanExpression predicates = keywords.stream()
                .map(keyword -> post.category.title.containsIgnoreCase(keyword)
                        .or(post.stations.any().region.containsIgnoreCase(keyword))
                        .or(post.stations.any().line.containsIgnoreCase(keyword))
                        .or(post.stations.any().name.containsIgnoreCase(keyword))
                        .or(post.title.containsIgnoreCase(keyword))
                        .or(post.body.containsIgnoreCase(keyword)))
                .reduce(BooleanExpression::and).orElseThrow();

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

    @Override
    public List<Post> findFirst9ByCategoriesOrderByCreatedDateTimeDesc(Set<Category> categories) {
        return queryFactory.selectFrom(post)
                .where(post.category.in(categories))
                .orderBy(post.createdDateTime.desc())
                .limit(9)
                .fetch();
    }

    @Override
    public List<Post> findFirst9ByStationsOrderByCreatedDateTimeDesc(Set<Station> stations) {
        return queryFactory.selectFrom(post)
                .where(post.stations.any().in(stations))
                .orderBy(post.createdDateTime.desc())
                .limit(9)
                .fetch();
    }

}
