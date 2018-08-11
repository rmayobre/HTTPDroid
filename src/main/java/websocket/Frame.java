package websocket;

import java.io.ByteArrayOutputStream;

/**
 * 
 * @author Ryan Mayobre
 *
 */
public class Frame implements FrameData
{
	/**
	 * 
	 */
	private boolean FIN;
	
	/**
	 * 
	 */
	private boolean RSV1;
	
	/**
	 * 
	 */
	private boolean RSV2;
	
	/**
	 * 
	 */
	private boolean RSV3;
	
	/**
	 * 
	 * @see {@link OpCode}
	 */
	private OpCode OPCODE;
	
	/**
	 * 
	 */
	private boolean MASKED;
	
	/**
	 * 
	 */
	public int PAYLOAD_LENGTH;
	
	/**
	 * 
	 */
	private ByteArrayOutputStream PAYLOAD_DATA;
	
	/**
	 * 
	 */
	Frame NEXT;
	
	public Frame(OpCode opcode)
	{
		this.FIN = true;
		this.RSV1 = false;
		this.RSV2 = false;
		this.RSV3 = false;
		this.OPCODE = opcode;
		this.MASKED = false;
		this.PAYLOAD_DATA = new ByteArrayOutputStream();
		this.NEXT = null;
	}
	
	/**
	 * 
	 * @param b0
	 * @param b1
	 */
	public Frame(int b0, int b1)
	{
		/*
		 * Gather FIN, RSVs, and OPCODE.
		 */
		this.FIN = ((b0 & MASK_FINAL) != 0);
		this.RSV1 = ((b0 & MASK_RSV1) != 0);
		this.RSV2 = ((b0 & MASK_RSV2) != 0);
		this.RSV3 = ((b0 & MASK_RSV3) != 0);
		this.OPCODE = OpCode.find((byte)(b0 & MASK_OPCODE));
		/*
		 * Find MASK and PAYLOAD_LENGTH
		 */
		this.MASKED = ((b1 & MASK) != 0);
		this.PAYLOAD_LENGTH = b1 & PAYLOAD_LENGTH_64;
		this.PAYLOAD_DATA = new ByteArrayOutputStream();
		/*
		 * Declare last frame.
		 */
		this.NEXT = null; // Last frame must always be null.
	}
	
	/**
	 * 
	 * @return
	 */
	public boolean getFIN()
	{
		return FIN;
	}
	
	/**
	 * 
	 * @return
	 */
	public boolean getRSV1()
	{
		return RSV1;
	}
	
	/**
	 * 
	 * @return
	 */
	public boolean getRSV2()
	{
		return RSV2;
	}
	
	/**
	 * 
	 * @return
	 */
	public boolean getRSV3()
	{
		return RSV3;
	}
	
	/**
	 * 
	 * @return
	 */
	public OpCode getOpCode()
	{
		return OPCODE;
	}
	
	/**
	 * Determine if this frame is a Control frame.
	 * @return True if frame is a Control frame, otherwise false.
	 * @see {@link OpCode}
	 */
	public boolean isControlFrame()
	{
		return OPCODE == OpCode.CLOSE
			|| OPCODE == OpCode.PING
			|| OPCODE == OpCode.PONG;
	}
	
	/**
	 * Determine if this frame is a Data frame.
	 * @return TRUE if frame is a Data frame, otherwise FALSE.
	 * @see {@link OpCode}
	 */
	public boolean isDataFrame()
	{
		return OPCODE == OpCode.TEXT
			|| OPCODE == OpCode.BINARY;
	}
	
	public boolean getMask()
	{
		return MASKED;
	}
	
	/**
	 * 
	 * @return
	 */
	public ByteArrayOutputStream getPayload()
	{
		return PAYLOAD_DATA;
	}
}
