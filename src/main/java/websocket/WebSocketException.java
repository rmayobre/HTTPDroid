package websocket;

import frame.CloseFrame;

/**
 * Exception for Websocket connectivity issues.
 * @author Ryan Mayobre
 */
public class WebSocketException extends Exception
{
	private static final long serialVersionUID = -371194877971098818L;
	
	/**
	 * Closing frame code to be sent to client.
	 * @see {@link CloseFrame}
	 */
	private final int close;
	
	/**
	 * @param message Error message.
	 * @param close Closing reason.
	 * @see {@link CloseFrame}
	 */
	public WebSocketException(String message, int close)
	{
		super(message);
		this.close = close;
	}
	
	/**
	 * 
	 * @param message Error message.
	 * @param e Exception being thrown.
	 * @param close Closing reason.
 	 * @see {@link CloseFrame}
	 */
	public WebSocketException(String message, Exception e, int close)
	{
		super(message, e);
		this.close = close;
	}
	
	/**
	 * @return {@link #close}
	 */
	public int getClose()
	{
		return close;
	}
}