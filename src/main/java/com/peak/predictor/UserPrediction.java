
package com.peak.predictor;

import lombok.Data;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import java.io.Serializable;

@Entity
@Data
@IdClass(UserId.class)
public class UserPrediction implements Serializable {
  private static final long serialVersionUID = -3009157732242241613L;

  @Id
  private String username;;

  @Id
  private String date;

  private String value;

  private int attempt;
}
