package com.example.backend.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LootDto {
    private Long id;

    @NotBlank
    private String name;

    private float gpValue;

    @NotBlank
    @Size(max = 20, message = "Loot rarity length can be maximum 20 characters")
    private String rarity;

    private int lbWeight;

    @Size(max = 50, message = "Loot category length can be maximum 50 characters")
    private String category;
}
