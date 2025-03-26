package com.example.backend.service;

import com.example.backend.dto.MonsterDto;

import java.util.List;

public interface IMonsterService {
    MonsterDto createMonster(MonsterDto monster);

    List<MonsterDto> getAllMonsters();
}
