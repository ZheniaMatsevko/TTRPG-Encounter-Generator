package com.example.backend.repository;

import com.example.backend.entity.FilterEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IFilterRepository extends JpaRepository<FilterEntity, Long> {
}
