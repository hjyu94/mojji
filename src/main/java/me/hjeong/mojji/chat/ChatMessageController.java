package me.hjeong.mojji.chat;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.hjeong.mojji.formatter.AccountFormatter;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
public class ChatMessageController {

    private final SimpMessagingTemplate simpMessagingTemplate;

    @MessageMapping("/chat/{to}")
    // @MessageMapping annotation ensures that, if a message is sent to the /chat/{receiver-nickname} destination
    // , the sendMessage() method is called.
    public void sendMessage(@DestinationVariable String to, ChatMessageForm chatMessageForm) {
        log.debug("handling message, to: {} message: {}", to, chatMessageForm);
        // isExists receiver?
        simpMessagingTemplate.convertAndSend("/topic/messages/" + to, chatMessageForm);
    }

}
