package me.hjeong.mojji.account;

import me.hjeong.mojji.factory.CategoryFactory;
import me.hjeong.mojji.category.CategoryRepository;
import me.hjeong.mojji.domain.Account;
import me.hjeong.mojji.domain.Category;
import me.hjeong.mojji.domain.Station;
import me.hjeong.mojji.factory.AccountFactory;
import me.hjeong.mojji.factory.StationFactory;
import me.hjeong.mojji.station.StationRepository;
import me.hjeong.mojji.station.StationService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.collection.IsIterableContainingInAnyOrder.containsInAnyOrder;

@SpringBootTest
@Transactional
class AccountRepositoryTest {

    @Autowired private AccountFactory accountFactory;
    @Autowired private CategoryFactory categoryFactory;
    @Autowired private StationFactory stationFactory;
    @Autowired private AccountRepository accountRepository;
    @Autowired private StationRepository stationRepository;
    @Autowired private CategoryRepository categoryRepository;
    @Autowired private StationService stationService;

    static final List<String> strCategoryList = List.of("카테고리1", "카테고리2");
    static final List<String> strStationList = List.of("역1(지역-호선)", "역2(지역-호선)");

    private static Stream<Arguments> findByCategoriesAndStationsProvider() {
        return Stream.of(
                Arguments.of(strCategoryList,                   strStationList,                     List.of("1","2","3")),
                Arguments.of(List.of(strCategoryList.get(0)),   List.of(strStationList.get(0)),     List.of("1","2","3")),
                Arguments.of(List.of(),                         List.of(strStationList.get(0)),     List.of("1","2")),
                Arguments.of(List.of(strCategoryList.get(0)),   List.of(),                          List.of("1","3"))
        );
    }

    @BeforeEach
    public void beforeEach() {
        List<Category> categoryList = strCategoryList.stream()
                .map(categoryFactory::createCategory)
                .collect(Collectors.toList());

        List<Station> stationList = strStationList.stream()
                .map(stationFactory::createStation)
                .collect(Collectors.toList());

        Account account1 = accountFactory.createAccount("1");
        account1.getCategories().add(categoryList.get(0));
        account1.getStations().add(stationList.get(0));
        accountRepository.save(account1);

        Account account2 = accountFactory.createAccount("2");
        account2.getStations().add(stationList.get(0));
        accountRepository.save(account2);

        Account account3 = accountFactory.createAccount("3");
        account3.getCategories().add(categoryList.get(0));
        accountRepository.save(account3);
    }

    @AfterEach
    public void afterEach() {
        accountRepository.deleteAll();
        categoryRepository.deleteAll();
        stationRepository.deleteAll();
    }

    @ParameterizedTest
    @MethodSource("findByCategoriesAndStationsProvider")
    @DisplayName("카테고리와 지역으로 account 검색하기")
    public void test(List<String> categoryStrs, List<String> stationStrs, List<String> expectAccountsNames) {
        Set<Category> categorySet = categoryStrs.stream().map(categoryRepository::findByTitle).collect(Collectors.toSet());
        Set<Station> stationSet = stationStrs.stream().map(stationService::getStationByString).collect(Collectors.toSet());

        List<Account> expectAccounts = expectAccountsNames.stream()
                .map(nickname -> accountRepository.findByNickname(nickname)).collect(Collectors.toList());
        List<Account> foundAccounts = accountRepository.findAnyAccountsByCategoriesAndStations(categorySet, stationSet);

        assertThat(foundAccounts, containsInAnyOrder(expectAccounts.toArray()));
    }

}