package me.hjeong.mojji.module.chat.controller;

import com.querydsl.core.types.Predicate;
import lombok.RequiredArgsConstructor;
import me.hjeong.mojji.module.account.repository.AccountRepository;
import me.hjeong.mojji.module.account.CurrentAccount;
import me.hjeong.mojji.module.account.Account;
import me.hjeong.mojji.module.chat.form.ChatMessageForm;
import me.hjeong.mojji.module.chat.repository.ChatPredicates;
import me.hjeong.mojji.module.chat.repository.ChatRepository;
import me.hjeong.mojji.module.chat.service.ChatService;
import me.hjeong.mojji.module.chat.ChatRoom;
import me.hjeong.mojji.module.post.Post;
import me.hjeong.mojji.module.post.PostService;
import me.hjeong.mojji.module.post.repository.PostRepository;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.NoSuchElementException;
import java.util.Set;

@Controller
@RequiredArgsConstructor
public class ChatController {

    private static final String CUR_CHAT_ROOM = "curChatRoom";

    private final ChatRepository chatRepository;
    private final ChatService chatService;
    private final AccountRepository accountRepository;
    private final PostRepository postRepository;
    private final PostService postService;

    @GetMapping("/chats")
    public String getChatRooms(@CurrentAccount Account sender, Model model) {
        model.addAttribute("account", sender);

        ChatRoom chatRoomInModel = (ChatRoom) model.getAttribute(CUR_CHAT_ROOM);
        if(null != chatRoomInModel) {
            ChatRoom curChatRoom = chatRepository.findById(chatRoomInModel.getId())
                    .orElseThrow(() -> new NoSuchElementException("채팅방을 찾을 수 없습니다."));
            model.addAttribute(CUR_CHAT_ROOM, curChatRoom);
        }

        Predicate predicate = ChatPredicates.findIncludingAccount(sender);
        Iterable<ChatRoom> chatRooms = chatRepository.findAll(predicate);
        model.addAttribute("chatRooms", chatRooms);

        return "chats";
    }

    // 글 뷰 -> 메세지 보내기
    @GetMapping("/chat/{nickname}")
    public String getAChatRoom(@CurrentAccount Account sender, @PathVariable("nickname") String receiverNickname
                            , @RequestParam(required = false) Long postId, RedirectAttributes attributes)
    {
        Account receiver = accountRepository.findByNickname(receiverNickname);
        if(receiver == null) {
            throw new NoSuchElementException("유저를 찾을 수 없습니다");
        }
        if(postId != null) {
            Post post = postRepository.findById(postId).orElseThrow(()-> new NoSuchElementException("해당하는 게시물을 찾을 수 없습니다."));
            postService.sendingLetterCountUp(post);
        }
        // 내가 나 자신에게 메세지를 보내려고 하는 경우 예외처리
        if(sender.equals(receiver)) {
            throw new AccessDeniedException("나 자신에게는 메세지를 보낼 수 없습니다.");
        }
        ChatRoom foundChatRoom = chatService.getChatRoomByParticipants(Set.of(sender, receiver));
        chatService.markAsRead(sender, foundChatRoom);
        attributes.addFlashAttribute(CUR_CHAT_ROOM, foundChatRoom);
        return "redirect:/chats";
    }

    // 채팅 멤버 클릭시 (방 입장시), 채팅 메세지 수신 시 메세지함 채우기
    @GetMapping("/chat/messages")
    public String getChatMessages(@CurrentAccount Account sender, @RequestParam String nickname, Model model) {
        Account receiver = accountRepository.findByNickname(nickname);
        ChatRoom chatRoom = chatService.getChatRoomByParticipants(Set.of(sender, receiver));
        chatService.markAsRead(sender, chatRoom);
        model.addAttribute("account", sender);
        model.addAttribute(CUR_CHAT_ROOM, chatRoom);
        return "chats :: #chatroom";
    }

    // 메세지 전송
    @PostMapping("/chat/message/add")
    public String addMessage(@CurrentAccount Account sender, @Valid ChatMessageForm form, Errors error, Model model) {
        ChatRoom chatRoom = chatRepository.findById(form.getChatRoomId()).orElseThrow(
                () -> new NoSuchElementException("채팅방을 찾을 수 없습니다")
        );
        if(!error.hasErrors()) {
           Account receiver = chatRoom.getReceiver(sender);
           chatService.addMessage(chatRoom, form.getMessage(), sender, receiver);
        }
        model.addAttribute("account", sender);
        model.addAttribute(CUR_CHAT_ROOM, chatRoom);
        return "chats :: #message-box";
    }

    @GetMapping("/chat/messages/count")
    @ResponseBody
    public Object getMessageCount(@CurrentAccount Account account) {
        HashMap<String, Object> map = new HashMap<String, Object>();
        long totalMsgCnt = 0;
        Iterable<ChatRoom> chatRoomIterable = chatRepository.findAll(ChatPredicates.findIncludingAccount(account));
        for(ChatRoom chatRoom : chatRoomIterable) {
            String receiver = chatRoom.getReceiver(account).getNickname();
            long count = chatRoom.getNotReadMessage(account);
            map.put(receiver, count);
            totalMsgCnt += count;
        }
        map.put("totalMsgCnt", totalMsgCnt);
        return map;
    }

}
