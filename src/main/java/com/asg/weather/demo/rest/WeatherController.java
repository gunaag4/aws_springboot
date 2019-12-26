package com.asg.weather.demo.rest;

import java.text.ParseException;
import java.text.SimpleDateFormat;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.asg.weather.demo.entity.Location;
import com.asg.weather.demo.entity.Weather;
import com.asg.weather.demo.entity.repository.LocationRepository;
import com.asg.weather.demo.entity.repository.WeatherRepository;
import com.asg.weather.demo.models.WeatherDto;

@RestController
public class WeatherController {

	@Autowired
	private LocationRepository locationService;
	
	@Autowired
	private WeatherRepository weatherRepository;
	
	private SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy HH:mm");
	
	@PostMapping("/weather")
	public Weather postWeather(@RequestBody WeatherDto weatherDto) throws ParseException
	{
		
		Location location = locationService.findByLatitudeAndLongitude(weatherDto.getLatitude(), weatherDto.getLongitude());
		if(location == null)
		{
			location = new Location();
			location.setCity(weatherDto.getCity());
			location.setState(weatherDto.getState());
			location.setLatitude(weatherDto.getLatitude());
			location.setLongitude(weatherDto.getLongitude());
			location = locationService.save(location);			
		}
		
		Weather weather = weatherRepository.findByLocationIdAndDate(location.getId(), dateFormat.parse(weatherDto.getDate()));
		
		
		if(weather != null)
		{
			weather.setTemperature(weatherDto.getTemperature());
			weather = weatherRepository.save(weather);
		}
		else
		{
			weather = new Weather();
			weather.setDate(dateFormat.parse(weatherDto.getDate()));
			weather.setTemperature(weatherDto.getTemperature());
			weather.setLocation(location);
			weather = weatherRepository.save(weather);
		}
		
		return weather;
		
	}
	
}
