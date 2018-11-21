package frame;

import websocket.OpCode;

/**
 * 
 * @author Ryan Mayobre
 * @see {@link Frame}
 */
public class PingFrame extends Frame
{
	PingFrame(byte[] data)
	{
		super(OpCode.PING, data); 
	}
}
