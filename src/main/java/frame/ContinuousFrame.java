package frame;

import websocket.OpCode;

/**
 * 
 * @author Ryan Mayobre
 * @see {@link Frame}
 */
public class ContinuousFrame extends Frame
{
	/**
	 * 
	 * @param data
	 */
	ContinuousFrame(byte[] data)
	{
		super(OpCode.CONTINUATION, data);
	}
	
	/**
	 * 
	 * @return
	 */
	public byte[] getData()
	{
		return payload.toByteArray();
	}
}
