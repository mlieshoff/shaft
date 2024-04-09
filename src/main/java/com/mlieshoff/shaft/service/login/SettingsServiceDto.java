package com.mlieshoff.shaft.service.login;

import java.util.List;
import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SettingsServiceDto {

  private Map<String, List<String>> allowedDatabasesAndBuckets;
}
