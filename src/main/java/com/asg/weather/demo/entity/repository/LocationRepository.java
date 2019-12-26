package com.asg.weather.demo.entity.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.asg.weather.demo.entity.Location;

public interface LocationRepository extends JpaRepository<Location, Long> {

	Location findByLatitudeAndLongitude(Double latitude, Double longitude);
	
	Location findByCityAndState(String city, String state);
	
}
