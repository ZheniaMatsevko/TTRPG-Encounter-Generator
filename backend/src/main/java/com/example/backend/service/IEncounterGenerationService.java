package com.example.backend.service;

import com.example.backend.dto.generation.Encounter;
import com.example.backend.dto.generation.GenerationFilter;

public interface IEncounterGenerationService {

    Encounter generateEncounter(GenerationFilter generationFilter);
}
