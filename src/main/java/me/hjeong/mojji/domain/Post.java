package me.hjeong.mojji.domain;

import lombok.*;
import me.hjeong.mojji.account.UserAccount;
import me.hjeong.mojji.post.event.PostCreatedEvent;
import org.springframework.data.domain.AbstractAggregateRoot;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Getter @Setter @EqualsAndHashCode(of = "id")
@Builder @AllArgsConstructor @NoArgsConstructor
public class Post extends AbstractAggregateRoot<Post> {

    @Id @GeneratedValue
    private Long id;

    @ElementCollection
    @Builder.Default
    private List<String> imgFileNames = new ArrayList<>();

    private Integer price;

    @ManyToOne
    private Account seller;

    @ManyToOne(fetch = FetchType.LAZY)
    @Builder.Default
    private Account buyer = null;

    private String title;

    private String body;

    @Builder.Default
    private Integer lookCount = 0;

    @Builder.Default
    private Integer letterCount = 0;

    private LocalDateTime createdDateTime;

    @Builder.Default
    private boolean isSold = false;

    @ManyToMany
    @Builder.Default
    private Set<Station> stations = new HashSet<>();

    @ManyToOne(fetch = FetchType.LAZY)
    private Category category;

    public boolean isCreatedBy(UserAccount userAccount) {
        return this.seller.equals(userAccount.getAccount());
    }

    public boolean isCreatedBy(Account account) {
        return this.seller.equals(account);
    }

    public Post publishCreatedEvent() {
        this.registerEvent(new PostCreatedEvent(this));
        return this;
    }

}
