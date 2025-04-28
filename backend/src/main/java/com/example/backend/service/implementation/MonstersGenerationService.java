package com.example.backend.service.implementation;

import com.example.backend.dto.MonsterDto;
import com.example.backend.dto.enums.QuantityRequirement;
import com.example.backend.dto.generation.FilterParam;
import com.example.backend.dto.generation.Quantity;
import com.example.backend.entity.MonsterEntity;
import com.example.backend.entity.enums.*;
import com.example.backend.mapper.IMonsterMapper;
import com.example.backend.repository.IMonsterRepository;
import com.example.backend.service.IMonstersGenerationService;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.*;
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

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Map<MonsterDto, Integer> getMonstersByFilters(List<FilterParam> params, int numberOfMonsters) {
        Map<MonsterDto, Integer> result = new HashMap<>();

        List<FilterParam> filtersWithNoneOrAll = params.stream().filter(param -> param.getQuantity().getQuantityRequirement().equals(QuantityRequirement.NONE) || param.getQuantity().getQuantityRequirement().equals(QuantityRequirement.ALL)).toList();
        List<FilterParam> filtersWithoutNoneOrAll = params.stream().filter(param -> param.getQuantity().getQuantityRequirement().equals(QuantityRequirement.ANY) || param.getQuantity().getQuantityRequirement().equals(QuantityRequirement.EXACT)).toList();

        boolean emptyFilterResult = false;
        int currentNumberOfMonsters = 0;
        List<MonsterDto> chosenMonstersWithAny = new ArrayList<>();

        for (FilterParam filterParam : filtersWithoutNoneOrAll) {
            String propertyName = filterParam.getPropertyName();
            String value = filterParam.getValue();
            Quantity quantity = filterParam.getQuantity();

            List<MonsterEntity> monsters = getMonstersByFilter(filtersWithNoneOrAll, propertyName, value);
            MonsterEntity monsterEntity = monsters.isEmpty() ? null : monsters.get(random.nextInt(monsters.size()));
            MonsterDto chosenMonster = IMonsterMapper.INSTANCE.entityToDto(monsterEntity);

            emptyFilterResult = monsters.isEmpty() || emptyFilterResult;

            if (quantity.getQuantityRequirement().equals(QuantityRequirement.EXACT)) {
                result.put(chosenMonster, quantity.getNumber());
                currentNumberOfMonsters += quantity.getNumber();
            } else {
                chosenMonstersWithAny.add(chosenMonster);
            }
        }

        if (emptyFilterResult || (currentNumberOfMonsters + chosenMonstersWithAny.size() > numberOfMonsters && numberOfMonsters > 0)) {
            return Map.of();
        }

        if (filtersWithoutNoneOrAll.isEmpty()) {
            boolean canWorkScenario0 = filtersWithNoneOrAll.stream().noneMatch(filterParam ->
                    (filterParam.getPropertyName().equals("size") && filterParam.getValue().equals("SMALL")
                            && filterParam.getQuantity().getQuantityRequirement().equals(QuantityRequirement.NONE))
            || (filterParam.getPropertyName().equals("size") && filterParam.getValue().equals("MEDIUM")
                            && filterParam.getQuantity().getQuantityRequirement().equals(QuantityRequirement.ALL))
            || (filterParam.getPropertyName().equals("size") && filterParam.getValue().equals("TINY")
                            && filterParam.getQuantity().getQuantityRequirement().equals(QuantityRequirement.ALL))
            || (filterParam.getPropertyName().equals("size") && filterParam.getValue().equals("LARGE")
                            && filterParam.getQuantity().getQuantityRequirement().equals(QuantityRequirement.ALL))
            || (filterParam.getPropertyName().equals("size") && filterParam.getValue().equals("HUGE")
                            && filterParam.getQuantity().getQuantityRequirement().equals(QuantityRequirement.ALL))
            || (filterParam.getPropertyName().equals("size") && filterParam.getValue().equals("GARGANTUAN")
                            && filterParam.getQuantity().getQuantityRequirement().equals(QuantityRequirement.ALL))
            || (filterParam.getPropertyName().equals("size") && filterParam.getValue().equals("VARIES")
                            && filterParam.getQuantity().getQuantityRequirement().equals(QuantityRequirement.ALL)));

            return generateForNoneAndAllFilters(canWorkScenario0, numberOfMonsters, params);
        } else {
            int missingNumberOfMonsters = numberOfMonsters > 0 ? numberOfMonsters - currentNumberOfMonsters : currentNumberOfMonsters + chosenMonstersWithAny.size() + random.nextInt(2, 10);
            result.putAll(generateMonsterMapWithRandomCounts(chosenMonstersWithAny, missingNumberOfMonsters));
        }

        return result;
    }

    private Map<MonsterDto, Integer> generateForNoneAndAllFilters(boolean canWorkScenario0, int numberOfMonsters, List<FilterParam> params) {


        int scenario;
        if (canWorkScenario0) {
            scenario = random.nextInt(3);
        } else {
            scenario = random.nextInt(1, 3);
        }

        log.info("Generating monsters for scenario: {}", scenario);
        Map<MonsterDto, Integer> result;

        if (scenario == 0) {
            result = generateScenario0(numberOfMonsters, params);
        } else if (scenario == 1) {
            result = generateScenario1(numberOfMonsters, params);
            if (result.isEmpty() && canWorkScenario0) {
                result = generateScenario0(numberOfMonsters, params);
            } else {
                result = generateScenario2(numberOfMonsters, params);
            }
        } else if (numberOfMonsters <= 6) {
            result = generateScenario2(numberOfMonsters, params);
            if (result.isEmpty() && canWorkScenario0) {
                result = generateScenario0(numberOfMonsters, params);
            } else {
                result = generateScenario1(numberOfMonsters, params);
            }
        } else {
            result = generateScenario0(numberOfMonsters, params);
        }
        return result;
    }

    private List<MonsterEntity> getMonstersByFilter(List<FilterParam> filtersWithNoneOrAll, String propertyName, String value) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<MonsterEntity> query = criteriaBuilder.createQuery(MonsterEntity.class);
        Root<MonsterEntity> root = query.from(MonsterEntity.class);
        query.select(root);

        List<Predicate> predicates = new ArrayList<>();

        predicates.add(createPredicate(criteriaBuilder, root, propertyName, value, false));

        for (FilterParam filterParam : filtersWithNoneOrAll) {
            String propertyNameForFilter = filterParam.getPropertyName();
            QuantityRequirement quantityRequirement = filterParam.getQuantity().getQuantityRequirement();
            String valueForFilter = filterParam.getValue();

            boolean isNegation = quantityRequirement.equals(QuantityRequirement.NONE);
            predicates.add(createPredicate(criteriaBuilder, root, propertyNameForFilter, valueForFilter, isNegation));
        }

        query.where(criteriaBuilder.and(predicates.toArray(new Predicate[0])));
        TypedQuery<MonsterEntity> t_query = entityManager.createQuery(query);
        return t_query.getResultList();
    }

    private Predicate createPredicate(CriteriaBuilder criteriaBuilder, Root<MonsterEntity> root, String propertyName, String value, boolean negate) {
        Predicate predicate;

        switch (propertyName) {
            case "habitat" -> {
                if (negate) {
                    Subquery<Long> subquery = criteriaBuilder.createQuery().subquery(Long.class);
                    Root<MonsterEntity> subRoot = subquery.from(MonsterEntity.class);
                    Join<MonsterEntity, Habitat> subJoin = subRoot.joinList("habitats");
                    subquery.select(subRoot.get("id"))
                            .where(criteriaBuilder.equal(subJoin, Habitat.valueOf(value)));

                    return criteriaBuilder.not(root.get("id").in(subquery));
                } else {
                    Join<MonsterEntity, Habitat> habitatsJoin = root.joinList("habitats");
                    return criteriaBuilder.equal(habitatsJoin, Habitat.valueOf(value));
                }
            }
            case "type" -> predicate = criteriaBuilder.equal(root.get(propertyName), Type.valueOf(value));
            case "size" -> predicate = criteriaBuilder.equal(root.get(propertyName), Size.valueOf(value));
            case "tag" -> predicate = criteriaBuilder.equal(root.get(propertyName), Tag.valueOf(value));
            case "alignment" -> predicate = criteriaBuilder.equal(root.get(propertyName), Alignment.valueOf(value));
            case "legendary", "lair", "spellcaster" -> {
                boolean booleanValue = Boolean.parseBoolean(value);
                predicate = criteriaBuilder.equal(root.get(propertyName), booleanValue);
            }
            case "cr" -> {
                String[] range = value.split("-");
                if (range.length != 2) {
                    throw new IllegalArgumentException("Invalid CR range format: " + value);
                }
                float minCr = Float.parseFloat(range[0]);
                float maxCr = Float.parseFloat(range[1]);
                predicate = criteriaBuilder.between(root.get("cr"), minCr, maxCr);
            }
            default -> predicate = criteriaBuilder.equal(root.get(propertyName), value);
        }

        return negate ? criteriaBuilder.not(predicate) : predicate;
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
        MonsterEntity monsterEntity = monsters.isEmpty() ? null : monsters.get(random.nextInt(monsters.size()));
        MonsterDto monster = IMonsterMapper.INSTANCE.entityToDto(monsterEntity);
        int finalNumberOfMonsters = (numberOfMonsters > 0) ? numberOfMonsters : 10;

        return Map.of(monster, finalNumberOfMonsters);
    }

    private Map<MonsterDto, Integer> generateScenario0(int numberOfMonsters, List<FilterParam> params) {

        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<MonsterEntity> query = criteriaBuilder.createQuery(MonsterEntity.class);
        Root<MonsterEntity> root = query.from(MonsterEntity.class);
        query.select(root);

        List<Predicate> predicates = new ArrayList<>();

        predicates.add(createPredicate(criteriaBuilder, root, "size", "SMALL", false));

        for (FilterParam filterParam : params) {
            String propertyNameForFilter = filterParam.getPropertyName();
            QuantityRequirement quantityRequirement = filterParam.getQuantity().getQuantityRequirement();
            String valueForFilter = filterParam.getValue();

            boolean isNegation = quantityRequirement.equals(QuantityRequirement.NONE);
            predicates.add(createPredicate(criteriaBuilder, root, propertyNameForFilter, valueForFilter, isNegation));
        }

        query.where(criteriaBuilder.and(predicates.toArray(new Predicate[0])));
        TypedQuery<MonsterEntity> t_query = entityManager.createQuery(query);
        List<MonsterEntity> monsters = t_query.getResultList();
        MonsterEntity monsterEntity = monsters.isEmpty() ? null : monsters.get(random.nextInt(monsters.size()));
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

    private Map<MonsterDto, Integer> generateScenario1(int presetNumberOfMonsters, List<FilterParam> params) {
        Map<MonsterDto, Integer> result = new HashMap<>();
        int numberOfMonsters = presetNumberOfMonsters != 0 ? presetNumberOfMonsters : random.nextInt(3, 10);
        MonsterDto leader = new MonsterDto();
        FilterParam typeFilter = params.stream()
                .filter(param -> param.getPropertyName().equals("type") && param.getQuantity().getQuantityRequirement().equals(QuantityRequirement.ALL))
                .findFirst()
                .orElse(null);

        do {
            Type randomType = typeFilter != null ? Type.valueOf(typeFilter.getValue()) : getRandomType();

            CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
            CriteriaQuery<MonsterEntity> query = criteriaBuilder.createQuery(MonsterEntity.class);
            Root<MonsterEntity> root = query.from(MonsterEntity.class);
            query.select(root);

            List<Predicate> predicates = new ArrayList<>();

            predicates.add(createPredicate(criteriaBuilder, root, "type", randomType.name(), false));

            for (FilterParam filterParam : params) {
                String propertyNameForFilter = filterParam.getPropertyName();
                QuantityRequirement quantityRequirement = filterParam.getQuantity().getQuantityRequirement();
                String valueForFilter = filterParam.getValue();

                boolean isNegation = quantityRequirement.equals(QuantityRequirement.NONE);
                predicates.add(createPredicate(criteriaBuilder, root, propertyNameForFilter, valueForFilter, isNegation));
            }

            query.where(criteriaBuilder.and(predicates.toArray(new Predicate[0])));
            TypedQuery<MonsterEntity> t_query = entityManager.createQuery(query);
            List<MonsterDto> monsters = t_query.getResultList().stream()
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

        } while (result.isEmpty() && typeFilter == null);

        return result;
    }

    private Map<MonsterDto, Integer> generateScenario2(int presetNumberOfMonsters, List<FilterParam> params) {
        Map<MonsterDto, Integer> result = new HashMap<>();
        int numberOfMonsters = presetNumberOfMonsters == 0 ? 6 : presetNumberOfMonsters;
        FilterParam habitatFilter = params.stream()
                .filter(param -> param.getPropertyName().equals("habitat") && param.getQuantity().getQuantityRequirement().equals(QuantityRequirement.ALL))
                .findFirst()
                .orElse(null);

        do {
            Habitat randomHabitat = habitatFilter != null ? Habitat.valueOf(habitatFilter.getValue()) : getRandomHabitat();

            CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
            CriteriaQuery<MonsterEntity> query = criteriaBuilder.createQuery(MonsterEntity.class);
            Root<MonsterEntity> root = query.from(MonsterEntity.class);
            query.select(root);

            List<Predicate> predicates = new ArrayList<>();

            predicates.add(createPredicate(criteriaBuilder, root, "habitat", randomHabitat.name(), false));

            for (FilterParam filterParam : params) {
                String propertyNameForFilter = filterParam.getPropertyName();
                QuantityRequirement quantityRequirement = filterParam.getQuantity().getQuantityRequirement();
                String valueForFilter = filterParam.getValue();

                boolean isNegation = quantityRequirement.equals(QuantityRequirement.NONE);
                predicates.add(createPredicate(criteriaBuilder, root, propertyNameForFilter, valueForFilter, isNegation));
            }

            query.where(criteriaBuilder.and(predicates.toArray(new Predicate[0])));
            TypedQuery<MonsterEntity> t_query = entityManager.createQuery(query);
            List<MonsterEntity> monsters = t_query.getResultList();

            if (monsters.size() >= 2) {
                Map<Float, List<MonsterEntity>> crBuckets = new HashMap<>();
                for (MonsterEntity monster : monsters) {
                    float cr = monster.getCr();
                    crBuckets.computeIfAbsent(cr, k -> new ArrayList<>()).add(monster);
                }

                result = findMonstersWithCrDifferenceUpTo2(crBuckets, numberOfMonsters);
            }
        } while (result.isEmpty() && habitatFilter == null);

        return result;
    }

    private Map<MonsterDto, Integer> generateScenario2(int presetNumberOfMonsters) {
        Map<MonsterDto, Integer> result = new HashMap<>();
        int numberOfMonsters = presetNumberOfMonsters == 0 ? 6 : presetNumberOfMonsters;

        do {
            Habitat randomHabitat = getRandomHabitat();

            List<MonsterEntity> monsters = monsterRepository.findAllByHabitat(randomHabitat);

            if (monsters.size() >= 2) {
                Map<Float, List<MonsterEntity>> crBuckets = new HashMap<>();
                for (MonsterEntity monster : monsters) {
                    float cr = monster.getCr();
                    crBuckets.computeIfAbsent(cr, k -> new ArrayList<>()).add(monster);
                }

                result = findMonstersWithCrDifferenceUpTo2(crBuckets, numberOfMonsters);
            }
        } while (result.isEmpty());

        return result;
    }

    private Map<MonsterDto, Integer> findMonstersWithCrDifferenceUpTo2(Map<Float, List<MonsterEntity>> crBuckets, int numberOfMonsters) {

        for (Map.Entry<Float, List<MonsterEntity>> entry : crBuckets.entrySet()) {
            List<MonsterEntity> group = new ArrayList<>(entry.getValue());

            float cr = entry.getKey();

            for (float i = cr + 0.1f; i <= cr + 2; i += 0.1f) {
                List<MonsterEntity> adjacentGroup = crBuckets.getOrDefault(i, Collections.emptyList());
                group.addAll(adjacentGroup);
            }

            if (group.size() >= 2) {
                Collections.shuffle(group);
                List<MonsterDto> finalGroup = group.subList(0, Math.min(group.size(), numberOfMonsters)).stream()
                        .map(IMonsterMapper.INSTANCE::entityToDto)
                        .toList();

                return generateMonsterMapWithRandomCounts(finalGroup, numberOfMonsters);
            }
        }

        return new HashMap<>();
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
