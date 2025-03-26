package com.example.backend.repository;

import com.example.backend.entity.MonsterActivitiesEntity;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IMonsterActivitiesRepository extends JpaRepository<MonsterActivitiesEntity, Long> {
    @Query("SELECT m FROM MonsterActivitiesEntity m ORDER BY RANDOM()")
    List<MonsterActivitiesEntity> findRandomActivities(Pageable pageable);
}
