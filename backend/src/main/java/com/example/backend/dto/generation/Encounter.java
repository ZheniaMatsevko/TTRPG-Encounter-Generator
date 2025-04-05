package com.example.backend.dto.generation;

import com.example.backend.dto.LootDto;
import com.example.backend.dto.MonsterActivitiesDto;
import com.example.backend.dto.MonsterTacticsDto;
import com.example.backend.dto.enums.EncounterDifficulty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Encounter {
    private List<MonsterWithCountDto> monstersWithCounts;
    private EncounterDifficulty difficulty;
    private List<MonsterTacticsDto> tactics;
    private List<MonsterActivitiesDto> activities;
    private List<LootDto> loots;
}
