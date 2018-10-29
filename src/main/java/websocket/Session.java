package websocket;

import java.io.IOException;
import java.net.Socket;

/**
 * WebSocketSession is the connection between the client and server endpoints.
 * 
 * @author Ryan Mayobre
 *
 */
public class Session extends WebSocket implements Runnable
{	
	/**
	 * Callback for Websocket events.
	 */
	private final WebSocketListener listener;
	
	/**
	 * 
	 */
	private final String clientIP;
	
	Session(Socket client, WebSocketListener listener) throws IOException
	{
		super(client);
		this.listener = listener;
		clientIP = client.getRemoteSocketAddress().toString();
	}
	
	@Override
	public void run() 
	{
		while (this.isOpen())
		{
			
		}
	}
	
	@Override
	protected void onOpen() 
	{
		
	}

	@Override
	protected void onMessage(String message) 
	{
		
	}

	@Override
	protected void onError(WebSocketException e) 
	{
		
	}

	@Override
	protected void onClose(int status) 
	{
//		if (this.)
		{
			try {
				close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		listener.WebSocketClose(this, status);
	}
}
