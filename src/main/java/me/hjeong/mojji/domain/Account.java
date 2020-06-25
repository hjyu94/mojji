package me.hjeong.mojji.domain;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

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

    private boolean notiByWeb = true;

    private boolean notiByEmail = false;

    @OneToMany
    private Set<Location> locations = new HashSet<>();

    @Lob
    @Basic(fetch = FetchType.EAGER)
    private String profileImage; // Varchar(255) -> Test, 기본적으로 LAZY Fetch

    public void generateEmailCheckToken() {
        emailCheckToken = UUID.randomUUID().toString();
    }

    public String getZonesToString() {
        return "";
    }

}
