package com.example.backend.controller;

import com.example.backend.dto.LootDto;
import com.example.backend.exceptions.ExceptionHelper;
import com.example.backend.service.ILootService;
import jakarta.validation.ValidationException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/loots")
@RequiredArgsConstructor
public class LootController {
    private final ILootService lootService;

    @PostMapping
    public LootDto createLoot(@RequestBody @Valid LootDto lootDto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            String message = ExceptionHelper.formErrorMessage(bindingResult);
            throw new ValidationException(message);
        }
        LootDto loot = lootService.createLoot(lootDto);
        log.info("Loot created with ID: {}", loot.getId());
        return loot;
    }

    @PostMapping("/multiple")
    public List<LootDto> createMultipleLoots(@RequestBody @Valid List<LootDto> lootDtos, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            String message = ExceptionHelper.formErrorMessage(bindingResult);
            throw new ValidationException(message);
        }

        List<LootDto> createdTactics = new ArrayList<>();
        for (LootDto lootDto : lootDtos) {
            LootDto createdLoot = lootService.createLoot(lootDto);
            log.info("Loot created with ID: {}", createdLoot.getId());
            createdTactics.add(createdLoot);
        }

        return createdTactics;
    }

    @GetMapping
    public List<LootDto> getAll() {
        log.info("Getting all loots");
        return lootService.getAllLoots();
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleException(Exception e) {
        String errorMessage = "ERROR: " + e.getMessage();
        log.error(errorMessage);
        return new ResponseEntity<>(errorMessage, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
