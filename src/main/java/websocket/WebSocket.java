package websocket;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

/**
 * This class creates the WebSocket protocols presented
 * in the RFC 6455 guidelines. A WebSocket must be created
 * for each session created when a client connects to the 
 * server.
 * @author Ryan Mayobre
 *
 */
public class WebSocket implements FrameData
{
	/**
	 * Required to create a magic string and shake hands with client.
	 */
	private final String MAGIC_KEY = "258EAFA5-E914-47DA-95CA-C5AB0DC85B11";
	
	/**
	 * Socket connection from client.
	 */
	private final Socket CLIENT;
	
	/**
	 * InputStream received from client.
	 */
	private final InputStream IN;
	
	/**
	 * Outputstream sent by server.
	 */
	private final OutputStream OUT;
	
	/**
	 * Determines if handshake was performed with client.
	 */
	private boolean HANDSHAKE;
	
	/**
	 * Determines if WebSocket is closed.
	 */
	private boolean CLOSED;
	
	/**
	 * Default constructor.
	 * @param socket
	 * @throws IOException
	 */
	WebSocket(final Socket client) throws IOException
	{
		this.CLIENT = client;
		this.IN = CLIENT.getInputStream();
		this.OUT = CLIENT.getOutputStream();
		this.HANDSHAKE = false;
		this.CLOSED = false;
	}
	
	/**
	 * 
	 * @param frame - You CANNOT send a {@link Frame} class in this method.
	 * Valid classes to place in parameters: {@link TextFrame}, 
	 * {@link BinaryFrame}, {@link CloseFrame}, {@link PongFrame}.
	 * @throws InvalidFrameException Will be thrown if frame is a {@link PingFrame}.
	 * @throws WebSocketException 
	 * @throws IOException 
	 * @see {@link #sendData(Frame)}
	 * @see {@link #sendControl(Frame)}
	 */
	public void send(Frame frame) throws InvalidFrameException, WebSocketException
	{
		if(this.CLOSED)
			throw new WebSocketException("Client side socket is closed.");
		else if(!this.HANDSHAKE)
		{
//			sendClose(new CloseFrame(CloseFrame.NEVER_CONNECTED));
			throw new WebSocketException("Handshake was never established.");
		}
		else
		{
			if(frame.isControlFrame())
				sendControl((ControlFrame) frame);
			else if (frame.isDataFrame())
			{
				try 
				{
					sendData((DataFrame) frame);
				} 
				catch (IOException e) 
				{
					throw new WebSocketException("Connection error", e);
				}
			}
			else
				throw new InvalidFrameException("Must send a valid frame.");
		}
	}
	
	/**
	 * Sends a {@link DataFrame} to client.
	 * @param frame - data frame to be sent to client.
	 * @throws IOException Thrown by {@link OutputStream}
	 */
	private void sendData(DataFrame frame) throws IOException
	{
		OUT.write(129);
		
		if(frame.getData().length <= 125)
		{
			OUT.write(frame.getData().length);
			OUT.write(frame.getData());
		}
		else if(frame.getData().length < Math.pow(2.0D, 16.0D))
		{
			OUT.write(TWO_BYTE_FRAME);
			byte[] lenBytes = this.toByteArray(Short.valueOf((short) frame.getData().length));
			OUT.write(lenBytes);
			OUT.write(frame.getData());
		}
		else
		{
			OUT.write(EIGHT_BYTE_FRAME);
			byte[] lenBytes = this.toByteArray(Long.valueOf(frame.getData().length));
			OUT.write(lenBytes);
			OUT.write(frame.getData());
		}
	}
	
	/**
	 * Takes in a number (Long or short) and creates a byte array.
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
	 * Sends a {@link ControlFrame} to client.
	 * @param frame {@link ControlFrame}
	 * @throws InvalidFrameException Thrown if {@link PingFrame} because the server should never throw a ping to client.
	 * @throws WebSocketException Thrown from {@link CloseFrame}.
	 */
	private void sendControl(ControlFrame frame) throws InvalidFrameException, WebSocketException
	{
		if(frame instanceof CloseFrame)
			sendClose((CloseFrame)frame);
//		else if(frame instanceof PongFrame)
//			sendPong((PongFrame)frame);
		else
			throw new InvalidFrameException("Server cannot send Ping frames to client.");
	}
	
	/**
	 * TODO finish pong response.
	 * Sends a pong response to client.
	 * @param frame {@link PongFrame}
	 */
	@Deprecated
	private void sendPong(PongFrame frame)
	{
		
	}
	
	/**
	 * Sends a {@link CloseFrame} to client containing a status code.
	 * @param frame - MUST be a {@link CloseFrame} for this function to work!
	 * @throws WebSocketException Thrown by {@link OuputStream} because of an 
	 * {@link IOException} if stream is closed or corrupted.
	 */
	private void sendClose(CloseFrame frame) throws WebSocketException
	{
		try 
		{
			OUT.write(new byte[]
			{
				(byte) OpCode.CLOSE.getCode(), (byte) 0x02,
			    (byte) ((frame.getStatus() & MASK_LOW_WORD_HIGH_BYTE) >> OCTET_ONE),
			    (byte) (frame.getStatus() & MASK_LOW_WORD_LOW_BYTE)
			});
		}
		catch (IOException e) 
		{
			throw new WebSocketException ("Connection error", e);
		}
	}
	
	/**
	 * Reads incoming stream of data from {@link Socket} connection.
	 * @return {@link Frame}
	 * @throws InvalidFrameException Thrown from {@link #build(Frame)} or stream could not be read.
	 * @throws WebSocketException Thrown if handshake was never established.
	 */
	public Frame read() throws InvalidFrameException, WebSocketException
	{
		if(HANDSHAKE)
		{
			try 
			{
				return build(new Frame(IN.read(), IN.read()));
			} 
			catch (IOException e)
			{
				throw new InvalidFrameException("Could not read stream from client.", e);
			}
		}
		else
			throw new WebSocketException("Handshake has never been established.");
	}
	
	/**
	 * TODO change up to return an extension of Frame class.
	 * Builds a {@link Frame} or a link of {@link Frame}s, depending on size of data being streamed.
	 * @param current - {@link Frame} being built. Uses recursion to build extending {@link Frame}s if needed.
	 * @return {@link Frame}
	 * @throws InvalidFrameException Is thrown if client's frame was not properly built.
	 * @throws IOException Thrown if stream of data is broken or corrupted.
	 */
	private Frame build(Frame current) throws InvalidFrameException, IOException
	{
		if(current.PAYLOAD_LENGTH == PAYLOAD_LENGTH_16)
		{
			current.PAYLOAD_LENGTH = 0;
			byte[] payload = new byte[TWO_BYTE_FRAME];
			IN.read(payload);
			
			for(int i = 0; i < payload.length; i++)
				current.PAYLOAD_LENGTH = (current.PAYLOAD_LENGTH << 8) + (payload[i] & 0xFF);
		}
		else if (current.PAYLOAD_LENGTH == PAYLOAD_LENGTH_64)
		{
			current.PAYLOAD_LENGTH = 0;
			byte[] payload = new byte[EIGHT_BYTE_FRAME];
			IN.read(payload);
			
			for(int i = 0; i < payload.length; i++)
				current.PAYLOAD_LENGTH = (current.PAYLOAD_LENGTH << 8) + (payload[i] & 0xFF);
		}
		
		byte[] maskingKey = new byte[MASK_BYTES];
		
		if(current.getMask())
			IN.read(maskingKey);
		else
			throw new InvalidFrameException("Client did not send a masked frame.");
		
		byte[] payload = new byte[current.PAYLOAD_LENGTH];
		IN.read(payload);
		
		if(current.getMask())
			for(int i = 0; i < current.PAYLOAD_LENGTH; i++)
				current.getPayload().write((char)(payload[i] ^ maskingKey[(i % 4)]));
		else
			throw new InvalidFrameException("Client did not send a masked frame.");
		
		/*
		 * Determine if this frame is the final fragment of the message.
		 * If FALSE, recursively call this function to get next frame.
		 */
		if(!current.getFIN())
			current.NEXT = build(new Frame(IN.read(), IN.read()));
		
		return current;
	}
	
	/**
	 * Perform handshake with client connection.
	 * 
	 * @param key - The key given by client upon request.
	 * @throws WebSocketException Thrown when handshake could not be performed.
	 * @see <a href="https://tools.ietf.org/html/rfc6455#section-4.2.2">RFC 6455, Section 4.2.2 (Sending the Server's Opening Handshake)</a>
	 */
	public void performHandshake(String key) throws WebSocketException
	{
		if(!HANDSHAKE)
		{
			try 
			{
				OUT.write(("HTTP/1.1 101 Switching Protocols\r\n").getBytes());
				OUT.write(("Upgrade: websocket\r\n").getBytes());
				OUT.write(("Connection: Upgrade\r\n").getBytes());
				OUT.write(("Sec-WebSocket-Accept: " + this.getAcceptKey(key)).getBytes());
				OUT.write(("\r\n\r\n").getBytes());
				this.HANDSHAKE = true;
			} 
			catch (IOException | NoSuchAlgorithmException e) 
			{
				throw new WebSocketException("Could not perform handshake with client.", e);
			}	
		}
		else
			throw new WebSocketException("Handshake is already established.");
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
	 * Determine if server performed WebSocket Handshake
	 * @return TRUE if handshake was performed successfully, otherwise, FALSE.
	 */
	public boolean shookHands()
	{
		return this.HANDSHAKE;
	}
	
	/**
	 * Normal disconnection from client. This sends the client a {@link CloseFrame}
	 * containing a {@link CloseFrame#NORMAL} status code.
	 * @throws WebSocketException Sent from {@link #sendClose(CloseFrame)}
	 */
	public void disconnect() throws WebSocketException
	{
		sendClose(new CloseFrame());
		close();
	}
	
	/**
	 * Proper disconnection from client. This will send the client a {@link CloseFrame}
	 * containing a status code.
	 * 
	 * @param status - Status code to be placed inside of {@link CloseFrame}
	 * @throws IOException 
	 * @throws WebSocketException 
	 * @see {@link CloseFrame}
	 * @see <a href="https://tools.ietf.org/html/rfc6455#section-1.4">RFC 6455, Section 1.4 (Closing Handshake)</a>
	 */
	public void disconnect(int status) throws WebSocketException
	{
		sendClose(new CloseFrame(status));
		close();
	}
	
	/**
	 * <p> 
	 * TO PERFORM A PROPER DISCONNECTION FROM CLIENT USE: 
	 * <br>
	 * {@link #disconnect()} or {@link #disconnect(int)} 
	 * 
	 * <p>
	 * Closes {@link Socket} connection from client, as well as {@link InputStream}
	 * and {@link OutputStream} of data from client.
	 * 
	 * @throws WebSocketException Could not properly close socket or streams from client connection.
	 */
    private final synchronized void close() throws WebSocketException
    {
    	try
    	{
    		this.CLOSED = true;
    		this.CLIENT.close();
    		this.IN.close();
    		this.OUT.close();
    	}
    	catch (IOException e)
    	{
    		throw new WebSocketException("Connection error", e);
    	}
    }
    
    /**
     * Determine if WebSocket is closed.
     * @return TRUE if WebSocket is closed, otherwise, FALSE.
     */
    public boolean isClosed()
	{
		return this.CLOSED;
	}
}

