package me.hjeong.mojji.module.station;

import lombok.*;

import javax.persistence.*;

@Entity @Getter @Setter @EqualsAndHashCode(of = "id")
@Builder @AllArgsConstructor @NoArgsConstructor
public class Station {

    @Id
    @GeneratedValue
    private Long id;

    @Column(nullable = false)
    private String region;

    @Column(nullable = false)
    private String line;

    @Column(nullable = false)
    private String name;

    @Override
    public String toString() {
        // 평촌(서울-4호선)
        return String.format("%s(%s-%s)", name, region, line);
    }
}