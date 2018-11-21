package com.httpdroid.library.websocket;

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
	void WebSocketOpen(Session session);
	
	/**
	 * Callback when client sends messages to server.
	 * @param data
	 */
	void onMessage(Session session, String message);
	
	/**
	 * Callback when
	 * @param session
	 * @param data
	 */
	void onMessage(Session session, byte[] data);
	
	/**
	 * Callback when an error occurs with server or client.
	 * @param session
	 * @param ex
	 */
	void onError(Session session, Exception e);
	
	/**
	 * Callback for when client sends a close request.
	 * @param session
	 * @param status
	 */
	void onClose(Session session, int status);
}
