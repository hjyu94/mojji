package me.hjeong.mojji.chat;

import lombok.RequiredArgsConstructor;
import me.hjeong.mojji.domain.Account;
import me.hjeong.mojji.domain.ChatRoom;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
@RequiredArgsConstructor
public class CustomChatRepositoryImpl implements CustomChatRepository {

    private final ChatRepository chatRepository;

    @Override
    public long countTotalUnreadMessageByAccount(Account account) {
        long count = 0;
        Iterable<ChatRoom> chatRoomIterable = chatRepository.findAll(ChatPredicates.findIncludingAccount(account));
        for(ChatRoom chatRoom : chatRoomIterable) {
            count += chatRoom.getNotReadMessage(account);
        }
        return count;
    }

}
