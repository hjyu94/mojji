package me.hjeong.mojji.module.chat.repository;

import me.hjeong.mojji.module.account.Account;

public interface CustomChatRepository {
    long countTotalUnreadMessageByAccount(Account account);
}
