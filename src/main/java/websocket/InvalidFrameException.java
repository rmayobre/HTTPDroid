package websocket;

/**
 * Exception for any problems with building or using WebSocket Data frames.
 * <p>
 * Must follow the <a href="https://tools.ietf.org/html/rfc6455">RFC 6455</a> guidelines.
 * @author Ryan Mayobre
 * @see {@link Frame}
 * @see {@link FrameData}
 * @see <a href="https://tools.ietf.org/html/rfc6455#section-5">RFC 6455, Section 5 (Data Framing)</a>
 */
public class InvalidFrameException extends Exception 
{
	private static final long serialVersionUID = -6571826516080525468L;
	
	public InvalidFrameException(String message)
	{
		super(message);
	}

	public InvalidFrameException(String message, Exception e) 
	{
		super(message, e);
	}
}

