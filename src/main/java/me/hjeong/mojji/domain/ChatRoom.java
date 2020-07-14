package me.hjeong.mojji.domain;

import lombok.*;
import me.hjeong.mojji.chat.ChatMessage;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Getter @Setter @EqualsAndHashCode(of = "id")
@Builder @NoArgsConstructor @AllArgsConstructor
public class ChatRoom {

    @Id @GeneratedValue
    private Long id;

    @ManyToMany // OneToMany: 한 Account 는 하나의 ChatRoom 만을 가져야 한다.
    @Builder.Default
    private Set<Account> participants = new HashSet<>();

    @ElementCollection
    @Builder.Default
    private List<ChatMessage> messages = new ArrayList<>();

    public Account getReceiver(Account sender) {
        Account receiver = null;
        for(Account account :participants) {
            if (!account.equals(sender)){
                receiver = account;
            }
        }
        return receiver;
    }

}
