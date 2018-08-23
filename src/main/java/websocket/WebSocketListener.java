package websocket;

/**
 * Call back interface for a websocket server.
 * @author Ryan Mayobre
 *
 */
public interface WebSocketListener 
{
	/**
	 * Callback for when a client requests connection.
	 * @param session
	 */
	void WebSocketOpen(WebSocketSession session);
	
	/**
	 * Callback when client sends messages to server.
	 * @param data
	 */
	void WebSocketMessage(WebSocketSession session, String message);
	
	/**
	 * 
	 * @param session
	 * @param data
	 */
	void WebSocketBinaryMessage(WebSocketSession session, byte[] data);
	
	/**
	 * Callback when an error occurs with server or client.
	 * @param session
	 * @param ex
	 */
	void WebSocketError(WebSocketSession session, Exception e);
	
	/**
	 * Callback for when client sends a close request.
	 * @param session
	 * @param status
	 */
	void WebSocketClose(WebSocketSession session, int status);
}
