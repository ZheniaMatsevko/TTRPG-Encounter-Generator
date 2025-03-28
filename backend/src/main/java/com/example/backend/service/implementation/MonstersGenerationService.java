package com.example.backend.service.implementation;

import com.example.backend.dto.MonsterDto;
import com.example.backend.dto.generation.FilterParam;
import com.example.backend.entity.MonsterEntity;
import com.example.backend.entity.enums.Habitat;
import com.example.backend.entity.enums.Size;
import com.example.backend.entity.enums.Type;
import com.example.backend.mapper.IMonsterMapper;
import com.example.backend.repository.IMonsterRepository;
import com.example.backend.service.IMonstersGenerationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class MonstersGenerationService implements IMonstersGenerationService {
    private final Random random = new Random();
    private final IMonsterRepository monsterRepository;

    @Override
    public Map<MonsterDto, Integer> getMonstersByFilters(List<FilterParam> params, int numberOfMonsters) {
        return Collections.emptyMap();
    }

    @Override
    public MonsterDto getRandomMonster() {
        MonsterEntity monsterEntity = monsterRepository.findRandomMonster();
        return IMonsterMapper.INSTANCE.entityToDto(monsterEntity);
    }

    @Override
    public Map<MonsterDto, Integer> generateForNoFilters(int numberOfMonsters) {
        int scenario = random.nextInt(3);
        log.info("Generating monsters for scenario: {}", scenario);
        Map<MonsterDto, Integer> result;

        if (scenario == 0) {
            result = generateScenario0(numberOfMonsters);
        } else if (scenario == 1) {
            result = generateScenario1(numberOfMonsters);
        } else if (numberOfMonsters <= 6) {
            result = generateScenario2(numberOfMonsters);
        } else {
            result = generateScenario0(numberOfMonsters);
        }
        return result;
    }

    private Map<MonsterDto, Integer> generateScenario0(int numberOfMonsters) {
        List<MonsterEntity> monsters = monsterRepository.findRandomMonstersBySize(Size.SMALL);
        MonsterEntity monsterEntity= monsters.isEmpty() ? null : monsters.get(random.nextInt(monsters.size()));
        MonsterDto monster = IMonsterMapper.INSTANCE.entityToDto(monsterEntity);
        int finalNumberOfMonsters = (numberOfMonsters > 0) ? numberOfMonsters : 10;

        return Map.of(monster, finalNumberOfMonsters);
    }

    private Map<MonsterDto, Integer> generateScenario1(int presetNumberOfMonsters) {
        Map<MonsterDto, Integer> result = new HashMap<>();
        int numberOfMonsters = presetNumberOfMonsters != 0 ? presetNumberOfMonsters : random.nextInt(3, 10);
        MonsterDto leader = new MonsterDto();
        do {
            Type randomType = getRandomType();

            List<MonsterDto> monsters = monsterRepository.findAllByType(
                            randomType)
                    .stream()
                    .map(IMonsterMapper.INSTANCE::entityToDto)
                    .toList();

            if (monsters.size() >= 2) {

                List<MonsterDto> filteredMonsters = new LinkedList<>();
                boolean foundMonsters = false;
                for (int i = 0; i < monsters.size(); i++) {
                    for (int j = i + 1; j < monsters.size(); j++) {
                        float cr1 = monsters.get(i).getCr();
                        float cr2 = monsters.get(j).getCr();
                        float max = Math.max(cr1, cr2);
                        float min = Math.min(cr1, cr2);
                        float diff = max - min;

                        if (diff >= 3 && diff <= max / 2) {
                            leader = cr1 > cr2 ? monsters.get(i) : monsters.get(j);
                            filteredMonsters.addAll(monsters.stream().filter(monster -> monster.getCr() == min).toList());
                            foundMonsters = true;
                            break;
                        }
                    }
                    if (foundMonsters)
                        break;
                }

                if (foundMonsters) {
                    numberOfMonsters--;
                    result = generateMonsterMapWithRandomCounts(filteredMonsters, numberOfMonsters);

                    result.put(leader, 1);
                }
            }

        } while (result.isEmpty());

        return result;
    }

    private Map<MonsterDto, Integer> generateScenario2(int presetNumberOfMonsters) {
        Map<MonsterDto, Integer> result = new HashMap<>();
        int numberOfMonsters = presetNumberOfMonsters == 0 ? 6 : presetNumberOfMonsters;

        do {
            Habitat randomHabitat = getRandomHabitat();

            List<MonsterDto> monsters = monsterRepository.findAllByHabitat(
                            randomHabitat)
                    .stream()
                    .map(IMonsterMapper.INSTANCE::entityToDto)
                    .toList();

            if (monsters.size() >= 2) {
                float minCR = (float) monsters.stream().mapToDouble(MonsterDto::getCr).min().orElse(0);
                float maxCR = minCR + 2;

                List<MonsterDto> filteredMonsters = monsters.stream()
                        .filter(monster -> monster.getCr() >= minCR && monster.getCr() <= maxCR)
                        .toList();

                if (filteredMonsters.size() >= 2) {
                    result = generateMonsterMapWithRandomCounts(filteredMonsters, numberOfMonsters);
                }

            }

        } while (result.isEmpty());

        return result;
    }

    private Map<MonsterDto, Integer> generateMonsterMapWithRandomCounts(List<MonsterDto> filteredMonsters, int numberOfMonsters) {
        int numberOfDifferentMonsters = Math.min(filteredMonsters.size(), numberOfMonsters);
        filteredMonsters = filteredMonsters.subList(0, numberOfDifferentMonsters);
        AtomicInteger remainingMonsters = new AtomicInteger(numberOfMonsters - numberOfDifferentMonsters);

        Map<MonsterDto, Integer> monstersWithCounts = filteredMonsters.stream()
                .collect(Collectors.toMap(
                        monster -> monster,
                        monster -> {
                            int count = remainingMonsters.get() <= 1 ? 1 : random.nextInt(1, remainingMonsters.get() + 1);
                            remainingMonsters.addAndGet(-(count - 1));
                            return count;
                        }
                ));

        if (remainingMonsters.get() > 0) {
            monstersWithCounts.put(filteredMonsters.get(0), monstersWithCounts.get(filteredMonsters.get(0)) + remainingMonsters.get());
        }

        return monstersWithCounts;
    }


    private Habitat getRandomHabitat() {
        Habitat[] habitats = Habitat.values();
        return habitats[random.nextInt(habitats.length)];
    }

    private Type getRandomType() {
        Type[] types = Type.values();
        return types[random.nextInt(types.length)];
    }

}
