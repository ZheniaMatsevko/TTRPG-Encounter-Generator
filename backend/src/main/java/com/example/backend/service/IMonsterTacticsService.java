package com.example.backend.service;

import com.example.backend.dto.MonsterTacticsDto;

import java.util.List;

public interface IMonsterTacticsService {
    MonsterTacticsDto createMonsterTactics(MonsterTacticsDto monsterTactics);

    List<MonsterTacticsDto> getAllMonsterTactics();

    List<MonsterTacticsDto> getMonsterTacticsForEncounter(int numberOfTactics);

}
