package ftp;

public interface FTPListener 
{
	/**
	 * Callback for when client connects to server.
	 */
	void onConnect();
	
	/**
	 * Callback for client's disconnection.
	 */
	void onDisconnect();
}
