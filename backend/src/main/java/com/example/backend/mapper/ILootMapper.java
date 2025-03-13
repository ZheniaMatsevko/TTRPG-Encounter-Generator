package com.example.backend.mapper;

import com.example.backend.dto.LootDto;
import com.example.backend.entity.LootEntity;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface ILootMapper {
    ILootMapper INSTANCE = Mappers.getMapper(ILootMapper.class);
    LootDto entityToDto(LootEntity eventEntity);
    LootEntity dtoToEntity(LootDto eventDto);
}
