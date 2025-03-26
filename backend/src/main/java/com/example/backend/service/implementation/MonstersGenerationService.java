package com.example.backend.service.implementation;

import com.example.backend.dto.MonsterDto;
import com.example.backend.dto.generation.FilterParam;
import com.example.backend.service.IMonstersGenerationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class MonstersGenerationService implements IMonstersGenerationService {
    @Override
    public Map<MonsterDto, Integer> getMonstersByFilters(List<FilterParam> params, int numberOfMonsters) {
        return Collections.emptyMap();
    }
}
