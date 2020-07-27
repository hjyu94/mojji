package me.hjeong.mojji.module.post;

import lombok.*;
import me.hjeong.mojji.module.account.Account;
import me.hjeong.mojji.module.account.UserAccount;
import me.hjeong.mojji.module.category.Category;
import me.hjeong.mojji.module.post.event.PostCreatedEvent;
import me.hjeong.mojji.module.station.Station;
import org.springframework.data.domain.AbstractAggregateRoot;

import javax.persistence.*;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
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

    @Column(nullable = false)
    private String title;

    private String body;

    @Builder.Default
    private Integer lookCount = 0;

    @Builder.Default
    private Integer letterCount = 0;

    @Column
    private LocalDateTime createdDateTime;

    @Builder.Default
    private boolean isSold = false;

    @ManyToMany
    @Builder.Default
    private Set<Station> stations = new HashSet<>();

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    private Category category;

    public boolean isCreatedBy(Account account) {
        return this.seller.equals(account);
    }

    // post/view.html 에서 수정, 삭제, 판매완료 버튼 보여줄지 말지 결정시에 사용
    public boolean isCreatedBy(UserAccount userAccount) {
        return this.seller.equals(userAccount.getAccount());
    }

    public Post publishCreatedEvent() {
        this.registerEvent(new PostCreatedEvent(this));
        return this;
    }

    public String getEncodedViewURL() {
        return "/post/" + URLEncoder.encode(this.id.toString(), StandardCharsets.UTF_8);
    }
}
