package com.httpdroid.websocket.io;

import java.io.Closeable;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import android.util.Base64;

import frame.BinaryFrame;
import com.httpdroid.library.websocket.frame.CloseFrame;
import frame.DataFrame;
import frame.PingFrame;
import frame.PongFrame;
import com.httpdroid.library.websocket.frame.TextFrame;
import com.httpdroid.websocket.Frame;
import com.httpdroid.websocket.WebSocketOpCode;
import com.httpdroid.websocket.exception.HandshakeException;
import com.httpdroid.websocket.exception.InvalidFrameException;
import com.httpdroid.websocket.exception.WebSocketException;
import com.httpdroid.websocket.exception.WebSocketIOException;

/**
 * 
 * @author Ryan Mayobre
 *
 */
public class WebSocketOutputStreamReader implements Closeable
{
	/**
	 * Required to create a magic string and shake hands with client.
	 */
	private static final String MAGIC_KEY = "258EAFA5-E914-47DA-95CA-C5AB0DC85B11";
	
	/**
     * Payload length indicating that the payload's true length is a
     * yet-to-be-provided unsigned 16-bit integer.
     */
	private static final int LENGTH_16 = 0x7E;

    /**
     * A payload specified with 16 bits must have at least this
     * length in order to be considered valid.
     */
    private static final int LENGTH_16_MIN = 0x7D;

    /**
     * Payload length indicating that the payload's true length is a
     * yet-to-be-provided unsigned 64-bit integer (MSB = 0).
     */
    private static final int LENGTH_64 = 0x7F;

    /**
     * A payload specified with 64 bits must have at least this
     * length in order to be considered valid.
     */
    private static final int LENGTH_64_MIN = 0xffff;
    
	/**
     * Binary mask to remove all but the bits of octet 1.
     */
    private static final int MASK_LOW_WORD_HIGH_BYTE = 0x0000ff00;

    /**
     * Binary mask to remove all but the lowest 8 bits (octet 0).
     */
    private static final int MASK_LOW_WORD_LOW_BYTE = 0x000000ff;

    /**
     * Number of bits required to shift octet 1 into the lowest 8 bits.
     */
    private static final int OCTET_ONE = 8;
    
    /**
     * WebSocket defined opcode for a Binary frame. Includes high bit (0x80)
     * to indicate that the frame is the final/complete frame.
     */
    private static final int OPCODE_BINARY = 0x82;

    /**
     * WebSocket defined opcode for a Close frame. Includes high bit (0x80)
     * to indicate that the frame is the final/complete frame.
     */
    private static final int OPCODE_CLOSE = 0x88;

    /**
     * WebSocket defined opcode for a Pong frame. Includes high bit (0x80)
     * to indicate that the frame is the final/complete frame.
     */
    private static final int OPCODE_PONG = 0x8A;

    /**
     * WebSocket defined opcode for a Text frame. Includes high bit (0x80)
     * to indicate that the frame is the final/complete frame.
     */
    private static final int OPCODE_TEXT = 0x81;
    
	/**
	 * WebSocket output stream of data.
	 */
	private final OutputStream out;

	WebSocketOutputStreamReader(OutputStream out) {
		this.out = out;
	}
	
	public void openHandShake(String key) throws HandshakeException {
		try {
			out.write(("HTTP/1.1 101 Switching Protocols\r\n").getBytes());
			out.write(("Upgrade: websocket\r\n").getBytes());
			out.write(("Connection: Upgrade\r\n").getBytes());
			out.write(("Sec-WebSocket-Accept: " + getAcceptKey(key)).getBytes());
			out.write(("\r\n\r\n").getBytes());
		} catch (IOException e) {
			throw new HandshakeException("", e);
		}
	}
	
	/**
	 * Generates acceptance key to be sent back to client when performing handshake.
	 * 
	 * @param key - The key given by client during request.
	 * @return The acceptance key.
	 * @throws HandshakeException Thrown when there is an error with the SHA-1 hash function result.
	 * @see <a href="https://tools.ietf.org/html/rfc6455#section-4.2.2">RFC 6455, Section 4.2.2 (Sending the Server's Opening Handshake)</a>
	 */
	private String getAcceptKey(String key) throws HandshakeException {
		try {
			MessageDigest message = MessageDigest.getInstance("SHA-1");
			String magic_string = key + MAGIC_KEY;
			message.update(magic_string.getBytes(), 0, magic_string.length());
			return Base64.encodeToString(message.digest(), Base64.DEFAULT);
		} catch (NoSuchAlgorithmException e) {
			throw new HandshakeException("Could not apply SHA-1 hashing function to key.", e);
		}
	}

    /**
     * Send a frame to another end point.
     *
     * @param frame
     * @throws WebSocketException
     */
	public void write(Frame frame) throws WebSocketException {
	    try {
            switch (frame.getOpcode()) {
                case WebSocketOpCode.OpCode.TEXT:
                    writeData(frame);
                case WebSocketOpCode.OpCode.BINARY:
                    writeData(frame);
                case WebSocketOpCode.OpCode.CLOSE:
                    writeClose(frame);
                case WebSocketOpCode.OpCode.PING:
                    writePing(frame);
                case WebSocketOpCode.OpCode.PONG:
                    writePong(frame);
                default:
                    throw new InvalidFrameException("Invalid WebSocketOpCode found inside of frame - " + frame.getOpcode());
            }
        } catch (IOException e) {
	        throw new WebSocketIOException("Could not read from WebSocket's input stream.", e);
        }
	}

    private void writeData(Frame frame) throws IOException {
	    byte[] payload = frame.getPayload().toByteArray();
        if(payload.length <= LENGTH_16_MIN) {
            out.write(payload.length);
            out.write(payload);
        }
        else if(payload.length <= LENGTH_64_MIN) {
            out.write(LENGTH_16);
            byte[] lenBytes = this.toByteArray((short) payload.length);
            out.write(lenBytes);
            out.write(payload);
        }
        else {
            out.write(LENGTH_64);
            byte[] lenBytes = this.toByteArray((long) payload.length);
            out.write(lenBytes);
            out.write(payload);
        }
    }

    /**
     * Send a close frame without a closing code sent with it. To send a closing frame
     * with a status inside the payload, use {@link #write(Frame)} and pass a frame with
     * closing frame opcode, as well as a {@link com.httpdroid.websocket.exception.WebSocketException.CloseCode}.
     * @throws IOException
     * @see #write(Frame)
     * @see com.httpdroid.websocket.WebSocketOpCode.OpCode
     * @see com.httpdroid.websocket.exception.WebSocketException.CloseCode
     */
    public void writeClose() throws WebSocketIOException {
        try {
            out.write(new byte[] {
                    (byte) OPCODE_CLOSE, (byte) 0x00
            });
        } catch (IOException e) {
            throw new WebSocketIOException("Output stream could not send closing frame.", e);
        }
    }

    private void writeClose(Frame frame) throws WebSocketIOException {
        // Get closing status from frame's payload.
        // Convert payload from frame into byte array, then to integer.
	    @WebSocketException.CloseCode int status
                = ByteBuffer.wrap(frame.getPayload().toByteArray()).getInt();
        try {
            out.write(new byte[]{
                    (byte) OPCODE_CLOSE, (byte) 0x02,
                    (byte) ((status & MASK_LOW_WORD_HIGH_BYTE) >> OCTET_ONE),
                    (byte) (status& MASK_LOW_WORD_LOW_BYTE)
            });
        } catch (IOException e) {
            throw new WebSocketIOException("Output stream could not send closing frame.", e);
        }
    }

    private void writePing(Frame frame) {

    }

    private void writePong(Frame frame) throws WebSocketIOException {
        try {
            out.write(new byte[] {
                    (byte) OPCODE_PONG,
                    (byte) (frame.getPayload().size())
            });
        } catch (IOException e) {
            throw new WebSocketIOException("Output stream could not send back pong.", e);
        }
    }
	
	/**
	 * Create a byte array from a number.
	 * 
	 * @param data - {@link Number}
	 * @return byte array.
	 */
	private byte[] toByteArray(Number data) {
		Class<? extends Number> dataType = data.getClass();
		
		long value;
		int length;
		if(Byte.class == dataType) {
			length = 1;
			value = data.byteValue();
		} else {
			if(Short.class == dataType) {
				length = 2;
				value = data.shortValue();
			} else {
				if(Integer.class == dataType) {
					length = 4;
					value = data.intValue();
				} else {
					if(Long.class == dataType) {
						length = 8;
						value = data.longValue();
					} else {
						throw new IllegalArgumentException("Parameter must be one of the following types:\n Byte, Short, Integer, Long");
					}
				}
			}
		}
		
		byte[] byteArray = new byte[length];
		
		for (int i = 0; i < length; i++) {
			byteArray[i] = ((byte) (int) (value >> 8 * (length - i - 1) & 0xFF));
		}

		return byteArray;
	}
	
	@Deprecated
	public void ping(final PingFrame frame) {
		
	}
	
	@Deprecated
	public void pong(final PongFrame frame) throws IOException {
		out.write(new byte[] {
			(byte) OPCODE_PONG, 
			(byte) (frame.getSize())
		});
	}

	/**
	 * Must send a Closing frame before closing stream.
	 * @see #writeClose(Frame)
	 */
	@Override
	public void close() throws IOException {
		out.close();
	}
}
