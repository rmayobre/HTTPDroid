package websocket;

/**
 * TODO javadoc this
 * @author Ryan Mayobre
 * @see {@link WebSocket}
 */
public class HandshakeException extends Exception
{

	/**
	 * 
	 */
	private static final long serialVersionUID = 5491611537088628567L;
	
	/**
	 * 
	 * @param message
	 */
	public HandshakeException(String message)
	{
		super(message);
	}
	
	/**
	 * 
	 * @param message
	 * @param e
	 */
	public HandshakeException(String message, Exception e)
	{
		super(message, e);
	}

}
