package websocket;

import java.io.Closeable;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.Socket;
import java.nio.ByteBuffer;

/**
 * WebSocketSession is the connection between the client and server endpoints.
 * 
 * @author Ryan Mayobre
 *
 */
public class WebSocketSession implements Runnable, Closeable
{
	/**
	 * Websocket connection to client.
	 */
	private final WebSocket client;
	
	/**
	 * Key provided from client's request.
	 */
	private final String key;
	
	/**
	 * Callback interface for websocket.
	 */
	private final WebSocketListener listener;
	
	/**
	 * 
	 * @param client
	 * @param key
	 * @param listener
	 * @throws IOException 
	 */
	public WebSocketSession(Socket client, String key, WebSocketListener listener) throws IOException 
	{
		this.client = new WebSocket(client);
		this.key = key;
		this.listener = listener;
	}

	@Override
	public void run() 
	{
		try 
		{
			client.performHandshake(key);
			
			listener.WebSocketOpen(this);
			
			while(!client.isClosed())
			{
				try 
				{ 
					Frame frame = client.read();
					switch(frame.getOpcode())
					{
						case TEXT:
							try 
							{
								listener.WebSocketMessage(this, frame.getPayload().toString());
							} 
							catch (IOException e) 
							{
								listener.WebSocketError(this, e);
							}
							break;
						case BINARY:
							try 
							{
								listener.WebSocketBinaryMessage(this, frame.getPayload().toByteArray());
							} 
							catch (IOException e) 
							{
								listener.WebSocketError(this, e);
							}
							break;
						case CLOSE:
							try 
							{
								int size = frame.getSize();
								if(size == 0)
									listener.WebSocketClose(this, CloseFrame.NO_CODE);
								else
								{
									int status = ByteBuffer.wrap(frame.getPayload().toByteArray()).getShort();
									listener.WebSocketClose(this, status);	
								}
							} 
							catch (IOException e) 
							{
								listener.WebSocketError(this, e);
							}
							break;
							/*
							 * Ping and pong are currently not implemented.
							 */
//						case PING:
//							break;
//						case PONG:
//							break;
						default:
							listener.WebSocketError(this, new InvalidFrameException("Invalid OpCode found in frame - " + frame.getOpcode().getCode()));
							break;
					}
					
				} 
				catch (InvalidFrameException | WebSocketException e) 
				{
					listener.WebSocketError(this, e);
				}
			}
		} 
		catch (WebSocketException e)
		{
			listener.WebSocketError(this, e);
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
			client.send(new TextFrame(message));
		} 
		catch (UnsupportedEncodingException 
				| InvalidFrameException 
				| WebSocketException e) 
		{
			listener.WebSocketError(this, e);
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
			client.send(new BinaryFrame(data));
		}
		catch (InvalidFrameException | WebSocketException e)
		{
			listener.WebSocketError(this, e);
		}
	}
	
	/**
	 * @throws IOException 
	 * 
	 */
	public void close() throws IOException
	{
		try 
		{
			client.send(new CloseFrame());
			client.close();
		} 
		catch (InvalidFrameException | WebSocketException e) 
		{
			listener.WebSocketError(this, e);
		}
	}
	
	/**
	 * @throws IOException 
	 * 
	 */
	public void close(int status) throws IOException
	{
		try 
		{
			client.send(new CloseFrame(status));
			client.close();
		}
		catch (InvalidFrameException | WebSocketException e) 
		{
			listener.WebSocketError(this, e);
		}
	}
}
