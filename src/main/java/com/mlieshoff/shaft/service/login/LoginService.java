package com.mlieshoff.shaft.service.login;

import java.util.Optional;

public interface LoginService {

    Optional<LoginServiceDto> login(String token);

}
