package com.mlieshoff.shaft.dao.login;


import java.util.List;
import java.util.Optional;

public interface LoginDao {

    Optional<LoginResultDaoDto> login(LoginRequestDaoDto loginRequestDaoDto);
}
