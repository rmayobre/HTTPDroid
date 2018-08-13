package websocket;

/**
 * 
 * @author Ryan Mayobre
 *
 */
public abstract class ControlFrame extends Frame
{	
	private byte[] DATA;
	/**
	 * 
	 * @param opcode
	 */
	ControlFrame(OpCode opcode)
	{
		super(opcode);
	}
}
