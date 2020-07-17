package me.hjeong.mojji.chat;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class ChatMessageForm {

    @NotNull private Long chatRoomId;
    private String senderNickname;
    private String receiverNickname;
    @NotBlank private String message;

}
