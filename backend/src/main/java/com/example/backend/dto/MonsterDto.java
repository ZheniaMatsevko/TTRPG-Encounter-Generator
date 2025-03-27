package com.example.backend.dto;

import com.example.backend.entity.enums.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MonsterDto {
    private Long id;

    @NotBlank
    private String name;

    @NotBlank
    private Size size;

    @NotBlank
    private Type type;

    private Tag tag;

    private Alignment alignment;

    private float cr;

    private boolean legendary;
    private boolean lair;
    private boolean spellcaster;

    private List<Habitat> habitats;
}
