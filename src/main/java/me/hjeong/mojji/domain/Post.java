package me.hjeong.mojji.domain;

import lombok.*;
import me.hjeong.mojji.account.UserAccount;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Getter @Setter @EqualsAndHashCode(of = "id")
@Builder @AllArgsConstructor @NoArgsConstructor
public class Post {

    @Id @GeneratedValue
    private Long id;

    @ElementCollection
    @Builder.Default
    private Set<String> images = new HashSet<>(); // url

    @ManyToOne
    private Account account;

    private String title;

    private String body;

    @Builder.Default
    private Integer lookCount = 0;

    private LocalDateTime createdDateTime;

    @ManyToMany
    @Builder.Default
    private Set<Station> stations = new HashSet<>();

    @ManyToOne
    private Category category;

    public boolean isCreatedBy(UserAccount userAccount) {
        return this.account.equals(userAccount.getAccount());
    }
}
