package websocket;

public class PongFrame extends ControlFrame
{

	PongFrame(PingFrame ping)
	{
		super(OpCode.PONG);
	}

}
