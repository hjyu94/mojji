package me.hjeong.mojji.infra.factory;

import lombok.RequiredArgsConstructor;
import me.hjeong.mojji.module.account.repository.AccountRepository;
import me.hjeong.mojji.module.account.Account;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class AccountFactory {

    @Autowired
    AccountRepository accountRepository;

    public Account createAccount(String nickname) {
        Account account = new Account();
        account.setNickname(nickname);
        account.setEmail(nickname + "@email.com");
        account.setPassword("1234");
        return accountRepository.save(account);
    }

    public Account getOne() {
        List<Account> accounts = accountRepository.findAll();
        if(accounts.isEmpty()) {
            return createAccount("유저1");
        } else {
            return accounts.get(0);
        }
    }

}