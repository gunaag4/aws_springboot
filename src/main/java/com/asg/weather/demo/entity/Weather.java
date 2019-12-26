package com.asg.weather.demo.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

@Entity
@Table(name = "Weather", uniqueConstraints = @UniqueConstraint(columnNames = {"date", "location_id"}))
public class Weather {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name="weather_id")
	private Long id;
	
	@Column(name = "date")
	private Date date;
		
	@Column(name = "temperature")
	private Double temperature;
	
	@ManyToOne
	@JoinColumn(name="location_id", referencedColumnName = "location_id")
	private Location location;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public Double getTemperature() {
		return temperature;
	}

	public void setTemperature(Double temperature) {
		this.temperature = temperature;
	}

	public Location getLocation() {
		return location;
	}

	public void setLocation(Location location) {
		this.location = location;
	}

	@Override
	public String toString() {
		return "Weather [id=" + id + ", date=" + date + ", temperature=" + temperature + ", location=" + location + "]";
	}

	
}
