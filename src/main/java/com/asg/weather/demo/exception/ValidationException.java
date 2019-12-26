package com.asg.weather.demo.exception;

public class ValidationException extends RuntimeException 
{
	private static final long serialVersionUID = 7686679853049785062L;

	public ValidationException(String message)
	{
		super(message);
	}
}
