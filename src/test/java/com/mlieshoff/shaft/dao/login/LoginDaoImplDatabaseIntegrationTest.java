package com.mlieshoff.shaft.dao.login;

import static org.assertj.core.api.Assertions.assertThat;

import com.mlieshoff.shaft.integration.dbunit.DatabaseExpectation;
import com.mlieshoff.shaft.integration.dbunit.DatabaseIntegrationTestBase;
import com.mlieshoff.shaft.integration.dbunit.DatabaseSetUp;

import lombok.extern.slf4j.Slf4j;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Slf4j
class LoginDaoImplDatabaseIntegrationTest extends DatabaseIntegrationTestBase {

  @Autowired private LoginDaoImpl unitUnderTest;

  @Test
  @DatabaseSetUp
  @DatabaseExpectation
  void login() {
    SettingsDaoDto settingsDaoDto = new SettingsDaoDto(Map.of("database1", List.of("bucket1", "bucket2")));

    Optional<LoginResultDaoDto> expected = Optional.of(new LoginResultDaoDto(settingsDaoDto));

    Optional<LoginResultDaoDto> actual = unitUnderTest.login(new LoginRequestDaoDto("token1"));

    assertThat(actual).usingRecursiveComparison().isEqualTo(expected);
  }
}
