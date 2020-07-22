package me.hjeong.mojji.module.account;

import lombok.*;
import me.hjeong.mojji.module.category.Category;
import me.hjeong.mojji.module.station.Station;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Entity
@Getter @Setter @EqualsAndHashCode(of = "id")
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

    @ManyToMany(cascade = CascadeType.ALL)
    private Set<Station> stations = new HashSet<>();

    @ManyToMany(cascade = CascadeType.ALL)
    private Set<Category> categories = new HashSet<>();

    @Lob
    private String profileImage;

    public void generateEmailCheckToken() {
        emailCheckToken = UUID.randomUUID().toString();
    }

    public String getStationsString() {
        return stations.stream()
                .map(station -> {
                    String name = station.getName();
                    if(!name.endsWith("역")) {
                        name = name + "역";
                    }
                    return name;
                })
                .reduce((a,b) -> a + ", " + b).orElse("");
    }

    public String getCategoriesString() {
        return categories.stream().map(Category::getTitle).reduce((a,b) -> a + ", " + b).orElse("");
    }

}
