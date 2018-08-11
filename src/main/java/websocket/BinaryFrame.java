package websocket;

/**
 * 
 * @author Ryan Mayobre
 *
 */
public class BinaryFrame extends DataFrame
{
	/**
	 * 
	 * @param data
	 */
	public BinaryFrame(byte[] data) 
	{
		super(OpCode.BINARY, data);
	}
}
