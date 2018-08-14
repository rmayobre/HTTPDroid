package websocket;

import java.io.Closeable;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.Socket;

/**
 * TODO Review all try-catch's and determine how to properly handle them.
 * 
 * @author Ryan Mayobre
 *
 */
public class WebSocketSession implements Runnable, Closeable
{
	/**
	 * Websocket connection to client.
	 */
	private final WebSocket CLIENT;
	
	/**
	 * Key provided from client's request.
	 */
	private final String KEY;
	
	/**
	 * Callback interface for websocket.
	 */
	private final WebSocketListener LISTENER;
	
	/**
	 * 
	 * @param client
	 * @param key
	 * @param listener
	 * @throws IOException 
	 */
	public WebSocketSession(Socket client, String key, WebSocketListener listener) throws IOException 
	{
		this.CLIENT = new WebSocket(client);
		this.KEY = key;
		this.LISTENER = listener;
	}

	@Override
	public void run() 
	{
		/*
		 * Perform handshake...
		 */
		try 
		{
			CLIENT.performHandshake(KEY);
		} 
		catch (WebSocketException e)
		{
			// TODO Log this.
			LISTENER.onError(this, e);
		}
		
		while(!CLIENT.isClosed())
		{
			try 
			{
				Frame frame = CLIENT.read();
			} 
			catch (InvalidFrameException | WebSocketException e) 
			{
				// TODO Log this.
				e.printStackTrace();
			}
			
		}
	}
	
	/**
	 * Send string message to client.
	 * @param message
	 */
	public void send(String message)
	{
		try 
		{
			CLIENT.send(new TextFrame(message));
		} 
		catch (UnsupportedEncodingException 
				| InvalidFrameException 
				| WebSocketException e) 
		{
			// TODO Log this.
			e.printStackTrace();
		}
	}
	
	/**
	 * Send an array of bytes to client.
	 * @param data
	 */
	public void send(byte[] data)
	{
		try 
		{
			CLIENT.send(new BinaryFrame(data));
		}
		catch (InvalidFrameException | WebSocketException e)
		{
			// TODO Log this
			e.printStackTrace();
		}
	}
	
	/**
	 * Close the WebSocket connection to client and return
	 * a closing frame with a status code.
	 * @param status
	 */
	public void close(int status)
	{
		try 
		{
			CLIENT.disconnect(status);
		}
		catch (WebSocketException e) 
		{
			// TODO Log this.
			e.printStackTrace();
		}
	}

	/**
	 * Properly close the session and WebSocket.
	 * This will also send a close frame with a normal status code. 
	 * connection from client.
	 */
	@Override
	public void close() 
	{
		try
		{
			CLIENT.disconnect();
		} 
		catch (WebSocketException e) 
		{
			// TODO Log this.
			e.printStackTrace();
		}
	}

}
