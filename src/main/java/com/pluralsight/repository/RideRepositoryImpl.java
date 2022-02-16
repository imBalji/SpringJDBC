package com.pluralsight.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.pluralsight.util.RideRowMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;

import com.pluralsight.model.Ride;

@Repository("rideRepository")
public class RideRepositoryImpl implements RideRepository {
	
	@Autowired
	private JdbcTemplate JdbcTemplate;

	@Override
	public List<Ride> getRides() {
		List<Ride> rides = JdbcTemplate.query("SELECT * FROM ride", new RideRowMapper());
		return rides;
	}

	@Override
	public Ride createRide(Ride ride) {
		
		// a simpler way
		// JdbcTemplate.update("insert into ride(name, duration) values(?,?)",ride.getName(),ride.getDuration());
		
		// hard way
		// SimpleJdbcInsert insert = new SimpleJdbcInsert(JdbcTemplate);
		// List<String> columns = new ArrayList<>();
		// columns.add("name"); columns.add("duration");
		// insert.setTableName("ride"); insert.setColumnNames(columns);
		//
		// Map<String, Object> data = new HashMap<>();
		// data.put("name", ride.getName());
		// data.put("duration", ride.getDuration());
		//
		// insert.setGeneratedKeyName("id");
		//
		// Number key = insert.executeAndReturnKey(data); // Returns generated value from SQL server
		// System.out.println(key);
		// return ride;
		
		// using keyHolder
		GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
		JdbcTemplate.update(new PreparedStatementCreator() {
			
			@Override
			public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
				PreparedStatement ps = con.prepareStatement("INSERT INTO ride(name, duration) VALUES(?,?)", new String[] {"id"});
				ps.setString(1, ride.getName());
				ps.setInt(2, ride.getDuration());
				return ps;
			}
		}, keyHolder);
		
		Number key = keyHolder.getKey();
		
		return getRide(key.intValue());
	}

	@Override
	public Ride getRide(int id) {
		Ride ride = JdbcTemplate.queryForObject("SELECT * FROM ride WHERE id = ?", new RideRowMapper(), id);
		return ride;
	}

	@Override
	public Ride updateRide(Ride ride) {
		JdbcTemplate.update("UPDATE ride SET name = ?, duration = ? WHERE id = ?", ride.getName(), ride.getDuration(), ride.getId());
		return ride;
	}

	@Override
	public void updateRides(List<Object[]> pairs) {
		JdbcTemplate.batchUpdate("UPDATE ride SET date = ? WHERE id = ?", pairs);
	}

	@Override
	public void deleteRide(int id) {
		// the easy way
		// JdbcTemplate.update("DELETE FROM ride WHERE id = ?",id);
		
		// With Named Parameter
		NamedParameterJdbcTemplate namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(JdbcTemplate);
		
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("id", id);
		
		namedParameterJdbcTemplate.update("DELETE FROM ride WHERE id = :id", paramMap);
	}
	
}