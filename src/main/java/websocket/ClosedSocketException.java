package websocket;

public class ClosedSocketException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 668664820697266611L;
	
	public ClosedSocketException(String message)
	{
		super(message);
	}

}
