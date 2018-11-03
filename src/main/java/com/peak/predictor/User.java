package com.peak.predictor;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.io.Serializable;

@Data
@Entity
public class User implements Serializable {
  private static final long serialVersionUID = -3009157732242241616L;

  String name;

  @Id
  String username;

  String password;

}
