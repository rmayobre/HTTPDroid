package frame;

import websocket.OpCode;

/**
 * A class structure of a WebSocket data frame.
 * @author Ryan Mayobre
 * @see {@link Frame}
 */
@Deprecated
public class DataFrame extends Frame
{
	/**
	 * Default constructor.
	 * @param opcode - {@link OpCode}
	 * @param data - message represented as byte array.
	 */
	protected DataFrame(OpCode opcode, byte[] data) 
	{
		super(opcode, data);
	}
	
	/**
	 * Constructor for creating a frame from a stream.
	 * @param b0 - first byte of the stream.
	 * @param b1 - second byte of the stream.
	 * @throws InvalidFrameException
	 */
	protected DataFrame(int b0, int b1) throws InvalidFrameException
	{
		super(b0, b1);
	}
	
	/**
	 * @return {@link Frame#payload} in byte array.
	 */
	public byte[] getData()
	{
		return payload.toByteArray();
	}
}
