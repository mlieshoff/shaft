package com.mlieshoff.shaft.dao;

import java.util.List;
import java.util.Optional;

public interface MeasureDao {

    LocationDaoDto createLocation(String database, String bucket);

    <I extends Comparable<? super I>, V extends Comparable<? super V>, M extends Measure<V>> M update(
      LocationDaoDto locationDaoDto, I id, MeasureType measureType, V value);

  <I extends Comparable<? super I>, V extends Comparable<? super V>, M extends Measure<V>>
      Optional<M> findCurrent(LocationEntity locationEntity, MeasureType measureType, I id);

  <I extends Comparable<? super I>, V extends Comparable<? super V>, M extends Measure<V>>
      List<M> getLastX(LocationEntity locationEntity, MeasureType measureType, I id, int limit);

  <V extends Comparable<? super V>, M extends Measure<V>> Long count(
          LocationEntity locationEntity, MeasureType measureType);
}
