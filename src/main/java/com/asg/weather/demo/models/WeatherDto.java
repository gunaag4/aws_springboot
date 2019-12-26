package com.asg.weather.demo.models;

import java.io.Serializable;

import javax.validation.constraints.NotNull;

public class WeatherDto implements Serializable
{
	private static final long serialVersionUID = 7805780501508456473L;
	
	@NotNull(message = "date is required field")
	private String date;
	private String city;
	private String state;
	@NotNull(message = "latitude is required field")
	private Double latitude;
	@NotNull(message = "longitude is required field")
	private Double longitude;
	private Double temperature;
	
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	public Double getLatitude() {
		return latitude;
	}
	public void setLatitude(Double latitude) {
		this.latitude = latitude;
	}
	public Double getLongitude() {
		return longitude;
	}
	public void setLongitude(Double longitude) {
		this.longitude = longitude;
	}
	public Double getTemperature() {
		return temperature;
	}
	public void setTemperature(Double temperature) {
		this.temperature = temperature;
	}
	@Override
	public String toString() {
		return "WeatherDto [date=" + date + ", city=" + city + ", state=" + state + ", latitude=" + latitude
				+ ", longitude=" + longitude + ", temperature=" + temperature + "]";
	}
	
}
