package com.peak.predictor;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.*;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class UserApiControllerTest {

  @InjectMocks
  UserApiController userApiController;

  @Test
  public void testCreateResult(){
    FixedPrediction fixedPrediction = new FixedPrediction();
    fixedPrediction.setDate("17");
    fixedPrediction.setFinalValue("50");

    List<UserPrediction> userPredictions = new ArrayList<>();
    for(int i = 0; i< 13; i++) {
      UserPrediction userPrediction = new UserPrediction();
      userPrediction.setAttempt(i);
      userPrediction.setDate("17");
      userPrediction.setUsername("username"+i);
      userPrediction.setValue(String.valueOf(30 + (int)(Math.random() * ((70 - 30) + 1))));
      userPredictions.add(userPrediction);
    }


    List<UserPrediction> winnersList = userApiController.getWinnersList(userPredictions, fixedPrediction);
  }

}