package websocket;

import java.io.ByteArrayOutputStream;
import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.Socket;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

/**
 * TODO Javadoc WebSocketSession
 * @author Ryan Mayobre
 *
 */
public class WebSocketSession implements Runnable, Closeable
{
	/**
	 * 
	 */
	private final String MAGIC_KEY = "258EAFA5-E914-47DA-95CA-C5AB0DC85B11";
	
	/**
	 * 
	 */
	private final Socket CLIENT;
	
	/**
	 * 
	 */
	private final String KEY;
	
	/**
	 * 
	 */
	private final WebSocketListener LISTENER;
	
	/**
	 * 
	 */
	private final InputStream IN;
	
	/**
	 * 
	 */
	private final OutputStream OUT;
	
	/**
	 * TODO removed this, session is closed when websocket is closed.
	 */
	private boolean CLOSED;
    
	/**
	 * 
	 */
    public static final int TWO_BYTE_FRAME = 126;
    
    /**
     * 
     */
    public static final int EIGHT_BYTE_FRAME = 127;
    
	
	/**
	 * 
	 * @param client
	 * @param key
	 * @param listener
	 * @throws IOException 
	 */
	public WebSocketSession(Socket client, String key, WebSocketListener listener) throws IOException 
	{
		this.CLIENT = client;
		this.KEY = key;
		this.LISTENER = listener;
		this.IN = CLIENT.getInputStream();
		this.OUT = CLIENT.getOutputStream();
	}

	@Override
	public void run() 
	{
		try 
		{
			/*
			 * Perform handshake...
			 */
			OUT.write(("HTTP/1.1 101 Switching Protocols\r\n").getBytes());
			OUT.write(("Upgrade: websocket\r\n").getBytes());
			OUT.write(("Connection: Upgrade\r\n").getBytes());
			OUT.write(("Sec-WebSocket-Accept: " + this.getAcceptKey()).getBytes());
			OUT.write(("\r\n\r\n").getBytes());
			
			CLOSED = false;
			/*
			 * Wait for websocket frames...
			 */
			while(!CLOSED)
			{
				/*
				 * Read frames...
				 */
			}
		} 
		catch (IOException | NoSuchAlgorithmException e) 
		{
			e.printStackTrace();
		} 
	}
	
	/**
	 * 
	 * @return
	 * @throws NoSuchAlgorithmException
	 */
	private String getAcceptKey() throws NoSuchAlgorithmException
	{
		MessageDigest message = MessageDigest.getInstance("SHA-1");
		
		String text = KEY + MAGIC_KEY;
		
		message.update(text.getBytes(), 0, text.length());
		
		return Base64.getEncoder().encodeToString(message.digest());
	}
	
	/**
	 * TODO change message from string to json.
	 * 
	 * @param message
	 * @throws IOException
	 */
	public void sendMessage(String message) throws IOException
	{
		byte[] messageBytes = message.getBytes("UTF-8");
		
		OUT.write(129);
		
		if(messageBytes.length <= 125)
		{
			OUT.write(messageBytes.length);
			OUT.write(messageBytes);
		}
		else if(messageBytes.length < Math.pow(2.0D, 16.0D))
		{
			OUT.write(TWO_BYTE_FRAME);
			byte[] lenBytes = this.toByteArray(Short.valueOf((short) messageBytes.length));
			OUT.write(lenBytes);
			OUT.write(messageBytes);
		}
		else
		{
			OUT.write(EIGHT_BYTE_FRAME);
			byte[] lenBytes = this.toByteArray(Long.valueOf(messageBytes.length));
			OUT.write(lenBytes);
			OUT.write(messageBytes);
		}
	}
	
	/**
	 * 
	 * @param data
	 * @return
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
	 * Sanitizes a user's input before being read by server.
	 * 
	 * TODO Make sanitization a class or look up a library for sanitization.
	 * 
	 * @param input
	 * @return Sanitized input.
	 */
	@SuppressWarnings("unused")
	private String sanitize(String input)
	{
		
		return input;
	}

	/**
	 * TODO extend to a proper closed process.
	 */
	@Override
	public void close() throws IOException 
	{
		CLOSED = true;
		CLIENT.close();
		IN.close();
		OUT.close();
	}

}
