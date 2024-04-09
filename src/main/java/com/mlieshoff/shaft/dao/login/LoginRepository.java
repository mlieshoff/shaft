package com.mlieshoff.shaft.dao.login;

import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

interface LoginRepository extends CrudRepository<LoginEntity, Long> {

  Optional<LoginEntity> findByToken(String token);
}
