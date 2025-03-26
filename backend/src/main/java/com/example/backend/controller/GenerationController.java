package com.example.backend.controller;

import com.example.backend.dto.generation.Encounter;
import com.example.backend.dto.generation.GenerationFilter;
import com.example.backend.exceptions.ExceptionHelper;
import com.example.backend.service.IEncounterGenerationService;
import jakarta.validation.ValidationException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Slf4j
@RestController
@RequestMapping("/generate")
@RequiredArgsConstructor
public class GenerationController {
    private final IEncounterGenerationService generationService;

    @PostMapping
    public Encounter generateEncounter(@RequestBody @Valid GenerationFilter generationFilter, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            String message = ExceptionHelper.formErrorMessage(bindingResult);
            throw new ValidationException(message);
        }

        log.info("Generating encounter");
        return generationService.generateEncounter(generationFilter);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleException(Exception e) {
        String errorMessage = "ERROR: " + e.getMessage();
        log.error(errorMessage);
        return new ResponseEntity<>(errorMessage, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
