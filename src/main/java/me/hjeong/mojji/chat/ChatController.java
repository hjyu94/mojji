package me.hjeong.mojji.chat;

import lombok.RequiredArgsConstructor;
import me.hjeong.mojji.account.CurrentAccount;
import me.hjeong.mojji.domain.Account;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
@RequiredArgsConstructor
public class ChatController {

    private final ChatRepository repository;
    private final ChatService service;

    @GetMapping("/chats")
    public String getChatRooms(@CurrentAccount Account account, Model model) {
        model.addAttribute("account", account);
        return "chats";
    }

    @GetMapping("/chat/{account-id}")
    public void getAChatRoom(@PathVariable("account-id") Long id) {

    }
}
