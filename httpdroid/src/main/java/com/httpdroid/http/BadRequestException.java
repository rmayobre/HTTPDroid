package com.httpdroid.http;

public class BadRequestException extends Exception
{
	private static final long serialVersionUID = 297852563315154837L;

	public BadRequestException(String message) 
	{
		super(message);
	}

}
