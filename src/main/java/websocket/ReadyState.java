package websocket;

public enum ReadyState 
{
	/**
	 * Connection is getting ready to open, but still not open.
	 */
	CONNECTING,
	
	/**
	 * Connection is open and ready for communication.
	 */
	OPEN,
	
	/**
	 * Connection is in process of closing.
	 */
	CLOSING,
	
	/**
	 * Connection is closed or couldn't be opened.
	 */
	CLOSED;
}
