package websocket;

import java.io.UnsupportedEncodingException;

/**
 * Class structure for a Text frame.
 * @author Ryan Mayobre
 *
 */
public class TextFrame extends DataFrame
{
	/**
	 * Message inside text frame.
	 */
	private final String MESSAGE;
	
	/**
	 * Constructor for creating a frame.
	 * @param message - message to be placed inside frame.
	 * @throws UnsupportedEncodingException Thrown if message 
	 * does not support UTF-8 encoding.
	 */
	TextFrame(String message) throws UnsupportedEncodingException 
	{
		super(OpCode.TEXT, message.getBytes("UTF-8"));
		this.MESSAGE = message;
	}
	
	/**
	 * @return {@link #MESSAGE}
	 */
	public String getMessage()
	{
		return this.MESSAGE;
	}
}
