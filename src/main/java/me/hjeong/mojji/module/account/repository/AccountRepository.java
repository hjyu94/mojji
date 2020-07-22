package me.hjeong.mojji.module.account.repository;

import me.hjeong.mojji.module.account.Account;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
public interface AccountRepository extends JpaRepository<Account, Long>, AccountRepositoryExtension {

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
