package websocket;

/**
 * TODO javadoc this.
 * @author Ryan Mayobre
 *
 */
public abstract class DataFrame extends Frame
{
	/**
	 * 
	 */
	private final byte[] DATA;
	
	/**
	 * 
	 * @param opcode
	 * @param data
	 */
	public DataFrame(OpCode opcode, byte[] data) 
	{
		super(opcode);
		this.DATA = data;
	}
	
	/**
	 * 
	 * @return
	 */
	public byte[] getData()
	{
		return this.DATA;
	}
}
