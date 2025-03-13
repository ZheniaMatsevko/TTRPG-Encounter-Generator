package com.example.backend.mapper;

import com.example.backend.dto.MonsterDto;
import com.example.backend.entity.MonsterEntity;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface IMonsterMapper {
    IMonsterMapper INSTANCE = Mappers.getMapper(IMonsterMapper.class);
    MonsterDto entityToDto(MonsterEntity eventEntity);
    MonsterEntity dtoToEntity(MonsterDto eventDto);
}
