package me.hjeong.mojji.domain;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Getter @Setter @EqualsAndHashCode(of = "id")
@Builder @AllArgsConstructor @NoArgsConstructor
public class Post {

    @Id @GeneratedValue
    private Long id;

    @Lob
    @Basic(fetch = FetchType.EAGER)
    private Set<String> images;

    @ManyToOne
    private Account account;

    private String title;

    private String body;

    private Integer lookCount;

    private LocalDateTime createdDateTime;

    @OneToMany
    @Builder.Default
    private Set<Station> stations = new HashSet<>();

    @OneToMany
    @Builder.Default
    private Set<Category> categories = new HashSet<>();

}
