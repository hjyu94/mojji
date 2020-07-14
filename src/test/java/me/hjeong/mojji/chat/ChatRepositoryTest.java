package me.hjeong.mojji.chat;

import com.querydsl.core.types.Predicate;
import me.hjeong.mojji.domain.Account;
import me.hjeong.mojji.domain.ChatRoom;
import me.hjeong.mojji.factory.AccountFactory;
import me.hjeong.mojji.factory.ChatRoomFactory;
import me.hjeong.mojji.infra.MyDataJpaTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;

import java.util.Optional;
import java.util.Set;

import static org.hamcrest.CoreMatchers.hasItems;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@MyDataJpaTest
@ComponentScan(basePackages = "me.hjeong.mojji.chat")
class ChatRepositoryTest {

    @Autowired ChatRepository chatRepository;
    @Autowired ChatService chatService;
    @Autowired AccountFactory accountFactory;
    @Autowired ChatRoomFactory chatRoomFactory;

    @Test
    @DisplayName("메세지 보내기 클릭 시 채팅방 만들기")
    public void createChatRoom() {
        // given
        Account sender = accountFactory.createAccount("sender");
        Account receiver = accountFactory.createAccount("receiver");

        // when
        ChatRoom chatRoom = chatService.getChatRoomByParticipants(Set.of(sender, receiver));

        // then
        Set<Account> participants = chatRoom.getParticipants();
        assertThat(participants, hasItems(sender));
        assertThat(participants, hasItems(receiver));
        assertNotNull(chatRoom);

        Predicate predicate = ChatPredicates.findIncludingAccount(sender);
        Iterable<ChatRoom> chatRooms = chatRepository.findAll(predicate);
        assertThat(chatRooms, hasItems(chatRoom));
    }

    @Test
    @DisplayName("두 Accouunt 로 대화방 찾기")
    public void getChatRoomByParticipants() {
        // given
        Account sender = accountFactory.createAccount("sender");
        Account receiver = accountFactory.createAccount("receiver");
        Set<Account> participants = Set.of(sender, receiver);
        ChatRoom chatRoom = chatRoomFactory.createChatRoom(participants);

        // when
        Predicate predicate = ChatPredicates.findByParticipants(participants);
        ChatRoom foundChatRoom = chatRepository.findOne(predicate).orElseGet(null);

        // then
        assertEquals(chatRoom, foundChatRoom);
    }

    @Test
    @DisplayName("대화방 찾을 수 없는 케이스 테스트")
    public void getChatRoomByParticipants_fail() {
        // given
        Account sender = accountFactory.createAccount("sender");
        Account receiver = accountFactory.createAccount("receiver");
        Account another = accountFactory.createAccount("another");
        chatRoomFactory.createChatRoom(Set.of(sender, another));

        // when
        Predicate predicate = ChatPredicates.findByParticipants(Set.of(sender, receiver));
        Optional<ChatRoom> chatRoomOptional = chatRepository.findOne(predicate);

        // then
        assertTrue(chatRoomOptional.isEmpty());
    }

}