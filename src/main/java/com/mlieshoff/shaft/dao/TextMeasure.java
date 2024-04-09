package com.mlieshoff.shaft.dao;

import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Table(name = "measure_text")
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class TextMeasure implements Measure<String> {

  @EmbeddedId protected MeasureId measureId;

  @Column(name = "value")
  private String value;
}
