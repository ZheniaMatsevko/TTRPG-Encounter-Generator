package com.example.backend.repository;

import com.example.backend.entity.MonsterTacticsEntity;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IMonsterTacticsRepository extends JpaRepository<MonsterTacticsEntity, Long> {
    @Query("SELECT m FROM MonsterTacticsEntity m ORDER BY RANDOM()")
    List<MonsterTacticsEntity> findRandomTactics(Pageable pageable);
}
