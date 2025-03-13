package com.example.backend.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Entity
@Data
@Table(name = "monsters")
public class MonsterEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, length = 20)
    private String size;

    @Column(nullable = false, length = 50)
    private String type;

    @Column(length = 50)
    private String tag;

    @Column(length = 20)
    private String alignment;

    @Column(nullable = false)
    private float cr;

    private String source;

    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    @ManyToMany(fetch = FetchType.EAGER, cascade = { CascadeType.PERSIST, CascadeType.MERGE })
    @JoinTable(name = "monster_filters",
            joinColumns = @JoinColumn(name = "monster_id", nullable = false),
            inverseJoinColumns = @JoinColumn(name = "filter_id", nullable = false),
            uniqueConstraints = @UniqueConstraint(columnNames = {"monster_id", "filter_id"})
    )
    private List<FilterEntity> filters;

}
