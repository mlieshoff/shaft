package com.mlieshoff.shaft.dao;

import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Data
@Table(name = "measure_integer")
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class LongMeasure implements Measure<Long> {

  @EmbeddedId private MeasureId measureId;

  @Column(name = "value")
  protected Long value;
}
