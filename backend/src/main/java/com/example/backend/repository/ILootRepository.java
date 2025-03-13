package com.example.backend.repository;

import com.example.backend.entity.LootEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ILootRepository extends JpaRepository<LootEntity, Long> {
}
