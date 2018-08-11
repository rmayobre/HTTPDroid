package websocket;

/**
 * TODO javadoc this
 * @author Ryan Mayobre
 * @see {@link WebSocket}
 */
public class InvalidFrameException extends Exception 
{
	/**
	 * 
	 */
	private static final long serialVersionUID = -6571826516080525468L;
	
	/**
	 * 
	 * @param message
	 */
	public InvalidFrameException(String message)
	{
		super(message);
	}

	public InvalidFrameException(String message, Exception e) 
	{
		super(message, e);
	}
}
