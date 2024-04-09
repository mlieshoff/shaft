package com.mlieshoff.shaft.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import com.mlieshoff.shaft.dao.login.LoginDao;
import com.mlieshoff.shaft.dao.login.LoginRequestDaoDto;
import com.mlieshoff.shaft.dao.login.LoginResultDaoDto;
import com.mlieshoff.shaft.dao.login.SettingsDaoDto;
import com.mlieshoff.shaft.service.login.LoginServiceDto;
import com.mlieshoff.shaft.service.login.LoginServiceImpl;
import com.mlieshoff.shaft.service.login.SettingsServiceDto;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
class LoginServiceImplTest {

  @InjectMocks private LoginServiceImpl unitUnderTest;

  @Mock private LoginDao loginDao;

  @Test
  void login_whenCalled_thenReturnReult() {
    Map<String, List<String>> settingInfo = Map.of("database1", List.of("bucket1", "bucket2"));
    Optional<LoginServiceDto> expected =
        Optional.of(new LoginServiceDto(new SettingsServiceDto(settingInfo)));
    when(loginDao.login(new LoginRequestDaoDto("token")))
        .thenReturn(Optional.of(new LoginResultDaoDto(new SettingsDaoDto(settingInfo))));

    Optional<LoginServiceDto> actual = unitUnderTest.login("token");

    assertThat(actual).usingRecursiveComparison().isEqualTo(expected);
  }
}
