package com.httpdroid.websocket.exception;


/**
 * Exception when Websocket connection failed to perform TLS opening handshake.
 * @author Ryan Mayobre
 * @see <a href="https://tools.ietf.org/html/rfc6455#section-4">RFC 6455, Section 4 Opening Handshake</a>
 */
public class HandshakeException extends WebSocketException {

	private static final long serialVersionUID = 7834921797559816418L;

	public HandshakeException(String message) {
		super(message, CloseCode.TLS_ERROR);
	}
	
	public HandshakeException(String message, Exception e) {
		super(message, e, CloseCode.TLS_ERROR);
	}
}
