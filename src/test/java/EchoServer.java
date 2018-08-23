import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import websocket.WebSocketSession;

public class EchoServer extends WebSocketServer
{

	public EchoServer(int port) throws IOException 
	{
		super(port);
	}

	@Override
	public void WebSocketOpen(WebSocketSession session)
	{
		String time = new SimpleDateFormat("HH.mm.ss").format(new Date());
		System.out.println("Client -  " + time + ": Successfully Connected!");
	}

	@Override
	public void WebSocketMessage(WebSocketSession session, String message)
	{
		String time = new SimpleDateFormat("HH.mm.ss").format(new Date());
		System.out.println("Client - " + time + ": " + message);
	}

	@Override
	public void WebSocketBinaryMessage(WebSocketSession session, byte[] data) 
	{
		String time = new SimpleDateFormat("HH.mm.ss").format(new Date());
		System.out.println("Binary data sent from client - " + time + ": ");
		for(int i = 0; i < data.length; i++)
			System.out.print(data[i]);
		System.out.println();
	}

	@Override
	public void WebSocketError(WebSocketSession session, Exception e)
	{
		e.printStackTrace();
	}

	@Override
	public void WebSocketClose(WebSocketSession session, int status) 
	{
		System.out.println("Session is now closing...");
		System.out.println("Closing status: " + status);
		try 
		{
			session.close(status);
		} 
		catch (IOException e) 
		{
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) 
	{
		try
		{
			System.out.println("Running...");
			EchoServer echo = new EchoServer(8080);
			Thread echoThread = new Thread(echo);
			echoThread.start();
		} 
		catch (IOException e) 
		{
			e.printStackTrace();
		}
	}

}
