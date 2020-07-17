package me.hjeong.mojji.formatter;

import me.hjeong.mojji.account.AccountRepository;
import me.hjeong.mojji.domain.Account;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.Formatter;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.util.Locale;

@Component
public class AccountFormatter implements Formatter<Account> {

    @Autowired
    AccountRepository accountRepository;

    @Override
    public Account parse(String s, Locale locale) throws ParseException {
        return accountRepository.findByNickname(s);
    }

    @Override
    public String print(Account account, Locale locale) {
        return account.getNickname();
    }

}
