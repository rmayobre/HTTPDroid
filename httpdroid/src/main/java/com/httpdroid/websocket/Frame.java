package com.httpdroid.websocket;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

/**
 * Class structure for a frame fragment.
 * @see <a href="https://tools.ietf.org/html/rfc6455#section-5.2">RFC 6455, Section 5.2 (Base Framing Protocol)</a>
 */
public class Frame {
	/** Binary mask to extract the masking flag bit of a WebSocket frame. */
    private static final int MASK = 0x80;
    /** Binary mask to extract the final fragment flag bit of a WebSocket frame. */
    private static final int MASK_FINAL = 0x80;
    /** Binary mask to extract RSV1 bit of a WebSocket frame. */
    private static final int MASK_RSV1 = 0x40;
    /** Binary mask to extract RSV2 bit of a WebSocket frame. */
    private static final int MASK_RSV2 = 0x20;
    /** Binary mask to extract RSV3 bit of a WebSocket frame. */
    private static final int MASK_RSV3 = 0x10;
	/** Binary mask to extract the opcode bits of a WebSocket frame. */
    private static final int MASK_OPCODE = 0x0F;
    /** Maximum size of Control frame. */
    @Deprecated
    private static final int MAX_CONTROL_PAYLOAD = 0x7D;

	public final boolean rsv1;
	public final boolean rsv2;
	public final boolean rsv3;

	/** Is this the final fragment? */
	public final boolean isFin;

	/** Is this fragment masked? */
	public final boolean isMasked;

	/** OpCode of the fragment. */
	@OpCode
	public final int opcode;

	/** Size of payload. */
    public int payload_length;

	/** Data held inside of frame. */
	public ByteArrayOutputStream payload;

	/**
	 * Constructor for server to create frame.
	 * @param opcode of the frame being created.
	 * @param data for the frame to place inside of {@link Frame#payload}.
	 * @see OpCode
	 */
	public Frame(@OpCode int opcode, byte[] data) {
		isFin = true;
		rsv1 = false;
		rsv2 = false;
		rsv3 = false;
		this.opcode = opcode;
		isMasked = false;
		payload = new ByteArrayOutputStream(data.length);
		payload.write(data, 0, data.length);
	}

	/**
	 * Constructor for reading a frame sent from client.
	 * @param b0 - First set of 4 bytes from stream.
	 * @param b1 - Second set of 4 byte from stream.
	 * @see OpCode
	 */
	public Frame(int b0, int b1) throws WebSocketException {
		isFin = ((b0 & MASK_FINAL) != 0);
		rsv1 = ((b0 & MASK_RSV1) != 0);
		rsv2 = ((b0 & MASK_RSV2) != 0);
		rsv3 = ((b0 & MASK_RSV3) != 0);
		opcode = check((byte)(b0 & MASK_OPCODE));
		isMasked = ((b1 & MASK) != 0);
		payload_length = b1 & 0x7F;
		payload = new ByteArrayOutputStream();
	}

	public Frame(List<Frame> fragments) throws IOException {
	    isFin = fragments.get(fragments.size()).isFin;
	    rsv1 = fragments.get(fragments.size()).rsv1;
	    rsv2 = fragments.get(fragments.size()).rsv2;
	    rsv3 = fragments.get(fragments.size()).rsv3;
	    opcode =fragments.get(fragments.size()).opcode;
	    isMasked = fragments.get(fragments.size()).isMasked;
	    payload = new ByteArrayOutputStream();
	    for (Frame fragment: fragments) {
	        payload.write(fragment.payload.toByteArray());
        }
        payload_length = payload.size();
    }

	/**
	 * Check if bytecode is a valid opcode. Throws an exception if not valid.
	 * @param code data stream's given opcode.
	 * @throws WebSocketException Thrown if OpCode was not recognized.
     * @see OpCode
	 */
	@OpCode
	private int check (int code) throws WebSocketException {
	    if (code == OpCode.CONTINUATION
                || code == OpCode.TEXT
                || code == OpCode.BINARY
                || code == OpCode.CLOSE
                || code == OpCode.PING
                || code == OpCode.PONG) {
            throw WebSocketException.invalidFrameException ("Invalid OpCode found inside of frame - " + code);
        } else {
            return code;
        }
	}
}
