package websocket;

/**
 * Class structor for Control Frames.
 * @author Ryan Mayobre
 */
public abstract class ControlFrame extends Frame
{	
	/**
	 * Max payload data size.
	 * TODO check payload size.
	 */
	@SuppressWarnings("unused")
	private final int MAX_SIZE = 125;
	
	/**
	 * Payload data from inside the frame.
	 */
	private byte[] DATA;
	
	/**
	 * Constructor for sending frames.
	 * @param opcode
	 * @param data
	 */
	protected ControlFrame(OpCode opcode, byte[] data)
	{
		super(opcode);
		DATA = data;
	}
	
	/**
	 * @return {@link #DATA}.
	 */
	public byte[] getData()
	{
		return DATA;
	}
}
