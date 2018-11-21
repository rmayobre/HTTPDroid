package frame;

import websocket.OpCode;

/**
 * 
 * @author Ryan Mayobre
 * @see {@link Frame}
 */
public class BinaryFrame extends Frame
{
	/**
	 * Constructor for created a Binary frame.
	 * @param data to be placed in payload.
	 */
	BinaryFrame(byte[] data) 
	{
		super(OpCode.BINARY, data);
	}
}
