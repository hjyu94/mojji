package me.hjeong.mojji.module.account.event;

import lombok.Getter;
import me.hjeong.mojji.module.account.Account;

@Getter
public class AccountCreatedEvent {

    private Account account;

    public AccountCreatedEvent(Account account) {
        this.account = account;
    }

}
