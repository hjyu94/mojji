package me.hjeong.mojji.domain;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;

@Entity
@Getter @Setter
@Builder @AllArgsConstructor @NoArgsConstructor
public class Account {

    @Id @GeneratedValue
    private Long id;

    @Column(unique = true, nullable = false)
    private String nickname;

    @Column(unique = true, nullable = false)
    private String email;

    @Column(nullable = false)
    private String password;

    private boolean emailVerified;

    private String emailCheckToken;

    private LocalDateTime sendEmailAt;

    private LocalDateTime joinedAt;

    @Lob
    @Basic(fetch = FetchType.EAGER)
    private String profileImage; // Varchar(255) -> Test, 기본적으로 LAZY Fetch

    public void generateEmailCheckToken() {
        emailCheckToken = UUID.randomUUID().toString();
    }

    public void confirmRegisterEmail() {
        emailVerified = true;
        joinedAt = LocalDateTime.now();
        emailCheckToken = null;
    }
}
