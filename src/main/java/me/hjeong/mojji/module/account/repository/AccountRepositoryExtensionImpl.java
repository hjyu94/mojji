package me.hjeong.mojji.module.account.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import me.hjeong.mojji.module.account.Account;
import me.hjeong.mojji.module.category.Category;
import me.hjeong.mojji.module.station.Station;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;

import java.util.List;
import java.util.Set;

import static me.hjeong.mojji.domain.QAccount.account;

public class AccountRepositoryExtensionImpl extends QuerydslRepositorySupport implements AccountRepositoryExtension {

    private final JPAQueryFactory queryFactory;

    public AccountRepositoryExtensionImpl(JPAQueryFactory queryFactory) {
        super(Account.class);
        this.queryFactory = queryFactory;
    }

    @Override
    public List<Account> findAnyAccountsByCategoriesAndStations(Set<Category> categorySet, Set<Station> stationSet) {
        return queryFactory.selectFrom(account)
                .where(account.stations.any().in(stationSet).or(account.categories.any().in(categorySet)))
                .fetch();
    }
}
