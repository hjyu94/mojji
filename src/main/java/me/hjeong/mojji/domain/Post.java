package me.hjeong.mojji.domain;

import lombok.*;

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
    private Set<String> images = new HashSet<>(); // url

    @ManyToOne
    private Account account;

    private String title;

    private String body;

    @Builder.Default
    private Integer lookCount = 0;

    private LocalDateTime createdDateTime;

    @ManyToMany
    private Set<Station> stations = new HashSet<>();

    @ManyToOne
    private Category category;

}
