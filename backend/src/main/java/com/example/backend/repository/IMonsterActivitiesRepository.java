package com.example.backend.repository;

import com.example.backend.entity.MonsterActivitiesEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IMonsterActivitiesRepository extends JpaRepository<MonsterActivitiesEntity, Long> {
}
