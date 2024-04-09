package com.mlieshoff.shaft.rest;

import com.mlieshoff.shaft.service.LocationServiceDto;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
interface LocationRestMapper {

    LocationRestMapper INSTANCE = Mappers.getMapper(LocationRestMapper.class);

    LocationRestDto locationServiceDtoToLocationRestDto(LocationServiceDto locationServiceDto);
}
