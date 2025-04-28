package com.example.backend.service;

import com.example.backend.dto.LootDto;

import java.util.List;

public interface ILootService {
    LootDto createLoot(LootDto loot);
    List<LootDto> getAllLoots();
    List<LootDto> generateLootByCR(float challengeRating);
    List<LootDto> generateLootByMultipleCR(List<Float> challengeRatings);

}
