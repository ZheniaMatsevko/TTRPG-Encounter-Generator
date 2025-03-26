package com.example.backend.utils;

import com.example.backend.exceptions.JsonDataLoadException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

@Getter
@Component
public class EncounterDifficultyLoader {

    private final Map<Integer, Map<String, Integer>> difficultyMap;

    public EncounterDifficultyLoader() {
        this.difficultyMap = loadEncDifficultyData();
    }

    private Map<Integer, Map<String, Integer>> loadEncDifficultyData() {
        ObjectMapper objectMapper = new ObjectMapper();
        try (InputStream inputStream = getClass().getClassLoader().getResourceAsStream("encounter_difficulty.json")) {
            if (inputStream == null) {
                throw new JsonDataLoadException("encounter_difficulty.json file not found");
            }
            return objectMapper.readValue(inputStream, new TypeReference<>() {
            });
        } catch (IOException e) {
            throw new JsonDataLoadException("Error loading encounter difficulty data", e);
        }
    }

    public Map<String, Integer> getDifficultyForLevel(int level) {
        return difficultyMap.getOrDefault(level, Map.of());
    }
}