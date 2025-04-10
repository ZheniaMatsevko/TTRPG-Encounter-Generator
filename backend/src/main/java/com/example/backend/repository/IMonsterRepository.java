package com.example.backend.repository;

import com.example.backend.entity.MonsterEntity;
import com.example.backend.entity.enums.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.NativeQuery;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;

@Repository
public interface IMonsterRepository extends JpaRepository<MonsterEntity, Long> {
    @Query("SELECT m FROM MonsterEntity m WHERE m.size = :size ORDER BY RANDOM()")
    List<MonsterEntity> findRandomMonstersBySize(@Param("size") Size size);

    @Query("SELECT m FROM MonsterEntity m ORDER BY RANDOM()")
    MonsterEntity findRandomMonster();

    @Query("SELECT m FROM MonsterEntity m WHERE :habitat MEMBER OF m.habitats")
    List<MonsterEntity> findAllByHabitat(Habitat habitat);

    @Query("SELECT DISTINCT m FROM MonsterEntity m " +
            "WHERE (:name IS NULL OR m.name LIKE %:name%) " +
            "AND (:size IS NULL OR m.size = :size) " +
            "AND (:type IS NULL OR m.type = :type) " +
            "AND (:tag IS NULL OR m.tag = :tag) " +
            "AND (:alignment IS NULL OR m.alignment = :alignment) " +
            "AND (:minCr IS NULL OR m.cr >= :minCr) " +
            "AND (:maxCr IS NULL OR m.cr <= :maxCr) " +
            "AND (:legendary IS NULL OR m.legendary = :legendary) " +
            "AND (:lair IS NULL OR m.lair = :lair) " +
            "AND (:spellcaster IS NULL OR m.spellcaster = :spellcaster) " +
            "AND (:habitats IS NULL OR m.habitats IN :habitats) " +
            "AND (:tactics IS NULL OR m.tactics IN :tactics) " +
            "AND (:activities IS NULL OR m.activities IN :activities)")
    List<MonsterEntity> findMonstersByMultipleParameters(
            @Param("name") String name,
            @Param("size") Size size,
            @Param("type") Type type,
            @Param("tag") Tag tag,
            @Param("alignment") Alignment alignment,
            @Param("minCr") Float minCr,
            @Param("maxCr") Float maxCr,
            @Param("legendary") Boolean legendary,
            @Param("lair") Boolean lair,
            @Param("spellcaster") Boolean spellcaster,
            @Param("habitats") List<Habitat> habitats,
            @Param("tactics") List<Habitat> tactics,
            @Param("activities") List<Habitat> activities
    );

    List<MonsterEntity> findAllByType(Type type);
}
