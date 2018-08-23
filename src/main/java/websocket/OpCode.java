package websocket;

/**
 * TODO javadoc for OpCode.
 * @author Ryan Mayobre
 * @see <a href="https://tools.ietf.org/html/rfc6455#section-11.8">RFC 6455, Section 11.8 (WebSocket Opcode Registry)</a>
 */
public enum OpCode 
{   
    /**
     * OpCode for a Continuation Frame
     */
    CONTINUATION((byte)0x00),
    
    /**
     * OpCode for a Text Frame
     */
    TEXT((byte)0x01),
    
    /**
     * OpCode for a Binary Frame
    */
    BINARY((byte)0x02),
    
    /**
     * OpCode for a Close Frame
     */
    CLOSE((byte)0x08),
    
    /**
     * OpCode for a Ping Frame
     */
    PING((byte)0x09),
    
    /**
     * OpCode for a Pong Frame
     */
    PONG((byte)0x0A);
	
	/**
	 * Byte code of current OpCode.
	 */
	private final byte CODE;
	
	/**
	 * 
	 * @param code
	 */
	OpCode(byte code)
	{
		this.CODE = code;
	}
	
	/**
	 * 
	 * @return
	 */
	public byte getCode()
	{
		return CODE;
	}
	
	/**
	 * Find the OpCode by byte.
	 * @param code - OpCode in byte.
	 * @return {@link OpCode}
	 * @throws InvalidFrameException Thrown if OpCode was not recognized.
	 */
	public static OpCode find(byte code) throws InvalidFrameException
	{
		if(code == CONTINUATION.CODE)
			return CONTINUATION;
		if(code == TEXT.CODE)
			return TEXT;
		if(code == BINARY.CODE)
			return BINARY;
		if(code == CLOSE.CODE)
			return CLOSE;
		if(code == PING.CODE)
			return PING;
		if(code == PONG.CODE)
			return PONG;
		else
			throw new InvalidFrameException("Invalid OpCode found inside of frame - " + code);
	}
}
