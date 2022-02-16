package com.pluralsight.controller;

import java.util.List;

import org.pluralsight.util.ServiceError;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.pluralsight.model.Ride;
import com.pluralsight.service.RideService;

@Controller
public class RideController {
	
	@Autowired
	private RideService rideService;
	
	@RequestMapping(value = "/rides", method = RequestMethod.GET)
	public @ResponseBody List<Ride> getRides() {
		return rideService.getRides();
	}
	
	@RequestMapping(value = "/ride/{id}", method = RequestMethod.GET)
	public @ResponseBody Ride getRide(@PathVariable(name = "id") int id) {
		return rideService.getRide(id);
	}
	
	@RequestMapping(value = "/rides", method = RequestMethod.PUT)
	public @ResponseBody Ride updateRide(@RequestBody Ride ride) {
		return rideService.updateRide(ride);
	}
	
	@RequestMapping(value = "/batch", method = RequestMethod.GET)
	public @ResponseBody Object batchUpdateRide() {
		rideService.batch();
		return null;
	}
	
	@RequestMapping(value = "/delete/{id}", method = RequestMethod.DELETE)
	public @ResponseBody Object deleteRide(@PathVariable(name = "id") int id) {
		rideService.deleteRide(id);
		return null;
	}
	
	@RequestMapping(value = "/test", method = RequestMethod.GET)
	public @ResponseBody Object test() {
		throw new DataAccessException("Testing exception thrown") {
		};
	}
	
	@RequestMapping(value = "/rides", method = RequestMethod.POST)
	public @ResponseBody Ride putRides(@RequestBody Ride ride) {
		return rideService.createRide(ride);
	}
	
	@ExceptionHandler(value = RuntimeException.class)
	public ResponseEntity<ServiceError> handle(RuntimeException re){
		ServiceError serviceError = new ServiceError(HttpStatus.OK.value(), re.getMessage());
		return new ResponseEntity<ServiceError>(serviceError, HttpStatus.OK);
	}
}