package com.example.backend.service.implementation;

import com.example.backend.dto.MonsterDto;
import com.example.backend.dto.enums.QuantityRequirement;
import com.example.backend.dto.generation.FilterParam;
import com.example.backend.entity.MonsterEntity;
import com.example.backend.entity.enums.*;
import com.example.backend.mapper.IMonsterMapper;
import com.example.backend.repository.IMonsterRepository;
import com.example.backend.service.IMonstersGenerationService;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Filter;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Slf4j
@Service
@RequiredArgsConstructor
public class MonstersGenerationService implements IMonstersGenerationService {
    private final Random random = new Random();
    private final IMonsterRepository monsterRepository;

    @PersistenceContext
    private EntityManager entityManager;


//    @Override
//    public Map<MonsterDto, Integer> getMonstersByFilters(List<FilterParam> params, int numberOfMonsters) {
//        // todo: how to implement front without filters?
//        MonsterDto[] result = new MonsterDto[numberOfMonsters];
//        List<FilterParam> noneParams = new ArrayList<>();
//        List<FilterParam> allParams = new ArrayList<>();
//        List<FilterParam> anyParams = new ArrayList<>();
//        List<FilterParam> exactParams = new ArrayList<>();
//
//        Map<String, List<String>> allMap = new HashMap<>();
//        Map<String, List<String>> anyMap = new HashMap<>();
//        Map<String, List<String>> noneMap = new HashMap<>();
//        Map<String, List<String>> exactMap = new HashMap<>();
//
//        for (FilterParam p : params) {
//            switch (p.getQuantity().getQuantityRequirement()) {
//                case ALL -> {
//                    allParams.add(p);
//                    this.addFilterToMap(p, allMap);
//                }
//                case NONE -> {
//                    this.addFilterToMap(p, noneMap);
//                    noneParams.add(p);
//                }
//                case ANY -> {
//                    anyParams.add(p);
//                    this.addFilterToMap(p, anyMap);
//                }
//                case EXACT -> {
//                    exactParams.add(p);
//                    this.addFilterToMap(p, exactMap);
//                }
//                default -> System.out.println("no quantity param matched");
//            }
//        }
//
////        CriteriaBuilder criteriaBuilder = entityManager
////        this.monsterRepository.findAll()
//        // todo: finish this code to find with NONE and ALL filters
//
//
//        return Collections.emptyMap();
//    }

    // method to get all monsters for specified ALL and NONE params

    private List<MonsterDto> queryMonsterAllNone(Map<String, List<String>> allMap, Map<String, List<String>> noneMap) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<MonsterEntity> query = cb.createQuery(MonsterEntity.class);
        Root<MonsterEntity> root = query.from(MonsterEntity.class);

        List<Predicate> allPredicates = new ArrayList<>();
        List<Predicate> nonePredicates = new ArrayList<>();

        // Process ALL conditions
        for (Map.Entry<String, List<String>> entry : allMap.entrySet()) {
            String filterKey = entry.getKey();
            List<String> values = entry.getValue();

            switch (filterKey) {
                case "size":
                    List<Size> sizes = values.stream().map(Size::valueOf).collect(Collectors.toList());
                    allPredicates.add(root.get("size").in(sizes));
                    break;
                case "type":
                    List<Type> types = values.stream().map(Type::valueOf).collect(Collectors.toList());
                    allPredicates.add(root.get("type").in(types));
                    break;
                case "tag":
                    List<Tag> tags = values.stream().map(Tag::valueOf).collect(Collectors.toList());
                    allPredicates.add(root.get("tag").in(tags));
                    break;
                case "alignment":
                    List<Alignment> alignments = values.stream().map(Alignment::valueOf).collect(Collectors.toList());
                    allPredicates.add(root.get("alignment").in(alignments));
                    break;
                case "cr":
                    if (values.size() >= 2) {
                        float minCr = Float.parseFloat(values.get(0));
                        float maxCr = Float.parseFloat(values.get(1));
                        allPredicates.add(cb.between(root.get("cr"), minCr, maxCr));
                    }
                    break;
                case "legendary":
                    boolean legendary = Boolean.parseBoolean(values.get(0));
                    allPredicates.add(cb.equal(root.get("legendary"), legendary));
                    break;
                case "lair":
                    boolean lair = Boolean.parseBoolean(values.get(0));
                    allPredicates.add(cb.equal(root.get("lair"), lair));
                    break;
                case "spellcaster":
                    boolean spellcaster = Boolean.parseBoolean(values.get(0));
                    allPredicates.add(cb.equal(root.get("spellcaster"), spellcaster));
                    break;
                case "habitat":
                    List<Habitat> habitats = values.stream().map(Habitat::valueOf).collect(Collectors.toList());
                    Join<MonsterEntity, Habitat> habitatJoin = root.join("habitats", JoinType.INNER);
                    allPredicates.add(habitatJoin.in(habitats));
                    break;
                case "tactics":
                    List<Habitat> tactics = values.stream().map(Habitat::valueOf).collect(Collectors.toList());
                    Join<MonsterEntity, Habitat> tacticsJoin = root.join("tactics", JoinType.INNER);
                    allPredicates.add(tacticsJoin.in(tactics));
                    break;
                case "activities":
                    List<Habitat> activities = values.stream().map(Habitat::valueOf).collect(Collectors.toList());
                    Join<MonsterEntity, Habitat> activitiesJoin = root.join("activities", JoinType.INNER);
                    allPredicates.add(activitiesJoin.in(activities));
                    break;
                case "name":
                    String nameLike = "%" + values.get(0) + "%";
                    allPredicates.add(cb.like(root.get("name"), nameLike));
                    break;
            }
        }

        // Process NONE conditions
        for (Map.Entry<String, List<String>> entry : noneMap.entrySet()) {
            String filterKey = entry.getKey();
            List<String> values = entry.getValue();

            switch (filterKey) {
                case "size":
                    List<Size> sizes = values.stream().map(Size::valueOf).collect(Collectors.toList());
                    nonePredicates.add(cb.not(root.get("size").in(sizes)));
                    break;
                case "type":
                    List<Type> types = values.stream().map(Type::valueOf).collect(Collectors.toList());
                    nonePredicates.add(cb.not(root.get("type").in(types)));
                    break;
                case "tag":
                    List<Tag> tags = values.stream().map(Tag::valueOf).collect(Collectors.toList());
                    nonePredicates.add(cb.not(root.get("tag").in(tags)));
                    break;
                case "alignment":
                    List<Alignment> alignments = values.stream().map(Alignment::valueOf).collect(Collectors.toList());
                    nonePredicates.add(cb.not(root.get("alignment").in(alignments)));
                    break;
                case "legendary":
                    boolean legendary = Boolean.parseBoolean(values.get(0));
                    nonePredicates.add(cb.notEqual(root.get("legendary"), legendary));
                    break;
                case "lair":
                    boolean lair = Boolean.parseBoolean(values.get(0));
                    nonePredicates.add(cb.notEqual(root.get("lair"), lair));
                    break;
                case "spellcaster":
                    boolean spellcaster = Boolean.parseBoolean(values.get(0));
                    nonePredicates.add(cb.notEqual(root.get("spellcaster"), spellcaster));
                    break;
                case "habitat":
                    List<Habitat> habitats = values.stream().map(Habitat::valueOf).collect(Collectors.toList());
                    Subquery<Long> habitatSubquery = query.subquery(Long.class);
                    Root<MonsterEntity> habitatSubRoot = habitatSubquery.from(MonsterEntity.class);
                    Join<MonsterEntity, Habitat> habitatJoin = habitatSubRoot.join("habitats");
                    habitatSubquery.select(habitatSubRoot.get("id"))
                            .where(habitatJoin.in(habitats), cb.equal(habitatSubRoot.get("id"), root.get("id")));
                    nonePredicates.add(cb.not(cb.exists(habitatSubquery)));
                    break;
                case "tactics":
                    List<Habitat> tactics = values.stream().map(Habitat::valueOf).collect(Collectors.toList());
                    Subquery<Long> tacticsSubquery = query.subquery(Long.class);
                    Root<MonsterEntity> tacticsSubRoot = tacticsSubquery.from(MonsterEntity.class);
                    Join<MonsterEntity, Habitat> tacticsJoin = tacticsSubRoot.join("tactics");
                    tacticsSubquery.select(tacticsSubRoot.get("id"))
                            .where(tacticsJoin.in(tactics), cb.equal(tacticsSubRoot.get("id"), root.get("id")));
                    nonePredicates.add(cb.not(cb.exists(tacticsSubquery)));
                    break;
                case "activities":
                    List<Habitat> activities = values.stream().map(Habitat::valueOf).collect(Collectors.toList());
                    Subquery<Long> activitiesSubquery = query.subquery(Long.class);
                    Root<MonsterEntity> activitiesSubRoot = activitiesSubquery.from(MonsterEntity.class);
                    Join<MonsterEntity, Habitat> activitiesJoin = activitiesSubRoot.join("activities");
                    activitiesSubquery.select(activitiesSubRoot.get("id"))
                            .where(activitiesJoin.in(activities), cb.equal(activitiesSubRoot.get("id"), root.get("id")));
                    nonePredicates.add(cb.not(cb.exists(activitiesSubquery)));
                    break;
                case "name":
                    String nameLike = "%" + values.get(0) + "%";
                    nonePredicates.add(cb.notLike(root.get("name"), nameLike));
                    break;
            }
        }

        // Combine predicates
        List<Predicate> finalPredicates = new ArrayList<>();
        if (!allPredicates.isEmpty()) {
            finalPredicates.add(cb.and(allPredicates.toArray(new Predicate[0])));
        }
        if (!nonePredicates.isEmpty()) {
            finalPredicates.add(cb.and(nonePredicates.toArray(new Predicate[0])));
        }

        if (finalPredicates.isEmpty()) {
            query.select(root);
        } else {
            query.select(root).where(cb.and(finalPredicates.toArray(new Predicate[0])));
        }

        query.distinct(true);

        List<MonsterEntity> monsters = entityManager.createQuery(query).getResultList();
        return monsters.stream().map(this::convertToDto).collect(Collectors.toList());
    }

    @Override
    public Map<MonsterDto, Integer> getMonstersByFilters(List<FilterParam> params, int numberOfMonsters) {
        if (params == null || params.isEmpty()) {
            // Return all monsters if no filters provided
            List<MonsterEntity> allMonsters = monsterRepository.findAll();
            return allMonsters.stream()
                    .limit(numberOfMonsters)
                    .map(this::convertToDto)
                    .collect(Collectors.toMap(monster -> monster, monster -> 1));
        }

        List<FilterParam> noneParams = new ArrayList<>();
        List<FilterParam> allParams = new ArrayList<>();
        List<FilterParam> anyParams = new ArrayList<>();
        List<FilterParam> exactParams = new ArrayList<>();

        Map<String, List<String>> allMap = new HashMap<>();
        Map<String, List<String>> anyMap = new HashMap<>();
        Map<String, List<String>> noneMap = new HashMap<>();
        Map<String, List<String>> exactMap = new HashMap<>();

        for (FilterParam p : params) {
            switch (p.getQuantity().getQuantityRequirement()) {
                case ALL -> {
                    allParams.add(p);
                    this.addFilterToMap(p, allMap);
                }
                case NONE -> {
                    this.addFilterToMap(p, noneMap);
                    noneParams.add(p);
                }
                case ANY -> {
                    anyParams.add(p);
                    this.addFilterToMap(p, anyMap);
                }
                case EXACT -> {
                    exactParams.add(p);
                    this.addFilterToMap(p, exactMap);
                }
                default -> System.out.println("no quantity param matched");
            }
        }

        // Process ALL and NONE filters
        List<MonsterDto> allNoneFiltered = queryMonsterAllNone(allMap, noneMap);

        // Process ANY filters
        List<MonsterDto> anyFiltered = new ArrayList<>();
        if (!anyMap.isEmpty()) {
            CriteriaBuilder cb = entityManager.getCriteriaBuilder();
            CriteriaQuery<MonsterEntity> query = cb.createQuery(MonsterEntity.class);
            Root<MonsterEntity> root = query.from(MonsterEntity.class);

            List<Predicate> anyPredicates = new ArrayList<>();

            for (Map.Entry<String, List<String>> entry : anyMap.entrySet()) {
                String filterKey = entry.getKey();
                List<String> values = entry.getValue();

                switch (filterKey) {
                    case "size":
                        List<Size> sizes = values.stream().map(Size::valueOf).collect(Collectors.toList());
                        anyPredicates.add(root.get("size").in(sizes));
                        break;
                    case "type":
                        List<Type> types = values.stream().map(Type::valueOf).collect(Collectors.toList());
                        anyPredicates.add(root.get("type").in(types));
                        break;
                    case "tag":
                        List<Tag> tags = values.stream().map(Tag::valueOf).collect(Collectors.toList());
                        anyPredicates.add(root.get("tag").in(tags));
                        break;
                    case "alignment":
                        List<Alignment> alignments = values.stream().map(Alignment::valueOf).collect(Collectors.toList());
                        anyPredicates.add(root.get("alignment").in(alignments));
                        break;
                    case "habitat":
                        List<Habitat> habitats = values.stream().map(Habitat::valueOf).collect(Collectors.toList());
                        Subquery<Long> habitatSubquery = query.subquery(Long.class);
                        Root<MonsterEntity> habitatSubRoot = habitatSubquery.from(MonsterEntity.class);
                        Join<MonsterEntity, Habitat> habitatJoin = habitatSubRoot.join("habitats");
                        habitatSubquery.select(habitatSubRoot.get("id"))
                                .where(habitatJoin.in(habitats), cb.equal(habitatSubRoot.get("id"), root.get("id")));
                        anyPredicates.add(cb.exists(habitatSubquery));
                        break;
                    case "tactics":
                        List<Habitat> tactics = values.stream().map(Habitat::valueOf).collect(Collectors.toList());
                        Subquery<Long> tacticsSubquery = query.subquery(Long.class);
                        Root<MonsterEntity> tacticsSubRoot = tacticsSubquery.from(MonsterEntity.class);
                        Join<MonsterEntity, Habitat> tacticsJoin = tacticsSubRoot.join("tactics");
                        tacticsSubquery.select(tacticsSubRoot.get("id"))
                                .where(tacticsJoin.in(tactics), cb.equal(tacticsSubRoot.get("id"), root.get("id")));
                        anyPredicates.add(cb.exists(tacticsSubquery));
                        break;
                    case "activities":
                        List<Habitat> activities = values.stream().map(Habitat::valueOf).collect(Collectors.toList());
                        Subquery<Long> activitiesSubquery = query.subquery(Long.class);
                        Root<MonsterEntity> activitiesSubRoot = activitiesSubquery.from(MonsterEntity.class);
                        Join<MonsterEntity, Habitat> activitiesJoin = activitiesSubRoot.join("activities");
                        activitiesSubquery.select(activitiesSubRoot.get("id"))
                                .where(activitiesJoin.in(activities), cb.equal(activitiesSubRoot.get("id"), root.get("id")));
                        anyPredicates.add(cb.exists(activitiesSubquery));
                        break;
                    case "name":
                        Predicate[] namePredicates = values.stream()
                                .map(name -> cb.like(root.get("name"), "%" + name + "%"))
                                .toArray(Predicate[]::new);
                        anyPredicates.add(cb.or(namePredicates));
                        break;
                }
            }

            if (!anyPredicates.isEmpty()) {
                query.select(root).where(cb.or(anyPredicates.toArray(new Predicate[0])));
                query.distinct(true);
                List<MonsterEntity> monsters = entityManager.createQuery(query).getResultList();
                anyFiltered = monsters.stream().map(this::convertToDto).collect(Collectors.toList());
            }
        }

        // Process EXACT filters
        List<MonsterDto> exactFiltered = new ArrayList<>();
        if (!exactMap.isEmpty()) {
            CriteriaBuilder cb = entityManager.getCriteriaBuilder();
            CriteriaQuery<MonsterEntity> query = cb.createQuery(MonsterEntity.class);
            Root<MonsterEntity> root = query.from(MonsterEntity.class);

            List<Predicate> exactPredicates = new ArrayList<>();

            for (Map.Entry<String, List<String>> entry : exactMap.entrySet()) {
                String filterKey = entry.getKey();
                List<String> values = entry.getValue();

                switch (filterKey) {
                    case "size":
                        Size size = Size.valueOf(values.get(0));
                        exactPredicates.add(cb.equal(root.get("size"), size));
                        break;
                    case "type":
                        Type type = Type.valueOf(values.get(0));
                        exactPredicates.add(cb.equal(root.get("type"), type));
                        break;
                    case "tag":
                        Tag tag = Tag.valueOf(values.get(0));
                        exactPredicates.add(cb.equal(root.get("tag"), tag));
                        break;
                    case "alignment":
                        Alignment alignment = Alignment.valueOf(values.get(0));
                        exactPredicates.add(cb.equal(root.get("alignment"), alignment));
                        break;
                    case "legendary":
                        boolean legendary = Boolean.parseBoolean(values.get(0));
                        exactPredicates.add(cb.equal(root.get("legendary"), legendary));
                        break;
                    case "lair":
                        boolean lair = Boolean.parseBoolean(values.get(0));
                        exactPredicates.add(cb.equal(root.get("lair"), lair));
                        break;
                    case "spellcaster":
                        boolean spellcaster = Boolean.parseBoolean(values.get(0));
                        exactPredicates.add(cb.equal(root.get("spellcaster"), spellcaster));
                        break;
                    case "habitat":
                        // For exact match on collections we need additional logic
                        for (String habitatStr : values) {
                            Habitat habitat = Habitat.valueOf(habitatStr);
                            Subquery<Long> habitatSubquery = query.subquery(Long.class);
                            Root<MonsterEntity> habitatSubRoot = habitatSubquery.from(MonsterEntity.class);
                            Join<MonsterEntity, Habitat> habitatJoin = habitatSubRoot.join("habitats");
                            habitatSubquery.select(habitatSubRoot.get("id"))
                                    .where(cb.equal(habitatJoin, habitat), cb.equal(habitatSubRoot.get("id"), root.get("id")));
                            exactPredicates.add(cb.exists(habitatSubquery));
                        }

                        // Also check that no other habitats exist
                        Subquery<Long> habitatCountSubquery = query.subquery(Long.class);
                        Root<MonsterEntity> habitatCountRoot = habitatCountSubquery.from(MonsterEntity.class);
                        habitatCountSubquery.select(cb.count(habitatCountRoot.join("habitats")))
                                .where(cb.equal(habitatCountRoot.get("id"), root.get("id")));
                        exactPredicates.add(cb.equal(habitatCountSubquery, (long) values.size()));
                        break;
                    // Similar logic for tactics and activities...
                    case "name":
                        String name = values.get(0);
                        exactPredicates.add(cb.equal(root.get("name"), name));
                        break;
                }
            }

            if (!exactPredicates.isEmpty()) {
                query.select(root).where(cb.and(exactPredicates.toArray(new Predicate[0])));
                query.distinct(true);
                List<MonsterEntity> monsters = entityManager.createQuery(query).getResultList();
                exactFiltered = monsters.stream().map(this::convertToDto).collect(Collectors.toList());
            }
        }

        // Combine and process results
        Set<MonsterDto> resultSet = new HashSet<>();

        // Start with monsters that match ALL and NONE filters
        resultSet.addAll(allNoneFiltered);

        // If ANY filters exist, filter the results
        if (!anyParams.isEmpty() && !anyFiltered.isEmpty()) {
            resultSet.retainAll(new HashSet<>(anyFiltered));
        }

        // If EXACT filters exist, filter the results
        if (!exactParams.isEmpty() && !exactFiltered.isEmpty()) {
            resultSet.retainAll(new HashSet<>(exactFiltered));
        }

        // Convert to result map with score (simple scoring system)
        Map<MonsterDto, Integer> resultMap = new HashMap<>();
        for (MonsterDto monster : resultSet) {
            int score = calculateScore(monster, allParams, noneParams, anyParams, exactParams);
            resultMap.put(monster, score);
        }

        // Sort by score and limit to requested number
        return resultMap.entrySet().stream()
                .sorted(Map.Entry.<MonsterDto, Integer>comparingByValue().reversed())
                .limit(numberOfMonsters)
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue,
                        (e1, e2) -> e1,
                        LinkedHashMap::new));
    }

    // Helper method to calculate a relevance score for each monster
    private int calculateScore(MonsterDto monster, List<FilterParam> allParams,
                               List<FilterParam> noneParams, List<FilterParam> anyParams,
                               List<FilterParam> exactParams) {
        int score = 0;

        // ALL params contribute most to score
        score += allParams.size() * 3;

        // NONE params contribute negatively if they match
        score -= noneParams.size();

        // ANY params contribute positively if they match
        for (FilterParam param : anyParams) {
            // Logic to check if monster matches this parameter
            if (matchesParam(monster, param)) {
                score += 2;
            }
        }

        // EXACT params contribute highly if they match perfectly
        for (FilterParam param : exactParams) {
            if (matchesExactParam(monster, param)) {
                score += 5;
            }
        }

        return score;
    }

    // Helper method to check if a monster matches a filter parameter
    private boolean matchesParam(MonsterDto monster, FilterParam param) {
        // Implement the logic to check if the monster matches the given parameter
        // This will depend on your FilterParam and MonsterDto implementations
        // Return true if matches, false otherwise
        return true; // Placeholder
    }

    // Helper method for exact matching
    private boolean matchesExactParam(MonsterDto monster, FilterParam param) {
        // Implement exact matching logic
        return true; // Placeholder
    }

    // Helper method to convert MonsterEntity to MonsterDto
    private MonsterDto convertToDto(MonsterEntity entity) {
        // Implement conversion logic from entity to DTO
        MonsterDto dto = new MonsterDto();
        // Set properties
        return dto;
    }

    private void addFilterToMap(FilterParam param, Map<String, List<String>> map) {
        String key = param.getPropertyName();
        String value = param.getValue();
        List<String> list = map.get(key);
        if (list != null) {
            list.add(value);
            map.put(key, list);
        } else {
            map.put(param.getPropertyName(), List.of(param.getValue()));
        }
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
