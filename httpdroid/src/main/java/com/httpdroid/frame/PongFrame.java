package frame;

import websocket.OpCode;

/**
 * 
 * @author Ryan Mayobre
 * @see {@link Frame}
 */
public class PongFrame extends Frame
{
	PongFrame(byte[] data)
	{
		super(OpCode.PONG, data);
	}
}
