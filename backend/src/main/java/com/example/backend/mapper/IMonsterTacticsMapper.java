package com.example.backend.mapper;

import com.example.backend.dto.MonsterTacticsDto;
import com.example.backend.entity.MonsterTacticsEntity;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface IMonsterTacticsMapper {
    IMonsterTacticsMapper INSTANCE = Mappers.getMapper(IMonsterTacticsMapper.class);
    MonsterTacticsDto entityToDto(MonsterTacticsEntity eventEntity);
    MonsterTacticsEntity dtoToEntity(MonsterTacticsDto eventDto);
}
