package me.hjeong.mojji.account;

import me.hjeong.mojji.domain.Account;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
public interface AccountRepository extends JpaRepository<Account, Long> {

    boolean existsByEmail(String Email);

    boolean existsByNickname(String nickname);

    Account findByEmail(String email);

    Account findByNickname(String nicknameOrEmail);

    @EntityGraph(attributePaths = {"stations"})
    Account findAccountWithStationsById(Long id);

    @EntityGraph(attributePaths = {"categories"})
    Account findAccountWithCategoriesById(Long id);

    @EntityGraph(attributePaths = {"stations", "categories"})
    Account findAccountWithStationsAndCategoriesById(Long id);
}
