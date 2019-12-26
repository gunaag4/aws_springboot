package com.asg.weather.demo.rest;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import javax.validation.constraints.NotNull;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.WebRequest;

import com.asg.weather.demo.entity.Location;
import com.asg.weather.demo.entity.Weather;
import com.asg.weather.demo.entity.repository.LocationRepository;
import com.asg.weather.demo.entity.repository.WeatherRepository;
import com.asg.weather.demo.exception.ValidationException;
import com.asg.weather.demo.models.WeatherDto;

@RestController
public class WeatherController {

	@Autowired
	private LocationRepository locationService;
	
	@Autowired
	private WeatherRepository weatherRepository;
	
	private SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy HH:mm");
	
	/**
	 * To add weather data
	 * @param weatherDto
	 * @return
	 * @throws ParseException
	 */
	@PostMapping("/weather")
	public ResponseEntity<Weather> postWeatherData(@RequestBody WeatherDto weatherDto) throws ParseException
	{
		
		isValidWeatherData(weatherDto);
		
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
		
		return new ResponseEntity<Weather>(weather, HttpStatus.CREATED);
	}
	
	/**
	 * to get weather data 
	 * @param latitude
	 * @param longitude
	 * @param date
	 * @return
	 * @throws ParseException
	 */
	@GetMapping("/weather")
	public ResponseEntity<Weather> getWeatherData(@RequestParam(name="latitude") @NotNull(message="Valid latitude is required.") Double latitude, 
			@RequestParam(name="longitude") @NotNull(message="Valid longitude is required.") Double longitude, 
			@RequestParam(name="date") @NotNull(message="Valid date (MM/dd/yyyy HH:mm) is required.") String date) throws ParseException
	{
		Weather weather = null;
		
		Location location = locationService.findByLatitudeAndLongitude(latitude, longitude);
		
		if(location == null) return new ResponseEntity<Weather>(weather, HttpStatus.NOT_FOUND);
		
		weather = weatherRepository.findByLocationIdAndDate(location.getId(), dateFormat.parse(date));
		
		if(weather == null) return new ResponseEntity<Weather>(weather, HttpStatus.NOT_FOUND);
		
		return new ResponseEntity<Weather>(weather, HttpStatus.OK);
	}
	
	/**
	 * to remove weather data
	 * @param latitude
	 * @param longitude
	 * @param date
	 * @return
	 * @throws ParseException
	 */
	@DeleteMapping("/weather")
	public ResponseEntity<Weather> deleteWeatherData(@RequestParam(name="latitude") @NotNull(message="Valid latitude is required.") Double latitude, 
			@RequestParam(name="longitude") @NotNull(message="Valid longitude is required.") Double longitude, 
			@RequestParam(name="date") @NotNull(message="Valid date (MM/dd/yyyy HH:mm) is required.") String date) throws ParseException
	{
		Weather weather = null;
		
		Location location = locationService.findByLatitudeAndLongitude(latitude, longitude);
		
		if(location == null) return new ResponseEntity<Weather>(weather, HttpStatus.NO_CONTENT);
		
		weather = weatherRepository.findByLocationIdAndDate(location.getId(), dateFormat.parse(date));
		
		weatherRepository.delete(weather);
		
		weather = null;
		
		return new ResponseEntity<Weather>(weather, HttpStatus.OK);
	}
	
	/**
	 * to update or create weather data
	 * @param weatherDto
	 * @return
	 * @throws ParseException
	 */
	@PutMapping("/weather")
	public ResponseEntity<Weather> updateWeatherData(@RequestBody WeatherDto weatherDto) throws ParseException
	{
		
		isValidWeatherData(weatherDto);
		
		Weather weather = null;
		
		Location location = locationService.findByLatitudeAndLongitude(weatherDto.getLatitude(), weatherDto.getLongitude());
		
		if(location == null) return new ResponseEntity<Weather>(weather, HttpStatus.NO_CONTENT);
		
		weather = weatherRepository.findByLocationIdAndDate(location.getId(), dateFormat.parse(weatherDto.getDate()));
		
		if(weather != null)
		{
			weatherRepository.save(weather);
			return new ResponseEntity<Weather>(weather, HttpStatus.OK);
		}
		else
		{
			weather = weatherRepository.save(weather);
			return new ResponseEntity<Weather>(weather, HttpStatus.CREATED);
		}
		
	}
	
	/**
	 * Invalid date format exception request handler
	 * @param exception
	 * @param request
	 * @return
	 */
	@ExceptionHandler({ ParseException.class })
	public ResponseEntity<Object> handleDateParseException(ParseException exception, WebRequest request) 
	{
		
	    String error = "Invalid date, it should beMM/dd/yyyy HH:mm format.";
	 
	    return new ResponseEntity<Object>(error, HttpStatus.BAD_REQUEST);
	}
	
	/**
	 * Invalid weather data exception request handler
	 * @param exception
	 * @param request
	 * @return
	 */
	@ExceptionHandler({ ValidationException.class })
	public ResponseEntity<Object> handleValidationException(ValidationException exception, WebRequest request) 
	{
		
	    return new ResponseEntity<Object>(exception.getMessage(), HttpStatus.BAD_REQUEST);
	}
	
	/**
	 * checks the given weather data is valid
	 * @param weatherDto
	 */
	
	private void isValidWeatherData(WeatherDto weatherDto)
	{
		
		ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory();
		Validator validator = validatorFactory.usingContext().getValidator();
		
		Set<ConstraintViolation<WeatherDto>> constrains = validator.validate(weatherDto);
		
		if(constrains != null && !constrains.isEmpty())
		{
			StringBuilder sb = new StringBuilder();
			for (ConstraintViolation<WeatherDto> constrain : constrains) 
			{
				sb.append(constrain.getPropertyPath() + "->" + constrain.getMessage() + ";");
			}
			
			throw new ValidationException(sb.toString());
		}
		
	}
	
}
