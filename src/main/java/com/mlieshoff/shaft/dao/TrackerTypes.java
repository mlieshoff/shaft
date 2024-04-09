package com.mlieshoff.shaft.dao;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum TrackerTypes implements MeasureType {
  ID(-1, String.class),
  WASTE_QUANTITY(2, Double.class),
  ;

  private final int code;

  private final Class<? extends Comparable<?>> valueClass;
}
