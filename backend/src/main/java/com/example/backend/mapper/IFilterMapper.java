package com.example.backend.mapper;

import com.example.backend.dto.FilterDto;
import com.example.backend.entity.FilterEntity;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface IFilterMapper {
    IFilterMapper INSTANCE = Mappers.getMapper(IFilterMapper.class);
    FilterDto entityToDto(FilterEntity eventEntity);
    FilterEntity dtoToEntity(FilterDto eventDto);
}
