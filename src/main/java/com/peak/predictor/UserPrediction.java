
package com.peak.predictor;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.io.Serializable;

@Entity
@Data
public class UserPrediction implements Serializable {
  private static final long serialVersionUID = -3009157732242241613L;

  @Id
  private long userName;

  private String date;

  private String value;

  private int attempt;
}
