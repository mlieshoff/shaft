package com.mlieshoff.shaft.service.login;

import com.mlieshoff.shaft.dao.login.LoginResultDaoDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
interface LoginServiceMapper {

    LoginServiceMapper INSTANCE = Mappers.getMapper(LoginServiceMapper.class);

    @Mapping(source = "settingsDaoDto", target = "settingsServiceDto")
    LoginServiceDto loginResultDaoDtoToLoginServiceDto(LoginResultDaoDto loginResultDaoDto);
}
