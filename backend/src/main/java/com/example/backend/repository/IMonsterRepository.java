package com.example.backend.repository;

import com.example.backend.entity.MonsterEntity;
import com.example.backend.entity.enums.Habitat;
import com.example.backend.entity.enums.Size;
import com.example.backend.entity.enums.Type;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IMonsterRepository extends JpaRepository<MonsterEntity, Long> {
    @Query("SELECT m FROM MonsterEntity m WHERE m.size = :size ORDER BY RANDOM()")
    MonsterEntity findRandomMonsterBySize(@Param("size") Size size);

    @Query("SELECT m FROM MonsterEntity m ORDER BY RANDOM()")
    MonsterEntity findRandomMonster();

    @Query("SELECT m FROM MonsterEntity m WHERE :habitat MEMBER OF m.habitats")
    List<MonsterEntity> findAllByHabitat(Habitat habitat);

    List<MonsterEntity> findAllByType(Type type);
}
