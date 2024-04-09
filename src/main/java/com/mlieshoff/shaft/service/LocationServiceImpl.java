package com.mlieshoff.shaft.service;

import com.mlieshoff.shaft.dao.LocationDaoDto;
import com.mlieshoff.shaft.dao.MeasureDao;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
class LocationServiceImpl implements LocationService {

  private final MeasureDao measureDao;

  @Override
  public LocationServiceDto create(String database, String bucket) {
    LocationDaoDto locationDaoDto = measureDao.createLocation(database, bucket);
    return LocationServiceMapper.INSTANCE.locationDaoDtoToLocationServiceDto(locationDaoDto);
  }
}
