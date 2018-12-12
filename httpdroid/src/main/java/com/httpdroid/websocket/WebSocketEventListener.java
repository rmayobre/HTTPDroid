package com.httpdroid.websocket;

public interface WebSocketEventListener {
    void onOpen();
    void onMessage(String message);
    void onBinary(byte[] data);
//    void onPing(byte[] data); TODO implement ping and pong.

    /**
     * WebSocket will call this method when an error occurs while using the WebSocket. Upon this
     * callback, the WebSocket's ReadyState will be set to {@link ReadyState#CLOSING} and then begin
     * the closing process.
     *
     * @param ex Record of the breach in WebSocket protocol.
     * @see WebSocketException#getCode()
     */
    void onError(WebSocketException ex);
    void onClose(@WebSocketException.CloseCode int status);
}
