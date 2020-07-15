package me.hjeong.mojji.chat;

import lombok.Data;
import me.hjeong.mojji.domain.Account;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class ChatMessageForm {

    @NotNull private Long chatRoomId;
    private Account sender;
    private Account receiver;
    @NotBlank private String message;

}
