package websocket;

/**
 * 
 * @author Ryan Mayobre
 *
 */
public abstract class ControlFrame extends Frame
{	
	/**
	 * 
	 * @param opcode
	 */
	ControlFrame(OpCode opcode)
	{
		super(opcode);
	}
}
