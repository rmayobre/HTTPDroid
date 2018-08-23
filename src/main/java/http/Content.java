package http;

/**
 * Not sure if i will need this still...
 * 
 * @author Ryan Mayobre
 *
 */
public enum Content
{
	/*
	 * Text files
	 */
	HTML("html"),
	CSS("css"),
	JS("js"),
	JSON("json"),
	/*
	 * Image files
	 */
	ICO("ico"),
	JPEG("jpeg"),
	PNG("png");
	
	private final String extension;
	
	Content(String extension)
	{
		this.extension = extension;
	}
	
	public String getExtension()
	{
		return extension;
	}
	
	public String getMime()
	{
		switch(this)
		{
			case HTML:
				return "text/html";
				
			case CSS:
				return "text/css";
				
			case JS:
				return "application/javascript";
				
			case JSON:
				return "application/json";
				
			case ICO:
				return "image/x-icon";
				
			case JPEG:
				return "image/jpeg";
				
			case PNG:
				return "image/png";
				
			default:
				return null;
		}
	}
}
