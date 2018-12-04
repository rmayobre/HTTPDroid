package com.httpdroid.websocket.io;

import com.httpdroid.websocket.WebSocketEventListener;
import com.httpdroid.websocket.WebSocketException;

import java.net.Socket;

/**
 * @see <a href="https://tools.ietf.org/html/rfc6455">RFC 6455 - The WebSocket Protocol</a>
 */
public class WebSocketIOSession extends WebSocketIO {

    private final Socket client;

    public WebSocketIOSession(Socket client, WebSocketEventListener listener) {
        super(client, listener);
        this.client = client;
    }

    public void performHandshake(String key) {
        try {
            getOutputStreamReader().writeHandshake(key);
        } catch (WebSocketException e) {
            close(e);
        }
    }


}
