package com.mlieshoff.shaft.dao;

import static org.assertj.core.api.Assertions.assertThat;

import com.mlieshoff.shaft.integration.dbunit.DatabaseExpectation;
import com.mlieshoff.shaft.integration.dbunit.DatabaseIntegrationTestBase;
import com.mlieshoff.shaft.integration.dbunit.DatabaseSetUp;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

@Slf4j
class MeasureDaoImplDatabaseIntegrationTest extends DatabaseIntegrationTestBase {

  @Autowired private MeasureDao unitUnderTest;

  @Test
  @DatabaseSetUp
  @DatabaseExpectation
  void update() {

    LocationDaoDto locationDaoDto = unitUnderTest.createLocation("database1", "bucket1");

    DecimalMeasure actual =
        unitUnderTest.update(locationDaoDto, "Company1", TrackerTypes.WASTE_QUANTITY, 42.0);

    assertThat(actual).isNull();
  }
}
