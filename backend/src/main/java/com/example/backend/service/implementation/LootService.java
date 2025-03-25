package com.example.backend.service.implementation;

import com.example.backend.dto.LootDto;
import com.example.backend.entity.LootEntity;
import com.example.backend.mapper.ILootMapper;
import com.example.backend.repository.ILootRepository;
import com.example.backend.service.ILootService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class LootService implements ILootService {
    private final ILootRepository lootRepository;

    @Override
    public LootDto createLoot(LootDto loot) {
        log.info("Creating loot");
        LootEntity createdLoot = lootRepository.save(ILootMapper.INSTANCE.dtoToEntity(loot));
        log.info("Loot created successfully.");
        return ILootMapper.INSTANCE.entityToDto(createdLoot);
    }

    @Override
    public List<LootDto> getAllLoots() {
        return lootRepository.findAll().stream().map(ILootMapper.INSTANCE::entityToDto).toList();
    }
}
