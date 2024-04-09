package com.mlieshoff.shaft.dao;

import jakarta.persistence.Embeddable;
import java.io.Serializable;
import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Embeddable
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MeasureId implements Serializable {

  private long databaseHash;

  private long bucketHash;

  private long hash;

  private Date modifiedAt;

  private int type;
}
