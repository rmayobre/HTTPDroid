package com.httpdroid.websocket.io;

import com.httpdroid.websocket.WebSocketEventListener;

import java.net.Socket;

/**
 * @see <a href="https://tools.ietf.org/html/rfc6455">RFC 6455 - The WebSocket Protocol</a>
 */
public class WebSocketIOSession implements Runnable{

    private final WebSocketIO client;

    public WebSocketIOSession(WebSocketEventListener listener, Socket client, String key) {
        this.client = new WebSocketIO(listener, client, key);
    }

    @Override
    public void run() {

    }
}
