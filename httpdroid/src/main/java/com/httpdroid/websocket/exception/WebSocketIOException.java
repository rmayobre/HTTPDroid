package com.httpdroid.websocket.exception;

public class WebSocketIOException extends WebSocketException {

    private static final long serialVersionUID = 8789133978001569624L;

    public WebSocketIOException(String message) {
        super(message, CloseCode.INTERNAL_ERROR);
    }

    public WebSocketIOException(String message, Exception ex) {
        super(message, ex, CloseCode.INTERNAL_ERROR);
    }
}
