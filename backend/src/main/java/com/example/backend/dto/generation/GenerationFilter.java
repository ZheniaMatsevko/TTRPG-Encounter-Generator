package com.example.backend.dto.generation;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GenerationFilter {
    private List<FilterParam> filters;
    private int numberOfMonsters;
    private boolean generateTactics;
    private boolean generateActivities;
    private boolean generateLoot;
    private List<Integer> charactersLevels;
}
