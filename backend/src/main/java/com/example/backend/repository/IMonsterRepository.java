package com.example.backend.repository;

import com.example.backend.entity.MonsterEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IMonsterRepository extends JpaRepository<MonsterEntity, Long> {
}
