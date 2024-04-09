package com.mlieshoff.shaft.dao;

public interface MeasureType {

  int getCode();

  <T extends Comparable<T>> Class<T> getValueClass();
}
