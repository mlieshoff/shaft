package com.mlieshoff.shaft.dao.login;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity(name = "login")
@NoArgsConstructor
@AllArgsConstructor
public class LoginEntity {

  @Id private Long id;

  private String token;

  private String jsonSettings;
}
