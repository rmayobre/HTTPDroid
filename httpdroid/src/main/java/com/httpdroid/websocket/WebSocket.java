package com.httpdroid.websocket;

/**
 * Provided interface for any kind of WebSocket type.
 * @param <T> Any form on endpoint for the socket to connect to. (e.g {@link java.net.URL} or {@link java.net.URI})
 */
public interface WebSocket<T> extends Runnable{
    /**
     * Perform handshake with client connection. Once this method is called, the WebSocket is then
     * in the {@link ReadyState#CONNECTING} state. Initialize WebSocket connection by providing an
     * endpoint object with the hostname and port number of the endpoint. This should do nothing if
     * the ReadyState is not {@link ReadyState#CLOSED}.
     * @param endpoint form of connection to desired endpoint. (e.g. "127.0.0.1:80")
     * @see <a href="https://tools.ietf.org/html/rfc6455#section-4.2.2">RFC 6455, Section 4.2.2 (Sending the Server's Opening Handshake)</a>
     */
    void open(T endpoint);

    /**
     * Send a text frame to endpoint.
     * @param message text being sent to endpoint.
     */
    void send(String message);

    /**
     * Send a binary frame to endpoint.
     * @param data Raw data to be sent to endpoint.
     */
    void send(byte[] data);

    /**
     * Send a close frame with no closing code provided.
     */
    void close();

    /**
     * Send a close frame with provided closing code.
     * @param code closing code for frame.
     */
    void close(@WebSocketException.CloseCode int code);
}
