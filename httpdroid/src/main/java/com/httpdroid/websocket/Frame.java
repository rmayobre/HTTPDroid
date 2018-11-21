package com.httpdroid.websocket;

import com.httpdroid.websocket.exception.InvalidFrameException;

import java.io.ByteArrayOutputStream;

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
    @Deprecated
    private static final int MAX_CONTROL_PAYLOAD = 0x7D;
    
	/**
	 * Final fragment.
	 */
	private final boolean fin;
	
	@SuppressWarnings("unused")
	private final boolean rsv1;
	
	@SuppressWarnings("unused")
	private final boolean rsv2;
	
	@SuppressWarnings("unused")
	private final boolean rsv3;
	
	/**
	 * WebSocketOpCode of the frame.
	 * @see WebSocketOpCode
	 */
	@WebSocketOpCode.OpCode
	private final int opcode;
	
	/**
	 * Determine if the frame is masked.
	 */
	private final boolean masked;
	
	/**
	 * Size of payload.
	 */
	public int payload_length;
	
	/**
	 * Data held inside of frame.
	 */
	private ByteArrayOutputStream payload;

	/**
	 * TODO put data into payload.
	 * Constructor for server to create frame.
	 *
	 * @param opcode of the frame being created.
	 * @param data for the frame to place inside of {@link Frame#payload}.
	 * @see WebSocketOpCode
	 */
	public Frame(@WebSocketOpCode.OpCode int opcode, byte[] data) {
		fin = true;
		rsv1 = false;
		rsv2 = false;
		rsv3 = false;
		this.opcode = opcode;
		masked = false;
		payload = new ByteArrayOutputStream(data.length);
		payload.write(data, 0, data.length);
	}

	/**
	 * Constructor for reading a frame sent from client.
	 * @param b0 - First byte from stream.
	 * @param b1 - Second byte from stream.
	 * @see WebSocketOpCode
	 */
	public Frame(int b0, int b1) throws InvalidFrameException {
		fin = ((b0 & MASK_FINAL) != 0);
		rsv1 = ((b0 & MASK_RSV1) != 0);
		rsv2 = ((b0 & MASK_RSV2) != 0);
		rsv3 = ((b0 & MASK_RSV3) != 0);
		opcode = WebSocketOpCode.find((byte)(b0 & MASK_OPCODE));
		masked = ((b1 & MASK) != 0);
		payload_length = b1 & 0x7F;
		payload = new ByteArrayOutputStream();
	}

	/**
	 * @return {@link #fin}
	 */
	public boolean isFin() {
		return fin;
	}

	/**
	 * @return {@link #opcode}
	 */
	@WebSocketOpCode.OpCode
	public int getOpcode() {
		return opcode;
	}

	/**
	 * @return {@link #masked}
	 */
	public boolean isMasked() {
		return masked;
	}

	/**
	 * Add data to frame's payload
	 *
	 * @param data - Masked data inside of frame.
	 */
	public void addToPayload(char data) {
		payload.write(data);
	}

	/**
	 * @return {@link #payload}
	 */
	public ByteArrayOutputStream getPayload() {
		return payload;
	}

	public int getSize()
	{
		return payload.size();
	}
}
