package websocket;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * Class structure for a WebSocket frame.
 * 
 * @author Ryan Mayobre
 * @see <a href="https://tools.ietf.org/html/rfc6455#section-5.2">RFC 6455, Section 5.2 (Base Framing Protocol)</a>
 */
public class Frame
{
	/**
     * Binary mask to extract the masking flag bit of a WebSocket frame.
     * @see {@link FrameData}
     */
    final int MASK = 0x80;
    
    /**
     * Binary mask to extract the final fragment flag bit of a WebSocket frame.
     * @see {@link FrameData}
     */
    final int MASK_FINAL = 0x80;
    
    /**
	 * Binary mask to extract RSV1 bit of a WebSocket frame.
	 * @see {@link FrameData}
	 */
    final int MASK_RSV1 = 0x40;
    
    /**
	 * Binary mask to extract RSV2 bit of a WebSocket frame.
	 * @see {@link FrameData}
	 */
    final int MASK_RSV2 = 0x40;
    
    /**
	 * Binary mask to extract RSV3 bit of a WebSocket frame.
	 * @see {@link FrameData}
	 */
    final int MASK_RSV3 = 0x40;
    
	/**
     * Binary mask to extract the opcode bits of a WebSocket frame.
     * @see {@link FrameData}
     */
    final int MASK_OPCODE = 0x0F;
    
    /**
	 * Maximum size of Control frame.
	 * @see {@link FrameData}
	 */
    final int MAX_CONTROL_PAYLOAD = 0x7D;
    
	/**
	 * Final fragment.
	 */
	private boolean FIN;
	
	@SuppressWarnings("unused")
	private boolean RSV1;
	
	@SuppressWarnings("unused")
	private boolean RSV2;
	
	@SuppressWarnings("unused")
	private boolean RSV3;
	
	/**
	 * OpCode of the frame.
	 * @see {@link OpCode}
	 */
	private OpCode OPCODE;
	
	/**
	 * Determine if the frame is masked.
	 */
	private boolean MASKED;
	
	/**
	 * Size of payload.
	 */
	int PAYLOAD_LENGTH;
	
	/**
	 * Payload inside of frame.
	 */
	private ByteArrayOutputStream PAYLOAD;
	
	/**
	 * Pointer to next frame.
	 */
	private Frame NEXT;
	
	/**
	 * Constructor for server to create frame.
	 * 
	 * @param opcode of the frame being created.
	 * @see {@link OpCode}
	 */
	protected Frame(OpCode opcode)
	{
		FIN = true;
		RSV1 = false;
		RSV2 = false;
		RSV3 = false;
		OPCODE = opcode;
		MASKED = false;
		PAYLOAD = null;
		NEXT = null;
	}
	
	/**
	 * Constructor for reading a frame sent from client.
	 * @param b0 - First byte from stream.
	 * @param b1 - Second byte from stream.
	 * @throws InvalidFrameException thrown from {@link OpCode}
	 */
	Frame(int b0, int b1) throws InvalidFrameException
	{
		/*
		 * Gather FIN, RSVs, and OPCODE.
		 */
		FIN = ((b0 & MASK_FINAL) != 0);
		RSV1 = ((b0 & MASK_RSV1) != 0);
		RSV2 = ((b0 & MASK_RSV2) != 0);
		RSV3 = ((b0 & MASK_RSV3) != 0);
		OPCODE = OpCode.find((byte)(b0 & MASK_OPCODE));
		/*
		 * Find MASK and PAYLOAD_LENGTH
		 */
		MASKED = ((b1 & MASK) != 0);
		PAYLOAD_LENGTH = b1 & 0x7F;
		PAYLOAD = new ByteArrayOutputStream();
		NEXT = null; // Last frame must always be null.
	}
	
	/**
	 * Determine if this frame is a Control frame.
	 * 
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
	 * 
	 * @return TRUE if frame is a Data frame, otherwise FALSE.
	 * @see {@link OpCode}
	 */
	public boolean isDataFrame()
	{
		return OPCODE == OpCode.TEXT
			|| OPCODE == OpCode.BINARY;
	}
	
	/**
	 * @return {@link #FIN}
	 */
	public boolean isFIN()
	{
		return FIN;
	}
	
	/**
	 * @return {@link #OPCODE}
	 */
	public OpCode getOpcode()
	{
		return OPCODE;
	}
	
	/**
	 * @return {@link #MASKED}
	 */
	public boolean isMasked()
	{
		return MASKED;
	}
	
	/**
	 * @return size of {@link #PAYLOAD} in int.
	 */
	public int size()
	{
		if(PAYLOAD == null)
			return 0;
		else
			return PAYLOAD.toByteArray().length;
	}
	
	/**
	 * Add data to frame's payload
	 * 
	 * @param data - Masked data inside of frame.
	 */
	public void addToPayload(char data)
	{
		PAYLOAD.write(data);
	}
	
	/**
	 * Gather payload from all frames, including continuation frames.
	 * 
	 * @return {@link Frame#PAYLOAD}
	 * @throws IOException 
	 */
	public ByteArrayOutputStream getPayload() throws IOException
	{
		if(NEXT == null)
			return PAYLOAD;
		else
		{
			ByteArrayOutputStream data = new ByteArrayOutputStream();
			data.write(PAYLOAD.toByteArray());
			data.write(NEXT.getPayload().toByteArray());
			return data;
		}
	}
	
	/**
	 * @return {@link #NEXT}
	 */
	public Frame getNext()
	{
		return NEXT;
	}
	
	/**
	 * Use this for continuation frame implementations.
	 * 
	 * @param frame {@link Frame}
	 */
	public void addFrame(Frame frame)
	{	
		Frame current = NEXT;
		while(current.getNext() != null)
			current = NEXT;
		current.setNext(frame);
	}
	
	/**
	 * Declare next frame.
	 * 
	 * @param frame {@link Frame}
	 */
	private void setNext(Frame frame)
	{
		NEXT = frame;
	}
	
}
