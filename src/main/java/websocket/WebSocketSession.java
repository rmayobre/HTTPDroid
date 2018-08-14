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
			
		}
	}

	/**
	 * Properly close the session and WebSocket 
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
