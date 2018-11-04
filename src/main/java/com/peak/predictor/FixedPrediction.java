package com.peak.predictor;

import lombok.Data;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.io.Serializable;

@Entity
@Data
public class FixedPrediction implements Serializable {
  private static final long serialVersionUID = -3009157732242241634L;

  @Id
  private String date;

  private String value;

  private String finalValue;
}
