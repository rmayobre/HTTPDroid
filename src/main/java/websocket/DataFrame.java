package websocket;

/**
 * A class structure of a WebSocket data frame.
 * @author Ryan Mayobre
 * @see {@link Frame}
 */
public abstract class DataFrame extends Frame
{
	/**
	 * Message to be sent to client in byte.
	 */
	private final byte[] DATA;
	
	/**
	 * Default constructor.
	 * @param opcode - {@link OpCode}
	 * @param data - message represented as byte array.
	 */
	protected DataFrame(OpCode opcode, byte[] data) 
	{
		super(opcode);
		DATA = data;
	}
	
	/**
	 * @return {@link #DATA}
	 */
	public byte[] getData()
	{
		return DATA;
	}
}
