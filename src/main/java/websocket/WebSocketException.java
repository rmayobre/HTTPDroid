package websocket;

/**
 * Exception for any connection problems for the WebSocket to client.
 * @author Ryan Mayobre
 * @see <a href="https://tools.ietf.org/html/rfc6455">RFC 6455</a>
 * @see {@link WebSocket}
 */
public class WebSocketException extends Exception
{
	private static final long serialVersionUID = -371194877971098818L;
	
	public WebSocketException(String message)
	{
		super(message);
	}
	
	public WebSocketException(String message, Exception e)
	{
		super(message, e);
	}
}