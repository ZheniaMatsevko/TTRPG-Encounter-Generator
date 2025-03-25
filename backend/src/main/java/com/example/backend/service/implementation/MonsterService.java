package com.example.backend.service.implementation;

import com.example.backend.dto.MonsterActivitiesDto;
import com.example.backend.dto.MonsterDto;
import com.example.backend.dto.MonsterTacticsDto;
import com.example.backend.entity.MonsterActivitiesEntity;
import com.example.backend.entity.MonsterEntity;
import com.example.backend.entity.MonsterTacticsEntity;
import com.example.backend.mapper.IMonsterActivitiesMapper;
import com.example.backend.mapper.IMonsterMapper;
import com.example.backend.mapper.IMonsterTacticsMapper;
import com.example.backend.repository.IMonsterActivitiesRepository;
import com.example.backend.repository.IMonsterRepository;
import com.example.backend.repository.IMonsterTacticsRepository;
import com.example.backend.service.IMonsterService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class MonsterService implements IMonsterService {
    private final IMonsterRepository monsterRepository;
    private final IMonsterActivitiesRepository monsterActivitiesRepository;
    private final IMonsterTacticsRepository monsterTacticsRepository;

    @Override
    public MonsterDto createMonster(MonsterDto monster) {
        log.info("Creating monster");
        MonsterEntity createdMonster = monsterRepository.save(IMonsterMapper.INSTANCE.dtoToEntity(monster));
        log.info("Monster created successfully.");
        return IMonsterMapper.INSTANCE.entityToDto(createdMonster);
    }

    @Override
    public MonsterTacticsDto createMonsterTactics(MonsterTacticsDto monsterTactics) {
        log.info("Creating monster tactics");
        MonsterTacticsEntity createdMonsterTactics = monsterTacticsRepository.save(IMonsterTacticsMapper.INSTANCE.dtoToEntity(monsterTactics));
        log.info("Monster tactics created successfully.");
        return IMonsterTacticsMapper.INSTANCE.entityToDto(createdMonsterTactics);
    }

    @Override
    public MonsterActivitiesDto createMonsterActivities(MonsterActivitiesDto monsterActivities) {
        log.info("Creating monster activities");
        MonsterActivitiesEntity createdMonsterActivities = monsterActivitiesRepository.save(IMonsterActivitiesMapper.INSTANCE.dtoToEntity(monsterActivities));
        log.info("Monster activities created successfully.");
        return IMonsterActivitiesMapper.INSTANCE.entityToDto(createdMonsterActivities);
    }

    @Override
    public List<MonsterDto> getAllMonsters() {
        return monsterRepository.findAll().stream().map(IMonsterMapper.INSTANCE::entityToDto).toList();
    }

    @Override
    public List<MonsterTacticsDto> getAllMonsterTactics() {
        return monsterTacticsRepository.findAll().stream().map(IMonsterTacticsMapper.INSTANCE::entityToDto).toList();
    }

    @Override
    public List<MonsterActivitiesDto> getAllMonsterActivities() {
        return monsterActivitiesRepository.findAll().stream().map(IMonsterActivitiesMapper.INSTANCE::entityToDto).toList();
    }
}
