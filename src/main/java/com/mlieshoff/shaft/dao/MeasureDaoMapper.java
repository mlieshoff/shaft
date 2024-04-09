package com.mlieshoff.shaft.dao;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
interface MeasureDaoMapper {

    MeasureDaoMapper INSTANCE = Mappers.getMapper(MeasureDaoMapper.class);

    LocationDaoDto locationEntityToLocationDaoDto(LocationEntity location);
}
