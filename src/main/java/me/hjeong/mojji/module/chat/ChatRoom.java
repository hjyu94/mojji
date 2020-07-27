package me.hjeong.mojji.module.chat;

import lombok.*;
import me.hjeong.mojji.module.account.Account;

import javax.persistence.*;
import java.util.*;

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

    public List<ChatMessage> getMessagesSortByCreatedTime(boolean isDesc) {
        Collections.sort(this.messages, new Comparator<ChatMessage>() {
            @Override
            public int compare(ChatMessage msg1, ChatMessage msg2) {
                int compare = msg1.getCreatedDateTime().compareTo(msg2.getCreatedDateTime());
                return isDesc? compare : compare * (-1);
            }
        });
        return this.messages;
    }

    public long getNotReadMessage(Account account) {
        return this.messages.stream()
                .filter(chatMessage -> chatMessage.getReceiver().equals(account))
                .filter(chatMessage -> chatMessage.getIsRead().equals(false))
                .count();
    }
}
