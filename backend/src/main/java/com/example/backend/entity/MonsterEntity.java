package com.example.backend.entity;

import com.example.backend.entity.enums.Habitat;
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

    private boolean legendary;
    private boolean lair;
    private boolean spellcaster;

    private String source;

    @ElementCollection(targetClass = Habitat.class)
    @JoinTable(name = "monster_habitat", joinColumns = @JoinColumn(name = "monster_id"))
    @Column(name = "habitat", nullable = false)
    @Enumerated(EnumType.STRING)
    private List<Habitat> habitats;
}
