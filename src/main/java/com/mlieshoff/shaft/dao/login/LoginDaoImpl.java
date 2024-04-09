package com.mlieshoff.shaft.dao.login;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
class LoginDaoImpl implements LoginDao {

  private static final Gson GSON = new GsonBuilder().create();

  private final LoginRepository loginRepository;

  @Override
  public Optional<LoginResultDaoDto> login(LoginRequestDaoDto loginRequestDaoDto) {
    String token = loginRequestDaoDto.token();
    Optional<LoginEntity> optionalLoginEntity = loginRepository.findByToken(token);
    System.out.println("-- " + GSON.toJson(optionalLoginEntity.get().getJsonSettings()));
    return optionalLoginEntity.map(
        loginEntity ->
            new LoginResultDaoDto(GSON.fromJson(loginEntity.getJsonSettings(), SettingsDaoDto.class)));
  }
}
