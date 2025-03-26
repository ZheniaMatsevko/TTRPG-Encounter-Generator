package com.example.backend.service;

import com.example.backend.dto.MonsterDto;
import com.example.backend.dto.enums.EncounterDifficulty;

import java.util.List;
import java.util.Map;

public interface IEncounterDifficultyService {
    EncounterDifficulty calculateEncounterDifficulty(List<Integer> userLevels, Map<MonsterDto, Integer> monstersXPToCount, int totalNumberOfMonsters);
}
