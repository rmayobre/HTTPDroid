package websocket;

import java.io.Closeable;
import java.io.IOException;
import java.io.OutputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

import frame.BinaryFrame;
import frame.CloseFrame;
import frame.DataFrame;
import frame.PingFrame;
import frame.PongFrame;
import frame.TextFrame;

/**
 * 
 * @author Ryan Mayobre
 *
 */
public class WebSocketOutputStream implements Closeable
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
	 * 
	 */
	private final OutputStream out;
	
	/**
	 * 
	 * @param out
	 */
	WebSocketOutputStream(OutputStream out)
	{
		this.out = out;
	}
	
	public void openHandShake(String key) throws IOException, NoSuchAlgorithmException
	{
		out.write(("HTTP/1.1 101 Switching Protocols\r\n").getBytes());
		out.write(("Upgrade: websocket\r\n").getBytes());
		out.write(("Connection: Upgrade\r\n").getBytes());
		out.write(("Sec-WebSocket-Accept: " + getAcceptKey(key)).getBytes());
		out.write(("\r\n\r\n").getBytes());
	}
	
	/**
	 * Generates acceptance key to be sent back to client when performing handshake.
	 * 
	 * @param key - The key given by client during request.
	 * @return The acceptance key.
	 * @throws NoSuchAlgorithmException Thrown when decryption of client's key can't be done.
	 * @see <a href="https://tools.ietf.org/html/rfc6455#section-4.2.2">RFC 6455, Section 4.2.2 (Sending the Server's Opening Handshake)</a>
	 */
	private String getAcceptKey(String key) throws NoSuchAlgorithmException
	{
		MessageDigest message = MessageDigest.getInstance("SHA-1");
		
		String magic_string = key + MAGIC_KEY;
		
		message.update(magic_string.getBytes(), 0, magic_string.length());
		
		return Base64.getEncoder().encodeToString(message.digest());
	}
	
	/**
	 * 
	 * @param frame
	 * @throws IOException 
	 */
	public void write(TextFrame frame) throws IOException
	{
		out.write(OPCODE_TEXT);
		write((DataFrame) frame);
	}
	
	/**
	 * 
	 * @param frame
	 * @throws IOException 
	 */
	public void write(BinaryFrame frame) throws IOException
	{
		out.write(OPCODE_BINARY);
		write((DataFrame) frame);
	}
	
	/**
	 * Sends a {@link DataFrame} to client.
	 * 
	 * TODO test this with a javascript client.
	 * 
	 * @param frame - data frame to be sent to client.
	 * @throws IOException Thrown because {@link OutputStream} 
	 * was either corrupted or lost connection.
	 */
	public void write(final DataFrame frame) throws IOException
	{	
		if(frame.getData().length <= LENGTH_16_MIN)
		{
			out.write(frame.getData().length);
			out.write(frame.getData());
		}
		else if(frame.getData().length <= LENGTH_64_MIN)
		{
			out.write(LENGTH_16);
			byte[] lenBytes = this.toByteArray(Short.valueOf((short) frame.getData().length));
			out.write(lenBytes);
			out.write(frame.getData());
		}
		else
		{
			out.write(LENGTH_64);
			byte[] lenBytes = this.toByteArray(Long.valueOf(frame.getData().length));
			out.write(lenBytes);
			out.write(frame.getData());
		}
	}
	
	/**
	 * Takes in a number (Long or short) and creates a byte array.
	 * 
	 * @param data - {@link Number}
	 * @return byte array.
	 */
	private byte[] toByteArray(Number data)
	{
		Class<? extends Number> dataType = data.getClass();
		
		long value;
		int length;
		if(Byte.class == dataType)
		{
			length = 1;
			value = ((Byte)data).byteValue();
		}
		else
		{
			if(Short.class == dataType)
			{
				length = 2;
				value = ((Short)data).shortValue();
			}
			else
			{
				if(Integer.class == dataType)
				{
					length = 4;
					value = ((Integer)data).intValue();
				}
				else
				{
					if(Long.class == dataType)
					{
						length = 8;
						value = ((Long)data).longValue();
					}
					else
						throw new IllegalArgumentException("Parameter must be one of the following types:\n Byte, Short, Integer, Long");
				}
			}
		}
		
		byte[] byteArray = new byte[length];
		
		for (int i = 0; i < length; i++) 
			byteArray[i] = ((byte)(int)(value >> 8 * (length - i - 1) & 0xFF));
		
		return byteArray;
	}
	
	/**
	 * 
	 * @param frame
	 * @throws IOException 
	 */
	public void write(final CloseFrame frame) throws IOException
	{
		out.write(new byte[]
		{
			(byte) OPCODE_CLOSE, (byte) 0x02,
		    (byte) ((frame.getStatus() & MASK_LOW_WORD_HIGH_BYTE) >> OCTET_ONE),
		    (byte) (frame.getStatus() & MASK_LOW_WORD_LOW_BYTE)
		});	
	}
	
	@Deprecated
	public void ping(final PingFrame frame)
	{
		
	}
	
	@Deprecated
	public void pong(final PongFrame frame) throws IOException
	{
		out.write(new byte[]
		{
			(byte) OPCODE_PONG, 
			(byte) (frame.getSize()) 	
		});
	}

	/**
	 * Must send a Closing frame before closing stream.
	 * @see {@link #write(CloseFrame)}
	 */
	@Override
	public void close() throws IOException
	{
		out.close();
	}
}
