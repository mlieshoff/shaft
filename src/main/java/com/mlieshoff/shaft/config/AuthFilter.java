package com.mlieshoff.shaft.config;

import com.mlieshoff.shaft.service.login.LoginService;
import com.mlieshoff.shaft.service.login.LoginServiceDto;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
public class AuthFilter implements Filter {

  private final LoginService loginService;

  @Override
  public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
      throws ServletException, IOException {
    HttpServletRequest req = (HttpServletRequest) request;
    String token = req.getHeader("x-auth-secret-key");
    log.info("Starting a auth for req : {} {}", req.getRequestURI(), token);
    Optional<LoginServiceDto> optionalLoginServiceDto = loginService.login(token);
      optionalLoginServiceDto.ifPresent(loginServiceDto -> log.info("Success : {}", loginServiceDto));
    chain.doFilter(request, response);
    log.info("Stopping a auth for req : {}", req.getRequestURI());
  }
}
