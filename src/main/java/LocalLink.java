import websocket.WebSocketServer;

public interface LocalLink 
{
	/**
	 *  Default WebSocket port number.
	 */
	final int WEBSOCKET_PORT = 8080;
	
	/**
	 * Secure Shell port number.
	 */
	final int SSH_PORT = 22;
	
	/**
	 * File Transport Protocol port number.
	 */
	final int FTP_PORT = 21;
	
	/**
	 * File Transport Protocol Secure port number.
	 */
	final int FTPS_PORT = 990;
	
	/**
	 * Start a LocalServer.
	 * 
	 * @param localServer - LocalServer to be started.
	 * @param thread - Thread the LocalServer will run on.
	 * @see {@link LocalServer}, {@link Thread}
	 */
	void start(WebSocketServer webSocketServer, Thread thread);
	
	/**
	 * Safely shuts down a LocalServer as well as the
	 * thread the LocalServer is running on.
	 * 
	 * @param localServer - The current LocalServer running.
	 * @param thread - Thread the LocalServer is running on.
	 * @see {@link LocalServer}, {@link Thread}
	 */
	void stop(WebSocketServer webSocketServer, Thread thread);
}
