package me.hjeong.mojji.chat;

import me.hjeong.mojji.domain.ChatRoom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

public interface ChatRepository extends JpaRepository<ChatRoom, Long>, QuerydslPredicateExecutor<ChatRoom>, CustomChatRepository {
}
