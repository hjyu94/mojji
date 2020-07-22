package me.hjeong.mojji.module.chat;

import lombok.*;
import me.hjeong.mojji.module.account.Account;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Embeddable
@Getter @Setter
@Builder @NoArgsConstructor @AllArgsConstructor
public class ChatMessage {

    @NotBlank // null, "", " "
    private String message;

    @ManyToOne(cascade = CascadeType.ALL, optional = false)
    private Account sender;

    @ManyToOne(cascade = CascadeType.ALL, optional = false)
    private Account receiver;

    @NotNull // null
    private LocalDateTime createdDateTime;

    @Builder.Default
    private Boolean isRead = false;

}
