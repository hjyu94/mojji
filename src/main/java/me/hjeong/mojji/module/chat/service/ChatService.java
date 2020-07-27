package me.hjeong.mojji.module.chat.service;

import com.querydsl.core.types.Predicate;
import lombok.RequiredArgsConstructor;
import me.hjeong.mojji.module.account.Account;
import me.hjeong.mojji.module.chat.ChatMessage;
import me.hjeong.mojji.module.chat.ChatRoom;
import me.hjeong.mojji.module.chat.repository.ChatPredicates;
import me.hjeong.mojji.module.chat.repository.ChatRepository;
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

    public void markAsRead(Account account, ChatRoom chatRoom) {
        chatRoom.getMessages()
                .stream()
                .filter(chatMessage -> chatMessage.getReceiver().equals(account))
                .forEach(chatMessage -> {
                    chatMessage.setIsRead(true);
                });
    }

    public void deleteAllIncludingAccount(Account account) {
        Iterable<ChatRoom> chatRooms = chatRepository.findAll(ChatPredicates.findIncludingAccount(account));
        chatRooms.forEach(chatRepository::delete);
        chatRepository.flush();
    }
}
