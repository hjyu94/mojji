package me.hjeong.mojji.module.account.repository;

import me.hjeong.mojji.module.account.Account;
import me.hjeong.mojji.module.category.Category;
import me.hjeong.mojji.module.station.Station;

import java.util.List;
import java.util.Set;

public interface AccountRepositoryExtension {

    List<Account> findAnyAccountsByCategoriesAndStations(Set<Category> categorySet, Set<Station> stationSet);

}
