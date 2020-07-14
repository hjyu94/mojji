package me.hjeong.mojji.domain;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import me.hjeong.mojji.chat.ChatMessage;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Getter @Setter @EqualsAndHashCode(of = "id")
public class ChatRoom {

    @Id @GeneratedValue
    private Long id;

    @OneToMany
    private Set<Account> participants = new HashSet<>();

    @ElementCollection
    private List<ChatMessage> messages = new ArrayList<>();

}
