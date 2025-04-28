package com.example.backend.service.implementation;

import com.example.backend.dto.LootDto;
import com.example.backend.entity.LootEntity;
import com.example.backend.mapper.ILootMapper;
import com.example.backend.repository.ILootRepository;
import com.example.backend.service.ILootService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Pageable;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Slf4j
@Service
@RequiredArgsConstructor
public class LootService implements ILootService {
    private final ILootRepository lootRepository;
    private final Random random = new Random();

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

    @Override
    public List<LootDto> generateLootByCR(float challengeRating) {
        log.info("Generating loot for CR: {}", challengeRating);
        return generateLoot(challengeRating);
    }

    @Override
    public List<LootDto> generateLootByMultipleCR(List<Float> challengeRatings) {
        float maxCR = challengeRatings.stream().max(Float::compare).orElse(0f);
        log.info("Generating loot for max CR: {} from list: {}", maxCR, challengeRatings);
        return generateLoot(maxCR);
    }

    private List<LootDto> generateLoot(float cr) {
        int dice = random.nextInt(100) + 1;
        log.info("Dice roll: {}", dice);

        float minValue = 0;
        float maxValue = 0;

        // CR <=4
        if (cr <= 4) {
            if (dice <= 30) {
                maxValue = 0.3f;
            } else if (dice <= 60) {
                minValue = 0.4f;
                maxValue = 2.4f;
            } else if (dice <= 70) {
                minValue = 1.5f;
                maxValue = 9f;
            } else if (dice <= 95) {
                minValue = 3f;
                maxValue = 18f;
            } else {
                minValue = 10f;
                maxValue = 60f;
            }
        }
        // CR 5-10
        else if (cr <= 10) {
            if (dice <= 30) {
                maxValue = 30f;
            } else if (dice <= 60) {
                minValue = 20f;
                maxValue = 120f;
            } else if (dice <= 70) {
                minValue = 120f;
                maxValue = 210f;
            } else if (dice <= 95) {
                minValue = 140f;
                maxValue = 240f;
            } else {
                minValue = 170f;
                maxValue = 300f;
            }
        }
        // CR 11-16
        else if (cr <= 16) {
            if (dice <= 20) {
                minValue = 100f;
                maxValue = 340f;
            } else if (dice <= 35) {
                minValue = 150f;
                maxValue = 900f;
            } else if (dice <= 75) {
                minValue = 200f;
                maxValue = 1050f;
            } else {
                minValue = 400f;
                maxValue = 1400f;
            }
        }
        // CR 17+
        else {
            if (dice <= 15) {
                minValue = 1800f;
                maxValue = 6300f;
            } else if (dice <= 55) {
                minValue = 2000f;
                maxValue = 7000f;
            } else {
                minValue = 3500f;
                maxValue = Float.MAX_VALUE;
            }
        }

        log.info("Determined GP value range: {} to {}", minValue, maxValue);

        Pageable pageable = PageRequest.of(0, 3);

        List<LootEntity> potentialLoot;
        if (minValue == 0) {
            potentialLoot = lootRepository.findRandomLootByMaxValue(maxValue, pageable);
        } else if (maxValue == Float.MAX_VALUE) {
            potentialLoot = lootRepository.findRandomLootByMinValue(minValue, pageable);
        } else {
            potentialLoot = lootRepository.findRandomLootByValueRange(minValue, maxValue, pageable);
        }

        List<LootEntity> selectedLoot = new ArrayList<>();
        float totalValue = 0;

        if (!potentialLoot.isEmpty()) {
            selectedLoot.add(potentialLoot.get(0));
            totalValue += potentialLoot.get(0).getGpValue();
        }

        if (potentialLoot.size() > 1 && totalValue + potentialLoot.get(1).getGpValue() <= maxValue) {
            selectedLoot.add(potentialLoot.get(1));
            totalValue += potentialLoot.get(1).getGpValue();

            if (potentialLoot.size() > 2 && totalValue + potentialLoot.get(2).getGpValue() <= maxValue && random.nextBoolean()) {
                selectedLoot.add(potentialLoot.get(2));
            }
        }

        log.info("Selected {} loot items with total value: {}", selectedLoot.size(),
                selectedLoot.stream().mapToDouble(LootEntity::getGpValue).sum());

        return selectedLoot.stream().map(ILootMapper.INSTANCE::entityToDto).toList();
    }
}
