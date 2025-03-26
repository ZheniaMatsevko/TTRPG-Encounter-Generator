package com.example.backend.service.implementation;

import com.example.backend.dto.MonsterDto;
import com.example.backend.dto.enums.EncounterDifficulty;
import com.example.backend.service.IEncounterDifficultyService;
import com.example.backend.utils.CRXPDataLoader;
import com.example.backend.utils.EncounterDifficultyLoader;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.EnumMap;
import java.util.List;
import java.util.Map;

import static com.example.backend.utils.MonstersXPCalculationUtils.getMultiplier;

@Slf4j
@AllArgsConstructor
@Service
public class EncounterDifficultyService implements IEncounterDifficultyService {
    private final EncounterDifficultyLoader encounterDifficultyLoader;
    private final CRXPDataLoader crXPDataLoader;

    public EncounterDifficulty calculateEncounterDifficulty(List<Integer> userLevels, Map<MonsterDto, Integer> monstersXPToCount, int totalNumberOfMonsters) {
        log.info("Calculating encounter difficulty");

        int totalMonstersXp = this.calculateTotalMonsterXP(monstersXPToCount, totalNumberOfMonsters);
        Map<EncounterDifficulty, Integer> partyDifficultyLimits = this.getPartyDifficultyLimits(userLevels);

        for (EncounterDifficulty difficulty : EncounterDifficulty.values()) {
            if (partyDifficultyLimits.get(difficulty) <= totalMonstersXp) {
                return difficulty;
            }
        }

        return EncounterDifficulty.EASY;
    }

    private Map<EncounterDifficulty, Integer> getPartyDifficultyLimits(List<Integer> userLevels) {
        Map<EncounterDifficulty, Integer> totalDifficultyLimits = new EnumMap<>(EncounterDifficulty.class);

        log.info("Calculating party difficulty limits");

        for (Integer level : userLevels) {
            Map<String, Integer> difficultyForLevel = encounterDifficultyLoader.getDifficultyForLevel(level);
            for (Map.Entry<String, Integer> entry : difficultyForLevel.entrySet()) {
                EncounterDifficulty difficulty = EncounterDifficulty.valueOf(entry.getKey());
                totalDifficultyLimits.merge(difficulty, entry.getValue(), Integer::sum);
            }
        }

        return totalDifficultyLimits;
    }

    private int calculateTotalMonsterXP(Map<MonsterDto, Integer> monstersXPToCount, int totalNumberOfMonsters) {
        int baseXP = monstersXPToCount.entrySet().stream()
                .mapToInt(entry -> {
                    MonsterDto monster = entry.getKey();
                    Integer monsterCount = entry.getValue();
                    int xp = crXPDataLoader.getXpByCr(monster.getCr());
                    return xp * monsterCount;  // XP * count of monsters
                })
                .sum();
        log.info("Calculating total monsters XP");

        return Math.round(baseXP * getMultiplier(totalNumberOfMonsters));
    }
}
