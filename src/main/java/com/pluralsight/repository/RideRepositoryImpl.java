package com.pluralsight.repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import com.pluralsight.model.Ride;

@Repository("rideRepository")
public class RideRepositoryImpl implements RideRepository {
	
	@Autowired
	private JdbcTemplate JdbcTemplate;

	@Override
	public List<Ride> getRides() {
		Ride ride = new Ride();
		ride.setName("Corner Canyon");
		ride.setDuration(120);
		List <Ride> rides = new ArrayList<>();
		rides.add(ride);
		return rides;
	}

	@Override
	public Ride createRide(Ride ride) {
		
		// JdbcTemplate.update("insert into ride(name, duration) values(?,?)",ride.getName(),ride.getDuration());
		SimpleJdbcInsert insert = new SimpleJdbcInsert(JdbcTemplate);
		List<String> columns = new ArrayList<>();
		columns.add("name"); columns.add("duration");
		insert.setTableName("ride"); insert.setColumnNames(columns);
		
		Map<String, Object> data = new HashMap<>();
		data.put("name", ride.getName());
		data.put("duration", ride.getDuration());
		
		insert.setGeneratedKeyName("id");
		
		Number key = insert.executeAndReturnKey(data); // Returns generated value from SQL server
		System.out.println(key);
		
		return ride;
	}
	
}