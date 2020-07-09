package me.hjeong.mojji.account;

import me.hjeong.mojji.domain.Account;
import me.hjeong.mojji.domain.Category;
import me.hjeong.mojji.domain.Station;

import java.util.List;
import java.util.Set;

public interface AccountRepositoryExtension {

    List<Account> findAnyAccountsByCategoriesAndStations(Set<Category> categorySet, Set<Station> stationSet);

}
