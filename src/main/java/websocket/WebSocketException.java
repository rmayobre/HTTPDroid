package websocket;

/**
 * TODO javadoc this
 * @author Ryan Mayobre
 *
 */
public class WebSocketException extends Exception
{

	/**
	 * 
	 */
	private static final long serialVersionUID = -371194877971098818L;
	
	/**
	 * 
	 * @param message
	 */
	public WebSocketException(String message)
	{
		super(message);
	}
	
	/**
	 * 
	 * @param message
	 * @param e
	 */
	public WebSocketException(String message, Exception e)
	{
		super(message, e);
	}
}
