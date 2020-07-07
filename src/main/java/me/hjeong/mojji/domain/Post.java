package me.hjeong.mojji.domain;

import lombok.*;
import me.hjeong.mojji.account.UserAccount;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter @Setter @EqualsAndHashCode(of = "id")
@Builder @AllArgsConstructor @NoArgsConstructor
public class Post {

    @Id @GeneratedValue
    private Long id;

    @ElementCollection
    @Builder.Default
    private List<String> imgFileNames = new ArrayList<>();

    private Integer price;

    @ManyToOne
    private Account seller;

    @ManyToOne
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
    private List<Station> stations = new ArrayList<>();

    @ManyToOne
    private Category category;

    public boolean isCreatedBy(UserAccount userAccount) {
        return this.seller.equals(userAccount.getAccount());
    }

    public boolean isCreatedBy(Account account) {
        return this.seller.equals(account);
    }
}
