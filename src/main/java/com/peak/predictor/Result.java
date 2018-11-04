package com.peak.predictor;

import lombok.Data;

import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.io.Serializable;
import java.util.List;

@Entity
@Data
public class Result implements Serializable {
  private static final long serialVersionUID = -3009157732242241630L;

  @Id
  private String date;

  private String value;

  private String finalValue;

  @ElementCollection(targetClass=String.class)
  private List<String> names;

}
