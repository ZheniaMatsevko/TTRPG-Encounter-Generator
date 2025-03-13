package com.example.backend.repository;

import com.example.backend.entity.MonsterTacticsEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IMonsterTacticsRepository extends JpaRepository<MonsterTacticsEntity, Long> {
}
