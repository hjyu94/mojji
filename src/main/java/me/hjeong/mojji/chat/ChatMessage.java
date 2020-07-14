package me.hjeong.mojji.chat;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import me.hjeong.mojji.domain.Account;

import javax.persistence.Embeddable;
import javax.persistence.ManyToOne;
import java.time.LocalDateTime;

@Embeddable
@Getter @Setter
@NoArgsConstructor
public class ChatMessage {

    private String message;

    @ManyToOne
    private Account sender;

    @ManyToOne
    private Account receiver;

    private LocalDateTime createdDateTime;

}
