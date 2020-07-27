package me.hjeong.mojji.infra.factory;

import lombok.RequiredArgsConstructor;
import me.hjeong.mojji.module.account.Account;
import me.hjeong.mojji.module.chat.ChatRoom;
import me.hjeong.mojji.module.chat.repository.ChatRepository;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;

@Component
@RequiredArgsConstructor
public class ChatRoomFactory {

    private final ChatRepository chatRepository;

    public ChatRoom createChatRoom() {
        ChatRoom chatRoom = ChatRoom.builder().build();
        return chatRepository.save(chatRoom);
    }

    public ChatRoom createChatRoom(Set<Account> participants) {
        ChatRoom chatRoom = ChatRoom.builder()
                .participants(participants)
                .build();
        return chatRepository.save(chatRoom);
    }

    public ChatRoom getOne() {
        List<ChatRoom> all = chatRepository.findAll();
        if(all.isEmpty()) {
            return createChatRoom();
        } else {
            return all.get(0);
        }
    }
}