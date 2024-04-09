package com.mlieshoff.shaft.dao;


public interface Measure<T> {

  MeasureId getMeasureId();

  void setMeasureId(MeasureId measureId);

  T getValue();

  void setValue(T value);
}
