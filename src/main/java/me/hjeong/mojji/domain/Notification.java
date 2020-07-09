package me.hjeong.mojji.domain;

import lombok.*;
import me.hjeong.mojji.notification.NotificationType;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter @Setter @EqualsAndHashCode(of = "id")
@Builder @NoArgsConstructor @AllArgsConstructor
public class Notification {

    @Id @GeneratedValue
    private Long id;

    private String title;

    private String message;

    private boolean checked;

    private String link;

    @ManyToOne
    private Account account;

    private LocalDateTime createdDateTime;

    @Enumerated(EnumType.STRING)
    private NotificationType type;

}