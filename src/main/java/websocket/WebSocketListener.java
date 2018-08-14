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
	public void onOpen(WebSocketSession session);
	
	/**
	 * Callback when client sends messages to server.
	 * @param data
	 */
	public void onMessage(WebSocketSession session, String message);
	
	/**
	 * Callback when an error occurs with server or client.
	 * @param session
	 * @param ex
	 */
	public void onError(WebSocketSession session, Exception ex);
	
	/**
	 * Callback for when client sends a close request.
	 * @param session
	 * @param close
	 */
	public void onClose(WebSocketSession session, CloseFrame close);
}
