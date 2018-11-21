package frame;

import websocket.OpCode;

/**
 * Class structor for Control Frames.
 * @author Ryan Mayobre
 * @see {@link Frame}
 */
@Deprecated
public abstract class ControlFrame extends Frame
{	
	/**
	 * Max payload data size.
	 * TODO check payload size.
	 */
	@SuppressWarnings("unused")
	private final int MAX_SIZE = 125;
	
	/**
	 * Constructor for sending frames.
	 * @param opcode
	 * @param data
	 */
	protected ControlFrame(OpCode opcode, byte[] data)
	{
		super(opcode, data);
	}
	
	/**
	 * Constructor for creating a frame from a stream.
	 * @param b0 - first byte of the stream.
	 * @param b1 - second byte of the stream.
	 * @throws InvalidFrameException
	 */
	protected ControlFrame(int b0, int b1) throws InvalidFrameException
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
