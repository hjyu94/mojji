package me.hjeong.mojji.chat;

import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.BooleanExpression;
import me.hjeong.mojji.domain.Account;
import me.hjeong.mojji.domain.QChatRoom;

import java.util.Set;

public class ChatPredicates {

    private static QChatRoom chatRoom = QChatRoom.chatRoom;

    public static Predicate findIncludingAccount(Account account) {
        return chatRoom.participants.any().in(account);
    }

    public static Predicate findByParticipants(Set<Account> participants) {
        return participants.stream()
                .map(account -> chatRoom.participants.any().in(account))
                .reduce(BooleanExpression::and).orElseThrow();
    }
}
