package com.example.backend.service;

import com.example.backend.dto.MonsterActivitiesDto;
import com.example.backend.dto.MonsterDto;
import com.example.backend.dto.MonsterTacticsDto;

import java.util.List;

public interface IMonsterService {
    MonsterDto createMonster(MonsterDto monster);
    MonsterTacticsDto createMonsterTactics(MonsterTacticsDto monsterTactics);
    MonsterActivitiesDto createMonsterActivities(MonsterActivitiesDto monsterActivities);

    List<MonsterDto> getAllMonsters();
    List<MonsterTacticsDto> getAllMonsterTactics();
    List<MonsterActivitiesDto> getAllMonsterActivities();

}
