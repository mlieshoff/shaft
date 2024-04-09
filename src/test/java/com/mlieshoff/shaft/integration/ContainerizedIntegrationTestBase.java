package com.mlieshoff.shaft.integration;

import lombok.extern.slf4j.Slf4j;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.TestPropertySource;
import org.testcontainers.containers.MariaDBContainer;
import org.testcontainers.containers.output.Slf4jLogConsumer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

import java.util.Collections;

@Slf4j
@Testcontainers
@TestPropertySource("classpath:application-test.properties")
public abstract class ContainerizedIntegrationTestBase {

  @Container
  protected static final MariaDBContainer<?> container =
      new MariaDBContainer<>(DockerImageName.parse("mariadb:latest"))
          .withUsername("user")
          .withPassword("password")
          .withDatabaseName("test")
          .withTmpFs(Collections.singletonMap("/tmpfs", "rw"))
          .withLogConsumer(new Slf4jLogConsumer(log))
          .withReuse(true);

  @BeforeAll
  public static void setUpContainer() {
    container.withReuse(true);
    container.start();
  }

  @DynamicPropertySource
  public static void overrideProperties(DynamicPropertyRegistry registry) {
    registry.add("spring.datasource.url", container::getJdbcUrl);
    registry.add("spring.datasource.username", container::getUsername);
    registry.add("spring.datasource.password", container::getPassword);
    registry.add("spring.datasource.driver-class-name", container::getDriverClassName);
  }

  @AfterAll
  public static void tearDownContainer() {
    container.stop();
  }
}
