package com.httpdroid.websocket;

import android.support.annotation.IntDef;

import java.lang.annotation.Retention;

import static java.lang.annotation.RetentionPolicy.SOURCE;

@IntDef({
        ReadyState.OPEN,
        ReadyState.CONNECTING,
        ReadyState.CLOSING,
        ReadyState.CLOSED
})
@Retention(SOURCE)
public @interface ReadyState {
    /** Connection is open and ready for communication.*/
    int OPEN = 0;
    /** Connection is getting ready to open, but still not open.*/
    int CONNECTING = 1;
    /** Connection is in process of closing.*/
    int CLOSING = 2;
    /** Connection is closed or couldn't be opened.*/
    int CLOSED = 3;
}