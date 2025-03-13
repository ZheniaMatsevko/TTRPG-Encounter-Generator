package com.example.backend.service.implementation;

import com.example.backend.repository.ILootRepository;
import com.example.backend.service.ILootService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class LootService implements ILootService {
    private final ILootRepository lootRepository;
}
