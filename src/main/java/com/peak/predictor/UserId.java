package com.peak.predictor;

import lombok.Data;

import javax.persistence.Embeddable;
import javax.persistence.Id;
import java.io.Serializable;

@Embeddable
@Data
public class UserId implements Serializable {
  private static final long serialVersionUID = -3009157732562241613L;

  private String username;

  private String date;
}
