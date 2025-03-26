package com.example.backend.service;

import com.example.backend.dto.MonsterActivitiesDto;

import java.util.List;

public interface IMonsterActivitiesService {
    MonsterActivitiesDto createMonsterActivities(MonsterActivitiesDto monsterActivities);

    List<MonsterActivitiesDto> getAllMonsterActivities();

    List<MonsterActivitiesDto> getMonsterActivitiesForEncounter(int numberOfActivities);
}
