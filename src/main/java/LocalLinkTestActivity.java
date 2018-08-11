import java.io.IOException;
import java.net.ServerSocket;

import websocket.CloseFrame;
import websocket.DataFrame;
import websocket.WebSocketListener;
import websocket.WebSocketServer;
import websocket.WebSocketSession;

/**
 * 
 * @author Ryan Mayobre
 *
 */
public class LocalLinkTestActivity implements LocalLink
{
	@SuppressWarnings("unused")
	private final String TAG = LocalLinkTestActivity.class.getSimpleName();
	
	public static void main(String[] args)
	{
		System.out.println("Now running...");
		Thread thread;
		try 
		{
			WebSocketServer webSocketServer = new WebSocketServer(new ServerSocket(WEBSOCKET_PORT), new WebSocketListener()
			{

				@Override
				public void onOpen(WebSocketSession session) {
					
				}

				@Override
				public void onMessage(WebSocketSession session,String message) {
					
				}

				@Override
				public void onError(WebSocketSession session, Exception ex) {
					
				}

				@Override
				public void onClose(WebSocketSession session, CloseFrame close) {
					
				}


			
			});
			thread = new Thread(webSocketServer);
			thread.setName("LocalLink WebSocket Listener");
			thread.start();
		} 
		catch (IOException e) 
		{
			e.printStackTrace();
		}
	}
	
	@Override
	public void start(WebSocketServer webSocketServer, Thread thread)
	{
		try 
		{
			webSocketServer = new WebSocketServer(new ServerSocket(WEBSOCKET_PORT), new WebSocketListener()
			{

				@Override
				public void onOpen(WebSocketSession session) {
					
				}

				@Override
				public void onMessage(WebSocketSession session, String message) {
					
				}

				@Override
				public void onError(WebSocketSession session, Exception ex) {
					
				}

				@Override
				public void onClose(WebSocketSession session, CloseFrame close) {
					
				}

				
			
			});
			thread = new Thread(webSocketServer);
			thread.setName("LocalLink WebSocket Listener");
			thread.start();
		} 
		catch (IOException e) 
		{
			// TODO Log this exception.
			// TODO Stop initial setup.
			e.printStackTrace();
		}
	}

	@Override
	public void stop(WebSocketServer webSocketServer, Thread thread) 
	{
		try 
		{
			webSocketServer.close();
			if(thread != null)
				thread.join();
		} 
		catch (InterruptedException | IOException e)
		{
			// TODO Log this exception.
			e.printStackTrace();
		}	
	}
	
//	/**
//	 * Default port set to 8080
//	 */
//	private final int PORT = 8080;
//	
//	private Thread serverThread;
//
//	private LocalServer localServer;
//	
//	/**
//	 * Start LocalLink server.
//	 */
//	public void start()
//	{
//		try 
//		{
//			localServer = new LocalServer(new ServerSocket(PORT));
//			serverThread = new Thread(localServer);
//			serverThread.setName("LocalLink Socket Listener");
//			serverThread.start();
//		} 
//		catch (IOException e) 
//		{
//			// TODO Log this exception.
//			// TODO Stop initial setup.
//			e.printStackTrace();
//		}
//	}
//	
//	/**
//	 * Stop LocalLink server.
//	 */
//	public void stop()
//	{
//		try 
//		{
//			localServer.close();
//			if(serverThread != null)
//				serverThread.join();
//		} 
//		catch (InterruptedException | IOException e)
//		{
//			// TODO Log this exception.
//			e.printStackTrace();
//		}
//	}
}
