package com.httpdroid.websocket.io;

import com.httpdroid.websocket.Frame;
import com.httpdroid.websocket.OpCode;
import com.httpdroid.websocket.ReadyState;
import com.httpdroid.websocket.WebSocket;
import com.httpdroid.websocket.WebSocketEventListener;
import com.httpdroid.websocket.WebSocketException;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.Socket;
import java.net.URL;
import java.nio.ByteBuffer;

/**
 * A WebSocket object that follows the RFC 6455 protocol. WebSocketIO is an IO connection, which
 * means that each connection (endpoint) will also be tied into a thread to handle the processing.
 * @see <a href="https://tools.ietf.org/html/rfc6455">RFC 6455 - The WebSocket Protocol</a>
 */
public class WebSocketIO implements WebSocket<URL> {

    private final WebSocketEventListener listener;

    private WebSocketOutputStreamReader out;

    private WebSocketInputStreamReader in;

    private Socket socket;

    private URL endpoint;

    @ReadyState
    private int readyState;

    public WebSocketIO(WebSocketEventListener listener) {
        this.listener = listener;
        readyState = ReadyState.CLOSED;
    }

    WebSocketIO(Socket socket, WebSocketEventListener listener) {
        this.listener = listener;
        this.socket = socket;
        try {
            in = new WebSocketInputStreamReader(socket.getInputStream());
            out = new WebSocketOutputStreamReader(socket.getOutputStream());
        } catch (IOException e) {
            close(new WebSocketException.WebSocketIOException(
                    "Could not fetch input stream or use output stream of endpoint.", e));
        }
    }

    //TODO set up thread start in the open method.


    @Override
    public void open(URL endpoint) {
        if (readyState == ReadyState.CLOSED) {
            readyState = ReadyState.CONNECTING;
            try {
                socket = new Socket(endpoint.getHost(), endpoint.getPort());
                in = new WebSocketInputStreamReader(socket.getInputStream());
                out = new WebSocketOutputStreamReader(socket.getOutputStream());
            } catch (IOException e) {
                close(new WebSocketException.WebSocketIOException(
                        "Could not fetch input stream or use output stream of endpoint.", e));
            }
            this.endpoint = endpoint;
        }
    }

    @Override
    public void send(String message) {
        try {
            out.write(new Frame(OpCode.TEXT, message.getBytes("UTF-8")));
        } catch (WebSocketException e) {
            close(e);
        } catch (UnsupportedEncodingException e) {
            close(new WebSocketException(
                    "String provided could not be converted into UTF-8 byte code.",
                    e, WebSocketException.CloseCode.NO_UTF8));
        }
    }

    @Override
    public void send(byte[] data) {
        try {
            out.write(new Frame(OpCode.BINARY, data));
        } catch (WebSocketException e) {
            close(e);
        }
    }

    @Override
    public void close() {
        readyState = ReadyState.CLOSED;
        try {
            out.write(new Frame(OpCode.CLOSE, (byte) 0x00));
            in.close();
            out.close();
            socket.close();
        } catch (WebSocketException e) {
            listener.onError(e);
        } catch (IOException e) {
            listener.onError(new WebSocketException.WebSocketIOException("Socket and in/out streams could not be close.", e));
        }
    }

    @Override
    public void close(int status) {
        readyState = ReadyState.CLOSED;
        try {
            out.write(new Frame(OpCode.CLOSE, ByteBuffer.allocate(4).putInt(status).array()));
            in.close();
            out.close();
            socket.close();
        } catch (WebSocketException e) {
            listener.onError(e);
        } catch (IOException e) {
            listener.onError(new WebSocketException.WebSocketIOException("Socket and in/out streams could not be close.", e));
        }
    }

    protected void close(WebSocketException ex) {
        readyState = ReadyState.CLOSING;
        listener.onError(ex);
        close(ex.getCode());
    }

    WebSocketOutputStreamReader getOutputStreamReader() {
        return out;
    }

    public int getReadyState() {
        return readyState;
    }

    public URL getEndpoint() {
        return endpoint;
    }
}
