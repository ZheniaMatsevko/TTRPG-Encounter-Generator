package com.example.backend.service.implementation;

import com.example.backend.dto.MonsterActivitiesDto;
import com.example.backend.dto.MonsterDto;
import com.example.backend.dto.MonsterTacticsDto;
import com.example.backend.dto.enums.EncounterDifficulty;
import com.example.backend.dto.generation.Encounter;
import com.example.backend.dto.generation.GenerationFilter;
import com.example.backend.service.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class EncounterGenerationService implements IEncounterGenerationService {

    private final IEncounterDifficultyService encounterDifficultyService;
    private final IMonstersGenerationService monstersGenerationService;
    private final IMonsterActivitiesService monsterActivitiesService;
    private final IMonsterTacticsService monsterTacticsService;

    @Override
    public Encounter generateEncounter(GenerationFilter generationFilter) {
        Encounter encounter = new Encounter();

        Map<MonsterDto, Integer> generatedMonstersToCount = monstersGenerationService.getMonstersByFilters(generationFilter.getFilters(),
                generationFilter.getNumberOfMonsters());
        int numberOfMonsters = generatedMonstersToCount.values().stream().mapToInt(Integer::intValue).sum();

        EncounterDifficulty encounterDifficulty = encounterDifficultyService.calculateEncounterDifficulty(generationFilter.getCharactersLevels(),
                generatedMonstersToCount, numberOfMonsters);

        if(generationFilter.isGenerateActivities()){
            List<MonsterActivitiesDto> monsterActivities = monsterActivitiesService.getMonsterActivitiesForEncounter(generatedMonstersToCount.size());
            encounter.setActivities(monsterActivities);
        }

        if(generationFilter.isGenerateTactics()){
            List<MonsterTacticsDto> monsterTactics = monsterTacticsService.getMonsterTacticsForEncounter(generatedMonstersToCount.size());
            encounter.setTactics(monsterTactics);
        }

        encounter.setDifficulty(encounterDifficulty);
        encounter.setMonstersWithCounts(generatedMonstersToCount);

        return encounter;
    }

}
