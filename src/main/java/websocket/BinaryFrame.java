package websocket;

/**
 * 
 * @author Ryan Mayobre
 *
 */
public class BinaryFrame extends DataFrame
{
	/**
	 * Constructor for created a Binary frame.
	 * @param data
	 */
	BinaryFrame(byte[] data) 
	{
		super(OpCode.BINARY, data);
	}
}
