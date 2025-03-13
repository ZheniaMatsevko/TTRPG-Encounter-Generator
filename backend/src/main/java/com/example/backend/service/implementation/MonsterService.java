package com.example.backend.service.implementation;

import com.example.backend.repository.IMonsterActivitiesRepository;
import com.example.backend.repository.IMonsterRepository;
import com.example.backend.repository.IMonsterTacticsRepository;
import com.example.backend.service.IMonsterService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class MonsterService implements IMonsterService {
    private final IMonsterRepository monsterRepository;
    private final IMonsterActivitiesRepository monsterActivitiesRepository;
    private final IMonsterTacticsRepository monsterTacticsRepository;

}
