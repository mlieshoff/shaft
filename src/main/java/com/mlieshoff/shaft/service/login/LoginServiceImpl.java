package com.mlieshoff.shaft.service.login;

import com.mlieshoff.shaft.dao.login.LoginDao;
import com.mlieshoff.shaft.dao.login.LoginRequestDaoDto;
import com.mlieshoff.shaft.dao.login.LoginResultDaoDto;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class LoginServiceImpl implements LoginService {

  private final LoginDao loginDao;

  @Override
  public Optional<LoginServiceDto> login(String token) {
    Optional<LoginResultDaoDto> optionalLoginResultDaoDto =
        loginDao.login(new LoginRequestDaoDto(token));
    return optionalLoginResultDaoDto.map(
        LoginServiceMapper.INSTANCE::loginResultDaoDtoToLoginServiceDto);
  }
}
