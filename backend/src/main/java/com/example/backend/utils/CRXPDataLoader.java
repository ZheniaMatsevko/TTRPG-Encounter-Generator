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
public class CRXPDataLoader {

    private final Map<Float, Integer> crXpMap;

    public CRXPDataLoader() {
        this.crXpMap = loadCRXPData();
    }

    private Map<Float, Integer> loadCRXPData() {
        ObjectMapper objectMapper = new ObjectMapper();
        try (InputStream inputStream = getClass().getClassLoader().getResourceAsStream("cr_xp.json")) {
            if (inputStream == null) {
                throw new JsonDataLoadException("cr_xp.json file not found");
            }
            return objectMapper.readValue(inputStream, new TypeReference<>() {
            });
        } catch (IOException e) {
            throw new JsonDataLoadException("Error loading CR-XP data", e);
        }
    }

    public Integer getXpByCr(float cr) {
        return crXpMap.getOrDefault(cr, 0);
    }
}
