package com.peak.predictor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.stream.Collectors;

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
		UserId userId = new UserId();
		userId.setDate(userPrediction.getDate());
		userId.setUsername(userPrediction.getUsername());
		Optional<UserPrediction> byId = userPredictionRepository.findById(userId);
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
	public @ResponseBody ResponseEntity getUserPredictionByDate(@RequestParam(value = "username") String username,
																															@RequestParam(value = "date") String date) {
		UserId userId = new UserId();
		userId.setDate(date);
		userId.setUsername(username);
		Optional<UserPrediction> userOptional = userPredictionRepository.findById(userId);
		if (userOptional.isPresent())
			return new ResponseEntity(userOptional.get(), HttpStatus.OK);
		else
			return new ResponseEntity("{\"result\":\"NOT_FOUND\"}", HttpStatus.NOT_FOUND);
	}

	@GetMapping(path="/api/user/predictions/all")
	public @ResponseBody
	Iterable<UserPrediction> getAllUserPredictions(@RequestParam(value = "date", required = false) String date) {
		if (StringUtils.isEmpty(date)) {
			return userPredictionRepository.findAll();
		} else {
			Iterable<UserPrediction> all = userPredictionRepository.findAll();
			List<UserPrediction> userPredictionList =  new ArrayList<>();
			Iterator<UserPrediction> iterator = all.iterator();
			while (iterator.hasNext()) {
				UserPrediction userPrediction = iterator.next();
				if(!date.equals(userPrediction.getDate())) {
					continue;
				} else {
					userPredictionList.add(userPrediction);
				}
			}
			return userPredictionList;
		}
	}

	@Autowired
	private ResultRepository resultRepository;

	@GetMapping(path="/api/user/predictions/result")
	public @ResponseBody Iterable<Result> getResult() {
		return resultRepository.findAll();
	}

	@RequestMapping(value = "/api/user/predictions/result", method = RequestMethod.POST ,produces="application/json" )
	public @ResponseBody ResponseEntity createResult(@RequestParam(value = "date", required = true) String date,
																									 @RequestParam(value = "doAgain", required = false) boolean doAgain) {
		Optional<Result> optionalResult = resultRepository.findById(date);
		if(optionalResult.isPresent() && doAgain) {
			return new ResponseEntity(HttpStatus.CONFLICT);
		}
		Optional<FixedPrediction> byId = fixedPredictionRepository.findById(date);
		if(byId.isPresent()) {
			FixedPrediction fixedPrediction = byId.get();
			//all predictions.
			List<UserPrediction> userPredictionRepositoryAll
					= (List<UserPrediction>) userPredictionRepository.findAll();
			//get for particular date.
			List<UserPrediction> userPredictions = userPredictionRepositoryAll.stream()
					.filter(userPrediction -> userPrediction.getDate().equals(date))
					.collect(Collectors.toList());
			// get winners.
			List<UserPrediction> winners = getWinnersList(userPredictions, fixedPrediction);
			//create details.
			List<String> names = findListOfWinners(winners);

			Result result = new Result();
			result.setDate(fixedPrediction.getDate());
			result.setValue(fixedPrediction.getValue());
			result.setFinalValue(fixedPrediction.getFinalValue());
			result.setNames(names);
			resultRepository.save(result);
			return new ResponseEntity(HttpStatus.CREATED);
		}
		return new ResponseEntity(HttpStatus.BAD_REQUEST);
	}

	public List<UserPrediction> getWinnersList(List<UserPrediction> userPredictions,
																							FixedPrediction fixedPrediction) {
		List<UserPrediction> winners = new ArrayList<>();
		BigDecimal finalValue = new BigDecimal(fixedPrediction.getFinalValue());
		SortedMap<String, BigDecimal> stringBigDecimalMap = new TreeMap<>();
		for (UserPrediction userPrediction: userPredictions) {
			BigDecimal userValue = new BigDecimal(userPrediction.getValue());
			BigDecimal difference = finalValue.subtract(userValue).abs() ;
			stringBigDecimalMap.put(userPrediction.getUsername(), difference);
		}

		Set<Map.Entry<String, BigDecimal>> set = stringBigDecimalMap.entrySet();
		List<Map.Entry<String, BigDecimal>> list = new ArrayList<Map.Entry<String, BigDecimal>>(set);
		Collections.sort(list, new Comparator<Map.Entry<String, BigDecimal>>() {
			public int compare(Map.Entry<String, BigDecimal> o1,
												 Map.Entry<String, BigDecimal> o2) {
				return o2.getValue().compareTo(o1.getValue());
			}
		});

		for (Map.Entry<String, BigDecimal> entry : list) {
			System.out.println(entry.getKey()  + "  " + entry.getValue());
		}

		if(list.size() <= 3) {
			return userPredictions;
		} else {
			for(int i=list.size()-1; i > list.size()-4; i--) {
				String key = list.get(i).getKey();
				UserPrediction userPredictionObjectByUsername = getUserPredictionObjectByUsername(userPredictions, key);
				winners.add(userPredictionObjectByUsername);
			}
		}

		return winners;
	}

	private UserPrediction getUserPredictionObjectByUsername(List<UserPrediction> userPredictions, String key) {
		for (UserPrediction userPrediction : userPredictions) {
			if (key.equals(userPrediction.getUsername())) {
				return userPrediction;
			}
		}
		return null;
	}

	private List<String> findListOfWinners(List<UserPrediction> userPredictions) {
		List<String> stringList = new ArrayList<>();
		for (UserPrediction userPrediction : userPredictions) {
			String username = userPrediction.getUsername();
			Optional<User> byId = userRepository.findById(username);
			if(byId.isPresent()) {
				User user = byId.get();
				stringList.add("Name:" + user.getName() + ", " + "UserName:" + user.getUsername()
						+ ", " + "Value:" + userPrediction.getValue() + ", " + "Attempts:" + userPrediction.getAttempt());
			}
		}

		return stringList;
	}

}
