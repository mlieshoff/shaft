package com.mlieshoff.shaft.service;

import com.mlieshoff.shaft.dao.LocationDaoDto;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
interface LocationServiceMapper {

  LocationServiceMapper INSTANCE = Mappers.getMapper(LocationServiceMapper.class);

  LocationServiceDto locationDaoDtoToLocationServiceDto(LocationDaoDto locationDaoDto);
}
