package websocket;

import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

import frame.BinaryFrame;
import frame.CloseFrame;
import frame.Frame;
import frame.PingFrame;
import frame.PongFrame;
import frame.TextFrame;

/**
 * This class creates the WebSocket protocols presented
 * in the RFC 6455 guidelines. A WebSocket must be created
 * for each session created when a client connects to the 
 * server.
 * 
 * <p>
 * If the session fails to perform a handshake between client and server,
 * disconnection all connections and streams from both client and server.
 * 
 * @author Ryan Mayobre
 *
 */
public abstract class WebSocket implements Closeable
{	
	/**
	 * 
	 */
	protected ReadyState readyState;
	
	/**
	 * 
	 */
	protected Socket client;
	
	/**
	 * 
	 */
	private WebSocketInputStream in;
	
	/**
	 * 
	 */
	private WebSocketOutputStream out;
	
	/**
	 * 
	 * @param client
	 * @throws IOException
	 */
	protected WebSocket(Socket client)
	{
		try
		{
			this.client = client;
			in = new WebSocketInputStream(client.getInputStream());
			out = new WebSocketOutputStream(client.getOutputStream());
			readyState = ReadyState.CONNECTING;
		}
		catch (IOException e)
		{
			onError(new WebSocketException("WebSocket could not be initalized.", e));
		}
	}
	
	/**
	 * Perform handshake with client connection.
	 * 
	 * @param key - The key given by client upon request.
	 * @throws WebSocketException Thrown when handshake could not be performed.
	 * @see <a href="https://tools.ietf.org/html/rfc6455#section-4.2.2">RFC 6455, Section 4.2.2 (Sending the Server's Opening Handshake)</a>
	 */
	public void open(final String key) throws WebSocketException
	{
		try 
		{
			out.openHandShake(key);
			readyState = ReadyState.OPEN;
		} 
		catch (NoSuchAlgorithmException | IOException e) 
		{
			
			onError(new WebSocketException("Could not perform opening handshake with client.", e));
		}
	}
	
	/**
	 * 
	 * @param frame
	 * @throws IOException
	 * @throws WebSocketException
	 */
	@SuppressWarnings("deprecation")
	protected void send(final Frame frame) throws IOException, WebSocketException
	{
		if (!isClosed())
		{
			if (frame instanceof TextFrame)
				out.write((TextFrame) frame);
			else if (frame instanceof BinaryFrame)
				out.write((BinaryFrame) frame);
			else if (frame instanceof CloseFrame)
				out.write((CloseFrame) frame);
			else if (frame instanceof PingFrame)
				out.ping((PingFrame) frame);
			else if (frame instanceof PongFrame)
				out.pong((PongFrame) frame);
			else
				onError(new WebSocketException("Unrecognized frame being "
											 + "sent through WebSocket."));
		}
		else
			onError(new WebSocketException("Could not send frame to client, "
									   + "Websocket is in a closing state."));
	}
	
	protected abstract void onOpen();
	protected abstract void onMessage(String message);
	protected abstract void onError(WebSocketException e);
	protected abstract void onClose(int status);
	
	/**
	 * Make sure to perform a closing handshake before closing.
	 * @throws IOException 
	 * @see {@link #close(int)}
	 */
	@Override
	public synchronized void close() throws IOException
	{
		in.close();
		out.close();
		client.close();
		readyState = ReadyState.CLOSED;
	}
	
	/**
	 * The proper way of closing the websocket is by providing
	 * the reason for closing the socket connection.
	 * @param status - reason for closing.
	 * @throws WebSocketException 
	 * @see {@link CloseFrame}
	 */
	public synchronized void close(final int status) throws WebSocketException
	{
		readyState = ReadyState.CLOSING;
		try 
		{
			send(new CloseFrame(status));
		} 
		catch (IOException e)
		{
			onError(new WebSocketException("Could not send close frame to client.", e));
		}
	}
	
	public boolean isOpen()
	{
		return readyState == ReadyState.OPEN;
	}
	
	public boolean isClosing()
	{
		return readyState == ReadyState.CLOSING;
	}
	
	public boolean isClosed()
	{
		return readyState == ReadyState.CLOSED;
	}
}

