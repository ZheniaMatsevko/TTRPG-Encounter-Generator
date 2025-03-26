package com.example.backend.service.implementation;

import com.example.backend.dto.MonsterActivitiesDto;
import com.example.backend.entity.MonsterActivitiesEntity;
import com.example.backend.mapper.IMonsterActivitiesMapper;
import com.example.backend.repository.IMonsterActivitiesRepository;
import com.example.backend.service.IMonsterActivitiesService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class MonsterActivitiesService implements IMonsterActivitiesService {
    private final IMonsterActivitiesRepository monsterActivitiesRepository;

    @Override
    public MonsterActivitiesDto createMonsterActivities(MonsterActivitiesDto monsterActivities) {
        log.info("Creating monster activities");
        MonsterActivitiesEntity createdMonsterActivities = monsterActivitiesRepository.save(IMonsterActivitiesMapper.INSTANCE.dtoToEntity(monsterActivities));
        log.info("Monster activities created successfully.");
        return IMonsterActivitiesMapper.INSTANCE.entityToDto(createdMonsterActivities);
    }

    @Override
    public List<MonsterActivitiesDto> getAllMonsterActivities() {
        return monsterActivitiesRepository.findAll().stream().map(IMonsterActivitiesMapper.INSTANCE::entityToDto).toList();
    }

    @Override
    public List<MonsterActivitiesDto> getMonsterActivitiesForEncounter(int numberOfActivities) {
        return null;
    }
}
