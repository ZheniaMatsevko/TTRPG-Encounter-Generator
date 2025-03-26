package com.example.backend.controller;

import com.example.backend.dto.MonsterActivitiesDto;
import com.example.backend.dto.MonsterDto;
import com.example.backend.dto.MonsterTacticsDto;
import com.example.backend.exceptions.ExceptionHelper;
import com.example.backend.service.IMonsterActivitiesService;
import com.example.backend.service.IMonsterService;
import com.example.backend.service.IMonsterTacticsService;
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
@RequestMapping("/monsters")
@RequiredArgsConstructor
public class MonsterController {
    private final IMonsterService monsterService;
    private final IMonsterTacticsService monsterTacticsService;
    private final IMonsterActivitiesService monsterActivitiesService;

    @PostMapping
    public MonsterDto createMonster(@RequestBody @Valid MonsterDto monsterDto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            String message = ExceptionHelper.formErrorMessage(bindingResult);
            throw new ValidationException(message);
        }
        MonsterDto monster = monsterService.createMonster(monsterDto);
        log.info("Monster created with ID: {}", monster.getId());
        return monster;
    }

    @PostMapping("/multiple")
    public List<MonsterDto> createMultipleMonsters(@RequestBody @Valid List<MonsterDto> monsterDtos, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            String message = ExceptionHelper.formErrorMessage(bindingResult);
            throw new ValidationException(message);
        }

        List<MonsterDto> createdMonsters = new ArrayList<>();
        for (MonsterDto monsterDto : monsterDtos) {
            MonsterDto createdMonster = monsterService.createMonster(monsterDto);
            log.info("Monster created with ID: {}", createdMonster.getId());
            createdMonsters.add(createdMonster);
        }

        return createdMonsters;
    }

    @GetMapping
    public List<MonsterDto> getAllMonsters() {
        log.info("Getting all monsters");
        return monsterService.getAllMonsters();
    }

    @PostMapping("/tactics")
    public MonsterTacticsDto createMonsterTactics(@RequestBody @Valid MonsterTacticsDto monsterTacticsDto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            String message = ExceptionHelper.formErrorMessage(bindingResult);
            throw new ValidationException(message);
        }
        MonsterTacticsDto monsterTactics = monsterTacticsService.createMonsterTactics(monsterTacticsDto);
        log.info("Monster tactics created with ID: {}", monsterTactics.getId());
        return monsterTactics;
    }

    @PostMapping("/tactics/multiple")
    public List<MonsterTacticsDto> createMultipleMonsterTactics(@RequestBody @Valid List<MonsterTacticsDto> monsterTacticsDtos, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            String message = ExceptionHelper.formErrorMessage(bindingResult);
            throw new ValidationException(message);
        }

        List<MonsterTacticsDto> createdTactics = new ArrayList<>();
        for (MonsterTacticsDto tactic : monsterTacticsDtos) {
            MonsterTacticsDto monsterTacticsDto = monsterTacticsService.createMonsterTactics(tactic);
            log.info("Tactic created with ID: {}", monsterTacticsDto.getId());
            createdTactics.add(monsterTacticsDto);
        }

        return createdTactics;
    }

    @GetMapping("/tactics")
    public List<MonsterTacticsDto> getAllMonsterTactics() {
        log.info("Getting all monster tactics");
        return monsterTacticsService.getAllMonsterTactics();
    }

    @GetMapping("/tactics/random")
    public List<MonsterTacticsDto> getRandomTactics(@RequestParam int numberOfTactics) {
        log.info("Getting random tactics, number of tactics: {}", numberOfTactics);
        return monsterTacticsService.getMonsterTacticsForEncounter(numberOfTactics);
    }

    @PostMapping("/activities")
    public MonsterActivitiesDto createMonster(@RequestBody @Valid MonsterActivitiesDto monsterActivitiesDto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            String message = ExceptionHelper.formErrorMessage(bindingResult);
            throw new ValidationException(message);
        }
        MonsterActivitiesDto monsterActivities = monsterActivitiesService.createMonsterActivities(monsterActivitiesDto);
        log.info("Monster activities created with ID: {}", monsterActivities.getId());
        return monsterActivities;
    }

    @PostMapping("/activities/multiple")
    public List<MonsterActivitiesDto> createMultipleMonsterActivities(@RequestBody @Valid List<MonsterActivitiesDto> monsterActivitiesDtos, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            String message = ExceptionHelper.formErrorMessage(bindingResult);
            throw new ValidationException(message);
        }

        List<MonsterActivitiesDto> createdTactics = new ArrayList<>();
        for (MonsterActivitiesDto monsterActivitiesDto : monsterActivitiesDtos) {
            MonsterActivitiesDto createdActivity = monsterActivitiesService.createMonsterActivities(monsterActivitiesDto);
            log.info("Activity created with ID: {}", createdActivity.getId());
            createdTactics.add(createdActivity);
        }

        return createdTactics;
    }

    @GetMapping("/activities")
    public List<MonsterActivitiesDto> getAllMonsterActivities() {
        log.info("Getting all monster activities");
        return monsterActivitiesService.getAllMonsterActivities();
    }

    @GetMapping("/activities/random")
    public List<MonsterActivitiesDto> getRandomActivities(@RequestParam int numberOfActivities) {
        log.info("Getting random activities, number of activities: {}", numberOfActivities);
        return monsterActivitiesService.getMonsterActivitiesForEncounter(numberOfActivities);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleException(Exception e) {
        String errorMessage = "ERROR: " + e.getMessage();
        log.error(errorMessage);
        return new ResponseEntity<>(errorMessage, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
