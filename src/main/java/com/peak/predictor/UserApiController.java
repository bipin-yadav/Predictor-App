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
	
}
