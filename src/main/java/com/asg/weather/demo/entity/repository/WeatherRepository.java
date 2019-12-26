package com.asg.weather.demo.entity.repository;

import java.util.Date;

import org.springframework.data.jpa.repository.JpaRepository;

import com.asg.weather.demo.entity.Weather;

public interface WeatherRepository extends JpaRepository<Weather, Long> {
	
	Weather findByLocationIdAndDateBetween(Long id, Date startDate, Date endDate);
	Weather findByLocationIdAndDate(Long id, Date date);

}
