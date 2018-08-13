package websocket;

/**
 * Currently not being used.
 * @author Ryan Mayobre
 *
 */
public class PongFrame extends ControlFrame
{
	PongFrame(PingFrame ping)
	{
		super(OpCode.PONG);
	}
}
