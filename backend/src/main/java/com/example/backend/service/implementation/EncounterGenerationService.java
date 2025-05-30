package com.example.backend.service.implementation;

import com.example.backend.dto.MonsterActivitiesDto;
import com.example.backend.dto.MonsterDto;
import com.example.backend.dto.MonsterTacticsDto;
import com.example.backend.dto.LootDto;

import com.example.backend.dto.enums.EncounterDifficulty;
import com.example.backend.dto.generation.Encounter;
import com.example.backend.dto.generation.GenerationFilter;
import com.example.backend.dto.generation.MonsterWithCountDto;
import com.example.backend.entity.enums.Type;
import com.example.backend.service.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class EncounterGenerationService implements IEncounterGenerationService {

    private final IEncounterDifficultyService encounterDifficultyService;
    private final IMonstersGenerationService monstersGenerationService;
    private final IMonsterActivitiesService monsterActivitiesService;
    private final IMonsterTacticsService monsterTacticsService;
    private final ILootService lootService;

    @Override
    public Encounter generateEncounter(GenerationFilter generationFilter) {
        Encounter encounter = new Encounter();
        Map<MonsterDto, Integer> generatedMonstersToCount;
        int presetNumberOfMonsters = generationFilter.getNumberOfMonsters();

        if (isEmptyFilter(generationFilter)) {

            if (presetNumberOfMonsters == 1) {
                generatedMonstersToCount = Map.of(monstersGenerationService.getRandomMonster(), 1);
            } else {
                generatedMonstersToCount = monstersGenerationService.generateForNoFilters(generationFilter.getNumberOfMonsters());
            }

        } else {
            generatedMonstersToCount = monstersGenerationService.getMonstersByFilters(generationFilter.getFilters(),
                    generationFilter.getNumberOfMonsters());
        }

        List<MonsterWithCountDto> monsterList = generatedMonstersToCount.entrySet().stream()
                .map(entry -> new MonsterWithCountDto(entry.getKey(), entry.getValue()))
                .toList();

        encounter.setMonstersWithCounts(monsterList);

        if (generationFilter.getCharactersLevels() != null && !generationFilter.getCharactersLevels().isEmpty()) {
            int numberOfMonsters = presetNumberOfMonsters != 0 ? presetNumberOfMonsters : generatedMonstersToCount.values().stream().mapToInt(Integer::intValue).sum();
            System.out.println(numberOfMonsters);

            EncounterDifficulty encounterDifficulty = encounterDifficultyService.calculateEncounterDifficulty(generationFilter.getCharactersLevels(),
                    generatedMonstersToCount, numberOfMonsters);

            encounter.setDifficulty(encounterDifficulty);
        }

        int numberOfTypesOfMonsters = countDifferentTypesOfMonsters(generatedMonstersToCount.keySet().stream().toList());

        if (generationFilter.isGenerateActivities()) {
            List<MonsterActivitiesDto> monsterActivities = monsterActivitiesService.getMonsterActivitiesForEncounter(numberOfTypesOfMonsters);
            encounter.setActivities(monsterActivities);
        }

        if (generationFilter.isGenerateTactics()) {
            List<MonsterTacticsDto> monsterTactics = monsterTacticsService.getMonsterTacticsForEncounter(numberOfTypesOfMonsters);
            encounter.setTactics(monsterTactics);
        }

        if (generationFilter.isGenerateLoot()) {
            float maxCR = calculateMaxCR(generatedMonstersToCount.keySet().stream().toList());
            List<LootDto> loot = lootService.generateLootByCR(maxCR);
            encounter.setLoots(loot);
            log.info("Generated loot for encounter with max CR: {}", maxCR);
        }


        return encounter;
    }

    private int countDifferentTypesOfMonsters(List<MonsterDto> monsters) {
        Set<Type> uniqueTypes = monsters.stream()
                .map(MonsterDto::getType)
                .collect(Collectors.toSet());

        return uniqueTypes.size();
    }
    private float calculateMaxCR(List<MonsterDto> monsters) {
        return monsters.stream()
                .map(MonsterDto::getCr)
                .max(Float::compare)
                .orElse(1.0f);
    }
    private boolean isEmptyFilter(GenerationFilter generationFilter) {
        return (generationFilter.getFilters() == null || generationFilter.getFilters().isEmpty())
                && (generationFilter.getCharactersLevels() == null || generationFilter.getCharactersLevels().isEmpty());
    }

}
