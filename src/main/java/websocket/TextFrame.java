package websocket;

import java.io.UnsupportedEncodingException;

/**
 * TODO javadoc this.
 * @author Ryan Mayobre
 *
 */
public class TextFrame extends DataFrame
{
	/**
	 * 
	 */
	private final String MESSAGE;
	
	/**
	 * 
	 * @param message
	 * @throws UnsupportedEncodingException
	 */
	public TextFrame(String message) throws UnsupportedEncodingException 
	{
		super(OpCode.TEXT, message.getBytes("UTF-8"));
		this.MESSAGE = message;
	}
	
	/**
	 * 
	 * @return
	 */
	public String getMessage()
	{
		return this.MESSAGE;
	}
}
