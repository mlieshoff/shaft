package com.mlieshoff.shaft.config;

import com.mlieshoff.shaft.service.auth.AuthService;
import com.mlieshoff.shaft.service.login.LoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import lombok.RequiredArgsConstructor;

@EnableWebMvc
@Configuration
@ComponentScan("com.mlieshoff.shaft")
@RequiredArgsConstructor
@EnableAutoConfiguration
public class ApplicationConfiguration {

  private final LoginService loginService;

  private final AuthService authService;

  @Bean
  public FilterRegistrationBean<AuthFilter> authFilter() {
    FilterRegistrationBean<AuthFilter> registrationBean = new FilterRegistrationBean<>();
    registrationBean.setFilter(new AuthFilter(loginService, authService));
    registrationBean.addUrlPatterns("/api/*");
    registrationBean.setOrder(1);
    return registrationBean;
  }
}
