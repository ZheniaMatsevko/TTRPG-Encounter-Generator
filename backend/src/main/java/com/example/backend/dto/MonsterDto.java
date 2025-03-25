package com.example.backend.dto;

import com.example.backend.entity.enums.Habitat;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
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
    @Size(max = 20, message = "Monster size length must be less than 20 characters")
    private String size;

    @NotBlank
    @Size(max = 50, message = "Monster type length must be less than 50 characters")
    private String type;

    @Size(max = 50, message = "Monster tag length must be less than 50 characters")
    private String tag;

    @Size(max = 20, message = "Monster alignment length must be less than 20 characters")
    private String alignment;

    private float cr;

    private String source;

    private boolean isLegendary;
    private boolean isLair;
    private boolean isSpellcaster;

    private List<Habitat> habitats;
}
