package com.mlieshoff.shaft.config;

import com.mlieshoff.shaft.service.auth.AuthService;
import com.mlieshoff.shaft.service.auth.Authorizated;
import com.mlieshoff.shaft.service.auth.AuthorizationDto;
import com.mlieshoff.shaft.service.login.LoginService;
import com.mlieshoff.shaft.service.login.LoginServiceDto;

import java.io.IOException;
import java.util.Optional;
import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
public class AuthFilter implements Filter {

  private final LoginService loginService;

  private final AuthService authService;

  @Override
  public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
      throws ServletException, IOException {
    long ms = -System.currentTimeMillis();
    HttpServletRequest req = (HttpServletRequest) request;
    String token = req.getHeader("x-auth-secret-key");
    log.info("Starting auth sequence: {} {}", req.getRequestURI(), "xxx");
    Optional<LoginServiceDto> optionalLoginServiceDto = loginService.login(token);
    if (optionalLoginServiceDto.isPresent()) {
      LoginServiceDto loginServiceDto = optionalLoginServiceDto.get();
      authService.doAuthorized(new AuthorizationDto(), new Authorizated() {
        @Override
        public void execute(AuthorizationDto authorizationDto) throws Exception {
          chain.doFilter(request, response);
        }
      });
    } else {
      throw new IllegalStateException("wrong login!");
    }
    log.info("Stopping auth sequence: {}ms {}", (ms + System.currentTimeMillis()), req.getRequestURI());
  }
}
