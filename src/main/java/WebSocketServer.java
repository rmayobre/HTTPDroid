


import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.ServerSocket;
import java.net.Socket;

import http.Request;
import websocket.WebSocketListener;
import websocket.WebSocketSession;

/**
 * TODO javadoc WebSocketServer
 * @author Ryan Mayobre
 *
 */
public class WebSocketServer implements Runnable, Closeable
{
	/**
	 * 
	 */
	private final ServerSocket SERVER;
	
	/**
	 * 
	 */
	private WebSocketListener WEBSOCKET_LISTENER;
	
	/**
	 * 
	 */
	private InputStream IN;

	/**
	 * 
	 */
	private OutputStream OUT;
	
	/**
	 * Determine when server loop should end.
	 */
	private boolean CLOSED;
	
	public WebSocketServer(ServerSocket server, WebSocketListener listener) 
	{
		this.SERVER = server;
		this.WEBSOCKET_LISTENER = listener;
	}

	/**
	 * 
	 */
	@Override
	public void run()
	{
		CLOSED = false;
		
		while(!CLOSED)
		{
			try 
			{
				Socket client = SERVER.accept();
				IN = client.getInputStream();
				OUT = client.getOutputStream();
				/*
				 * Determine if connection is an upgrade
				 * to WebSocket.
				 */
				Request client_request = new Request(IN);
				
				if(client_request.isWebSocketUpgrade())
				{
					WebSocketSession session = new WebSocketSession(client, client_request.getKey(), WEBSOCKET_LISTENER);
					Thread WebSocketThread = new Thread(session);
//					WebSocketThread.setName(name);
					WebSocketThread.start();
				}
				/*
				 * TODO incorporate other types of requests here.
				 * 
				 * SYNTAX - client_request.<CHECKING METHOD>
				 */
				else
				{
//					throws new BadRequestException("404 - Bad Request");
					// TODO setup a proper status response to errors.
					client.close();
					IN.close();
					OUT.close();
				}
			} 
			catch (IOException e) 
			{
				// TODO Log this catch
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * Properly close any data destinations.
	 * 
	 * @param closeable - A data destination using the {@link Closeable} interface.
	 */
	private void close(Object closeable)
	{
		try 
		{
            if (closeable != null) 
            {
                if (closeable instanceof Closeable) 
                    ((Closeable) closeable).close();
                else if (closeable instanceof Socket)
                    ((Socket) closeable).close();
                else if (closeable instanceof ServerSocket)
                    ((ServerSocket) closeable).close(); 
                else
                    throw new IllegalArgumentException("Object is not closeable.");
            }
        }
		catch (IOException e) 
		{
        	// TODO log this error.
            e.printStackTrace();
        }
	}

	/**
	 * Disconnects all data destinations and ends server loop.
	 * @throws IOException
	 */
	@Override
	public void close() throws IOException 
	{
		CLOSED = true; // Marked true to end While-loop.
		close(SERVER);
		close(IN);
		close(OUT);
	}

}
