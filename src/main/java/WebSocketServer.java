


import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

import http.BadRequestException;
import http.Request;
import websocket.WebSocketListener;
import websocket.WebSocketSession;

/**
 * TODO javadoc WebSocketServer
 * @author Ryan Mayobre
 *
 */
public abstract class WebSocketServer implements Runnable, WebSocketListener
{	
	/**
	 * 
	 */
	private final ServerSocket SERVER;
	
	/**
	 * 
	 */
	private InputStream IN;

	/**
	 * 
	 */
//	private OutputStream OUT;
	
	/**
	 * Determines if the server is listening to the network.
	 */
	private boolean LISTENING;
	
	public WebSocketServer(int port) throws IOException 
	{
		SERVER = new ServerSocket(port);
	}

	/**
	 * 
	 */
	@Override
	public void run()
	{
		LISTENING = true;
		
		while(LISTENING)
		{
			try 
			{
				Socket client = SERVER.accept();
				IN = client.getInputStream();
//				OUT = client.getOutputStream();
				/*
				 * Determine if connection is an upgrade
				 * to WebSocket.
				 */
				Request client_request = new Request(IN);
				
				if(client_request.isWebSocketUpgrade())
				{
					/*
					 * TODO properly name threads based on sessions.
					 */
					WebSocketSession session = new WebSocketSession(client, client_request.getKey(), this);
					Thread WebSocketThread = new Thread(session);
//					WebSocketThread.setName(name);
					WebSocketThread.start();
				}
				/*
				 * TODO implement basic http request.
				 */
				else
				{
					// TODO setup a proper status response to errors.
					client.close();
					throw new BadRequestException("404 - Bad Request");
				}
			} 
			catch (IOException | BadRequestException e) 
			{
				e.printStackTrace();
			}
		}
	}
	
//	@Override
//	public void WebSocketOpen(WebSocketSession session)
//	{
//		
//	}
//	
//	@Override
//	public void WebSocketMessage(WebSocketSession session, String message)
//	{
//		
//	}
//	
//	@Override
//	public void WebSocketBinaryMessage(WebSocketSession session, byte[] data)
//	{
//		
//	}
//	
//	@Override
//	public void WebSocketError(WebSocketSession session, Exception e)
//	{
//		
//	}
//	
//	@Override
//	public void WebSocketClose(WebSocketSession session, int status)
//	{
//		
//	}
	
	public void shutdown()
	{
		LISTENING = false;
	}

}
