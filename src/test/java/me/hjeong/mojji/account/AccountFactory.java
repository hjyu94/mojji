package me.hjeong.mojji.account;

import lombok.RequiredArgsConstructor;
import me.hjeong.mojji.domain.Account;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

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

}