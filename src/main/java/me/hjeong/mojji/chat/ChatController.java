package me.hjeong.mojji.chat;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
@RequiredArgsConstructor
public class ChatController {

    private final ChatRepository repository;
    private final ChatService service;

    @GetMapping("/chats")
    public void getChatRooms() {

    }

    @GetMapping("/chat/{account-id}")
    public void getAChatRoom(@PathVariable("account-id") Long id) {

    }
}
