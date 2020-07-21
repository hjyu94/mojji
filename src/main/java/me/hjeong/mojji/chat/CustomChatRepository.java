package me.hjeong.mojji.chat;

import me.hjeong.mojji.domain.Account;

public interface CustomChatRepository {
    long countTotalUnreadMessageByAccount(Account account);
}
