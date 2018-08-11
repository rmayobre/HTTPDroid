package websocket;

/**
 * Call back interface for a websocket server.
 * @author Ryan Mayobre
 *
 */
public interface WebSocketListener 
{
	/**
	 * 
	 * @param session
	 */
	public void onOpen(WebSocketSession session);
	
	/**
	 * 
	 * @param data
	 */
	public void onMessage(WebSocketSession session, String message);
	
	/**
	 * TODO create a websocket error class containing data to error.
	 * @param session
	 * @param ex
	 */
	public void onError(WebSocketSession session, Exception ex);
	
	/**
	 * 
	 * @param session
	 * @param close
	 */
	public void onClose(WebSocketSession session, CloseFrame close);
}
