package com.example.backend.entity;

import com.example.backend.entity.enums.*;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

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

    @Column(nullable = false)
    private Size size;

    @Column(nullable = false)
    private Type type;

    private Tag tag;
    private Alignment alignment;

    @Column(nullable = false)
    private float cr;

    private boolean legendary;
    private boolean lair;
    private boolean spellcaster;

    @ElementCollection(targetClass = Habitat.class)
    @JoinTable(name = "monster_habitat", joinColumns = @JoinColumn(name = "monster_id"))
    @Column(name = "habitat", nullable = false)
    @Enumerated(EnumType.STRING)
    private List<Habitat> habitats;
}
