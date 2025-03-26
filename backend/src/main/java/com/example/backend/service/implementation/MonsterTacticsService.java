package com.example.backend.service.implementation;

import com.example.backend.dto.MonsterTacticsDto;
import com.example.backend.entity.MonsterTacticsEntity;
import com.example.backend.mapper.IMonsterTacticsMapper;
import com.example.backend.repository.IMonsterTacticsRepository;
import com.example.backend.service.IMonsterTacticsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class MonsterTacticsService implements IMonsterTacticsService {
    private final IMonsterTacticsRepository monsterTacticsRepository;

    @Override
    public MonsterTacticsDto createMonsterTactics(MonsterTacticsDto monsterTactics) {
        log.info("Creating monster tactics");
        MonsterTacticsEntity createdMonsterTactics = monsterTacticsRepository.save(IMonsterTacticsMapper.INSTANCE.dtoToEntity(monsterTactics));
        log.info("Monster tactics created successfully.");
        return IMonsterTacticsMapper.INSTANCE.entityToDto(createdMonsterTactics);
    }

    @Override
    public List<MonsterTacticsDto> getAllMonsterTactics() {
        return monsterTacticsRepository.findAll().stream().map(IMonsterTacticsMapper.INSTANCE::entityToDto).toList();
    }

    @Override
    public List<MonsterTacticsDto> getMonsterTacticsForEncounter(int numberOfTactics) {
        return null;
    }

}
