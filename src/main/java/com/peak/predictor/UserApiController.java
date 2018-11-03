package com.peak.predictor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@Slf4j
public class UserApiController {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private FixedPredictionRepository fixedPredictionRepository;

	@Autowired
	private UserPredictionRepository userPredictionRepository;

	@RequestMapping(value = "/api/users", method = RequestMethod.POST ,produces="application/json" )
	public @ResponseBody ResponseEntity registerUser(@RequestBody User user)   {
		boolean exists = userRepository.existsById(user.username);
		if(exists) {
			return new ResponseEntity("Bad Request", HttpStatus.BAD_REQUEST);
		}
		User save = userRepository.save(user);
		if(save != null) {
			return new ResponseEntity("{\"success\":\"true\"}", HttpStatus.OK);
		} else {
			return new ResponseEntity("Bad Request", HttpStatus.BAD_REQUEST);
		}
	}

	@RequestMapping(value = "/api/users", method = RequestMethod.GET ,produces="application/json" )
	public @ResponseBody ResponseEntity getUserByUsername(@RequestParam(value = "username") String username)   {
		Optional<User> userOptional = userRepository.findById(username);
		if (userOptional.isPresent())
			return new ResponseEntity(userOptional.get(), HttpStatus.OK);
		else
		return new ResponseEntity("Not Found", HttpStatus.NOT_FOUND);
	}

	@GetMapping(path="/api/users/all")
	public @ResponseBody Iterable<User> getAllUsers() {
		return userRepository.findAll();
	}



	@RequestMapping(value = "/api/fixed/predictions", method = RequestMethod.POST ,produces="application/json" )
	public @ResponseBody ResponseEntity createFixedPrediction(@RequestBody FixedPrediction fixedPrediction) {
		FixedPrediction save = fixedPredictionRepository.save(fixedPrediction);
		if(save != null) {
			return new ResponseEntity("{\"success\":\"true\"}", HttpStatus.OK);
		} else {
			return new ResponseEntity(HttpStatus.BAD_REQUEST);
		}
	}

	@RequestMapping(value = "/api/fixed/predictions", method = RequestMethod.GET ,produces="application/json" )
	public @ResponseBody ResponseEntity getFixedPredictionByDate(@RequestParam(value = "date") String date) {
		Optional<FixedPrediction> userOptional = fixedPredictionRepository.findById(date);
		if (userOptional.isPresent())
			return new ResponseEntity(userOptional.get(), HttpStatus.OK);
		else
			return new ResponseEntity("{\"result\":\"NOT_FOUND\"}", HttpStatus.NOT_FOUND);
	}

	@GetMapping(path="/api/fixed/predictions/all")
	public @ResponseBody
	Iterable<FixedPrediction> getAllFixedPredictions() {
		return fixedPredictionRepository.findAll();
	}



	@RequestMapping(value = "/api/user/predictions", method = RequestMethod.POST ,produces="application/json" )
	public @ResponseBody ResponseEntity createUserPrediction(@RequestBody UserPrediction userPrediction) {
		log.info(userPrediction.toString());
		Optional<UserPrediction> byId = userPredictionRepository.findById(userPrediction.getUsername());
		if(byId.isPresent()) {
			UserPrediction oldPrediction = byId.get();
			if (oldPrediction.getAttempt() >= 3) {
				return new ResponseEntity(HttpStatus.BAD_REQUEST);
			}
			userPrediction.setAttempt(oldPrediction.getAttempt() + 1);
		} else {
			userPrediction.setAttempt(1);
		}

				UserPrediction save = userPredictionRepository.save(userPrediction);
				if (save != null) {
					return new ResponseEntity("{\"success\":\"true\"}", HttpStatus.OK);
				} else {
					return new ResponseEntity(HttpStatus.BAD_REQUEST);
				}

	}

	@RequestMapping(value = "/api/user/predictions", method = RequestMethod.GET ,produces="application/json" )
	public @ResponseBody ResponseEntity getUserPredictionByDate(@RequestParam(value = "username") String username) {
		Optional<UserPrediction> userOptional = userPredictionRepository.findById(username);
		if (userOptional.isPresent())
			return new ResponseEntity(userOptional.get(), HttpStatus.OK);
		else
			return new ResponseEntity("{\"result\":\"NOT_FOUND\"}", HttpStatus.NOT_FOUND);
	}

	@GetMapping(path="/api/user/predictions/all")
	public @ResponseBody
	Iterable<UserPrediction> getAllUserPredictions() {
		return userPredictionRepository.findAll();
	}
	
}
