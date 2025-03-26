package com.example.backend.service.implementation;

import com.example.backend.dto.MonsterDto;
import com.example.backend.entity.MonsterEntity;
import com.example.backend.mapper.IMonsterMapper;
import com.example.backend.repository.IMonsterRepository;
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

    @Override
    public MonsterDto createMonster(MonsterDto monster) {
        log.info("Creating monster");
        MonsterEntity createdMonster = monsterRepository.save(IMonsterMapper.INSTANCE.dtoToEntity(monster));
        log.info("Monster created successfully.");
        return IMonsterMapper.INSTANCE.entityToDto(createdMonster);
    }

    @Override
    public List<MonsterDto> getAllMonsters() {
        return monsterRepository.findAll().stream().map(IMonsterMapper.INSTANCE::entityToDto).toList();
    }

}
