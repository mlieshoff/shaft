package com.mlieshoff.shaft.dao.login;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class LoginDaoImplTest {

  private static final Gson GSON = new GsonBuilder().create();

  @InjectMocks private LoginDaoImpl unitUnderTest;

  @Mock private LoginRepository loginRepository;

  @Test
  void login_whenCalled_thenReturnResults() {
    SettingsDaoDto settingsDaoDto = new SettingsDaoDto(Map.of("database1", List.of("bucket1", "bucket2")));
    Optional<LoginResultDaoDto> expected = Optional.of(new LoginResultDaoDto(settingsDaoDto));
    LoginEntity loginEntity = new LoginEntity(4711L, "token", GSON.toJson(settingsDaoDto));
    when(loginRepository.findByToken("token")).thenReturn(Optional.of(loginEntity));

    Optional<LoginResultDaoDto> actual = unitUnderTest.login(new LoginRequestDaoDto("token"));

    assertThat(actual).usingRecursiveComparison().isEqualTo(expected);
  }
}
