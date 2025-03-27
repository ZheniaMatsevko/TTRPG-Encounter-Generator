package com.example.backend.service;

import com.example.backend.dto.MonsterDto;
import com.example.backend.dto.generation.FilterParam;

import java.util.List;
import java.util.Map;

public interface IMonstersGenerationService {
    Map<MonsterDto, Integer> getMonstersByFilters(List<FilterParam> params, int numberOfMonsters);
    Map<MonsterDto, Integer> generateForNoFilters(int numberOfMonsters);
    MonsterDto getRandomMonster();
}
