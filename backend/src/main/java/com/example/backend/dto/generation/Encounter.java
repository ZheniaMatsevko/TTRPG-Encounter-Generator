package com.example.backend.dto.generation;

import com.example.backend.dto.LootDto;
import com.example.backend.dto.MonsterActivitiesDto;
import com.example.backend.dto.MonsterDto;
import com.example.backend.dto.MonsterTacticsDto;
import com.example.backend.dto.enums.EncounterDifficulty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Encounter {
    private Map<MonsterDto, Integer> monstersWithCounts;
    private EncounterDifficulty difficulty;
    private List<MonsterTacticsDto> tactics;
    private List<MonsterActivitiesDto> activities;
    private List<LootDto> loots;
}
