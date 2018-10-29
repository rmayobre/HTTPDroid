package frame;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import websocket.OpCode;

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
     */
    private static final int MASK = 0x80;
    
    /**
     * Binary mask to extract the final fragment flag bit of a WebSocket frame.
     */
    private static final int MASK_FINAL = 0x80;
    
    /**
	 * Binary mask to extract RSV1 bit of a WebSocket frame.
	 */
    private static final int MASK_RSV1 = 0x40;
    
    /**
	 * Binary mask to extract RSV2 bit of a WebSocket frame.
	 */
    private static final int MASK_RSV2 = 0x20;
    
    /**
	 * Binary mask to extract RSV3 bit of a WebSocket frame.
	 */
    private static final int MASK_RSV3 = 0x10;
    
	/**
     * Binary mask to extract the opcode bits of a WebSocket frame.
     */
    private static final int MASK_OPCODE = 0x0F;
    
    /**
	 * Maximum size of Control frame.
	 */
    private static final int MAX_CONTROL_PAYLOAD = 0x7D;
    
	/**
	 * Final fragment.
	 */
	public final boolean fin;
	
	@SuppressWarnings("unused")
	private final boolean rsv1;
	
	@SuppressWarnings("unused")
	private final boolean rsv2;
	
	@SuppressWarnings("unused")
	private final boolean rsv3;
	
	/**
	 * OpCode of the frame.
	 * @see {@link OpCode}
	 */
	public final OpCode opcode;
	
	/**
	 * Determine if the frame is masked.
	 */
	public final boolean masked;
	
	/**
	 * Size of payload.
	 */
	public int payload_length;
	
	/**
	 * Data held inside of frame.
	 */
	ByteArrayOutputStream payload;
	
	/**
	 * Pointer to next frame.
	 */
	private Frame next;
	
	/**
	 * TODO put data into payload.
	 * Constructor for server to create frame.
	 * 
	 * @param opcode of the frame being created.
	 * @param data for the frame to place inside of {@link Frame#payload}.
	 * @see {@link OpCode}
	 */
	protected Frame(OpCode opcode, byte[] data)
	{
		fin = true;
		rsv1 = false;
		rsv2 = false;
		rsv3 = false;
		this.opcode = opcode;
		masked = false;
		payload = null;
		next = null;
	}
	
	/**
	 * Constructor for reading a frame sent from client.
	 * @param b0 - First byte from stream.
	 * @param b1 - Second byte from stream.
	 * @throws InvalidFrameException thrown from {@link OpCode}
	 */
	public Frame(int b0, int b1) throws InvalidFrameException
	{
		fin = ((b0 & MASK_FINAL) != 0);
		rsv1 = ((b0 & MASK_RSV1) != 0);
		rsv2 = ((b0 & MASK_RSV2) != 0);
		rsv3 = ((b0 & MASK_RSV3) != 0);
		opcode = OpCode.find((byte)(b0 & MASK_OPCODE));
		masked = ((b1 & MASK) != 0);
		payload_length = b1 & 0x7F;
		payload = new ByteArrayOutputStream();
		next = null; // Last frame must always be null.
	}
	
	/**
	 * @return {@link #fin}
	 */
	public boolean isFin()
	{
		return fin;
	}
	
	/**
	 * @return {@link #opcode}
	 */
	public OpCode getOpcode()
	{
		return opcode;
	}
	
	/**
	 * @return {@link #masked}
	 */
	public boolean isMasked()
	{
		return masked;
	}
	
	/**
	 * @return size of {@link #PAYLOAD} in int.
	 */
	public int size()
	{
		if(payload == null)
			return 0;
		else
			return payload.toByteArray().length;
	}
	
	/**
	 * Add data to frame's payload
	 * 
	 * @param data - Masked data inside of frame.
	 */
	public void addToPayload(char data)
	{
		payload.write(data);
	}
	
	/**
	 * Gather payload from all frames, including continuation frames.
	 * 
	 * @return {@link Frame#PAYLOAD}
	 * @throws IOException 
	 */
	public ByteArrayOutputStream getPayload() throws IOException
	{
		if(next == null)
			return payload;
		else
		{
			ByteArrayOutputStream data = new ByteArrayOutputStream();
			data.write(payload.toByteArray());
			data.write(next.getPayload().toByteArray());
			return data;
		}
	}
	
	/**
	 * @return {@link #NEXT}
	 */
	public Frame getNext()
	{
		return next;
	}
	
	/**
	 * Use this for continuation frame implementations.
	 * 
	 * @param frame {@link Frame}
	 * @throws InvalidFrameException thrown by {@link #setNext(Frame)}
	 */
	public void addFrame(Frame frame) throws InvalidFrameException
	{	
		Frame current = next;
		
		while(current.getNext() != null)
			current = getNext();
		current.setNext(frame);
	}
	
	/**
	 * Declare next frame.
	 * 
	 * @param frame {@link Frame}
	 * @throws InvalidFrameException Thrown if a continuous frame is the final fragment.
	 */
	private void setNext(Frame frame) throws InvalidFrameException
	{
		if(!fin)
			next = frame;
		else
			throw new InvalidFrameException("Cannot have a continuous frame set as final fragment.");
	}
	
	@Deprecated
	public int getSize()
	{
		if(next == null)
			return payload.size();
		else
			return payload.size() + next.getSize();
	}
}
