package com.example.backend.mapper;

import com.example.backend.dto.MonsterActivitiesDto;
import com.example.backend.entity.MonsterActivitiesEntity;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface IMonsterActivitiesMapper {
    IMonsterActivitiesMapper INSTANCE = Mappers.getMapper(IMonsterActivitiesMapper.class);
    MonsterActivitiesDto entityToDto(MonsterActivitiesEntity eventEntity);
    MonsterActivitiesEntity dtoToEntity(MonsterActivitiesDto eventDto);
}
