package me.hjeong.mojji.module.chat.repository;

import me.hjeong.mojji.module.chat.ChatRoom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

public interface ChatRepository extends JpaRepository<ChatRoom, Long>, QuerydslPredicateExecutor<ChatRoom>, CustomChatRepository {
}
