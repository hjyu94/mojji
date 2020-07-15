package me.hjeong.mojji.chat;

import com.querydsl.core.types.Predicate;
import lombok.RequiredArgsConstructor;
import me.hjeong.mojji.domain.Account;
import me.hjeong.mojji.domain.ChatRoom;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Set;

@Service
@Transactional
@RequiredArgsConstructor
public class ChatService {

    private final ChatRepository chatRepository;

    public ChatRoom getChatRoomByParticipants(Set<Account> participants) {
        Predicate predicate = ChatPredicates.findByParticipants(participants);
        Optional<ChatRoom> chatRoomOptional = chatRepository.findOne(predicate);
        if(chatRoomOptional.isEmpty()) {
            ChatRoom chatRoom = ChatRoom.builder()
                    .participants(participants)
                    .build();
            return chatRepository.save(chatRoom);
        } else {
            return chatRoomOptional.get();
        }
    }

    public ChatRoom addMessage(ChatRoom chatRoom, String message, Account sender, Account receiver) {
        ChatMessage chatMessage = ChatMessage.builder()
                .message(message)
                .createdDateTime(LocalDateTime.now())
                .sender(sender)
                .receiver(receiver)
                .build();
        chatRoom.getMessages().add(chatMessage);
        return chatRepository.save(chatRoom);
    }
}
