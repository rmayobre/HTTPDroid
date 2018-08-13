package websocket;

/**
 * Class structor for Control Frames.
 * @author Ryan Mayobre
 */
public abstract class ControlFrame extends Frame
{	
	/**
	 * Application data from {@link PingFrame} and {@link PongFrame}.
	 */
	@Deprecated
	private byte[] DATA;
	
	/**
	 * Constructor for {@link CloseFrame}.
	 * @param opcode
	 */
	ControlFrame(OpCode opcode)
	{
		super(opcode);
	}
	
	/**
	 * Constuctor for {@link PingFrame} and {@link PongFame}.
	 * @param opcode
	 * @param data
	 */
	@Deprecated
	ControlFrame(OpCode opcode, byte[] data)
	{
		super(opcode);
		this.DATA = data;
	}
	
	
	/**
	 * @return {@link #DATA}.
	 */
	public byte[] getData()
	{
		return this.DATA;
	}
}
