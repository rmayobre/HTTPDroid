package com.httpdroid.websocket;

import android.support.annotation.IntDef;

import com.httpdroid.websocket.exception.InvalidFrameException;

import java.lang.annotation.Retention;

import static java.lang.annotation.RetentionPolicy.SOURCE;

/**
 * Declaration of a frame type. OpCodes are used to specify what kind of
 * data or control is in the frame.
 *
 * @author Ryan Mayobre
 * @see <a href="https://tools.ietf.org/html/rfc6455#section-11.8">RFC 6455, Section 11.8 (WebSocket Opcode Registry)</a>
 */
public class WebSocketOpCode {
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
        /**
         * WebSocketOpCode for a Continuation Frame
         */
        int CONTINUATION = ((byte)0x00);
        /**
         * WebSocketOpCode for a Text Frame
         */
        int TEXT = ((byte)0x01);
        /**
         * WebSocketOpCode for a Binary Frame
         */
        int BINARY = ((byte)0x02);
        /**
         * WebSocketOpCode for a Close Frame
         */
        int CLOSE = ((byte)0x08);
        /**
         * WebSocketOpCode for a Ping Frame
         */
        int PING = ((byte)0x09);
        /**
         * WebSocketOpCode for a Pong Frame
         */
        int PONG = ((byte)0x0A);
    }

    /**
     * Find the WebSocketOpCode by byte.
     *
     * @param code - WebSocketOpCode in byte.
     * @return {@link WebSocketOpCode}
     * @throws InvalidFrameException Thrown if WebSocketOpCode was not recognized.
     */
    @OpCode
    public static int find(byte code) throws InvalidFrameException {
        switch (code) {
            case OpCode.CONTINUATION:
                return OpCode.CONTINUATION;
            case OpCode.TEXT:
                return OpCode.TEXT;
            case OpCode.BINARY:
                return OpCode.BINARY;
            case OpCode.CLOSE:
                return OpCode.CLOSE;
            case OpCode.PING:
                return OpCode.PING;
            case OpCode.PONG:
                return OpCode.PONG;
            default:
                throw new InvalidFrameException("Invalid WebSocketOpCode found inside of frame - " + code);
        }
    }
}
