package com.example.backend.repository;

import com.example.backend.entity.LootEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.data.domain.Pageable;
import java.util.List;

@Repository
public interface ILootRepository extends JpaRepository<LootEntity, Long> {
    @Query("SELECT l FROM LootEntity l WHERE l.gpValue >= :minValue AND l.gpValue <= :maxValue ORDER BY RANDOM()")
    List<LootEntity> findRandomLootByValueRange(@Param("minValue") float minValue, @Param("maxValue") float maxValue, Pageable pageable);

    @Query("SELECT l FROM LootEntity l WHERE l.gpValue <= :maxValue ORDER BY RANDOM()")
    List<LootEntity> findRandomLootByMaxValue(@Param("maxValue") float maxValue, Pageable pageable);

    @Query("SELECT l FROM LootEntity l WHERE l.gpValue >= :minValue ORDER BY RANDOM()")
    List<LootEntity> findRandomLootByMinValue(@Param("minValue") float minValue, Pageable pageable);
}
