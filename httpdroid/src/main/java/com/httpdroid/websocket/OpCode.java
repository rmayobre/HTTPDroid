package com.httpdroid.websocket;

import android.support.annotation.IntDef;

import java.lang.annotation.Retention;

import static java.lang.annotation.RetentionPolicy.SOURCE;

/**
 * Declaration of a frame type. OpCodes are used to specify what kind of
 * data or control is in the frame.
 * @see <a href="https://tools.ietf.org/html/rfc6455#section-11.8">RFC 6455, Section 11.8 (WebSocketIOTemp Opcode Registry)</a>
 */
@IntDef({
        OpCode.CONTINUATION,
        OpCode.TEXT,
        OpCode.BINARY,
        OpCode.CLOSE,
        OpCode.PING,
        OpCode.PONG
})
@Retention(SOURCE)
public @interface OpCode {
    /** WebSocket OpCode for a Continuation Frame */
    int CONTINUATION = ((byte)0x00);
    /** WebSocket OpCode for a Text Frame */
    int TEXT = ((byte)0x01);
    /** WebSocket OpCode for a Binary Frame */
    int BINARY = ((byte)0x02);
    /** WebSocket OpCode for a Close Frame */
    int CLOSE = ((byte)0x08);
    /** WebSocket OpCode for a Ping Frame */
    int PING = ((byte)0x09);
    /** WebSocket OpCode for a Pong Frame */
    int PONG = ((byte)0x0A);
}
