package com.httpdroid.websocket.exception;

/**
 * Exception for any problems with building or using WebSocket Data frames.
 * <p>
 * Must follow the <a href="https://tools.ietf.org/html/rfc6455">RFC 6455</a> guidelines.
 * @author Ryan Mayobre
 * @see <a href="https://tools.ietf.org/html/rfc6455">RFC 6455</a>
 */
public class InvalidFrameException extends WebSocketException {

	private static final long serialVersionUID = -6571826516080525468L;
	
	public InvalidFrameException(String message) {
		super(message, CloseCode.POLICY_VALIDATION);
	}

	public InvalidFrameException(String message, Exception e) {
		super(message, e, CloseCode.POLICY_VALIDATION);
	}
}

