package com.mlieshoff.shaft.dao.login;

import java.util.List;
import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SettingsDaoDto {

  private Map<String, List<String>> allowedDatabasesAndBuckets;
}
