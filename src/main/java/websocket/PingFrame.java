package websocket;

public class PingFrame extends ControlFrame
{
	
	PingFrame(byte[] data)
	{
		super(OpCode.PING, data); 
	}
}
