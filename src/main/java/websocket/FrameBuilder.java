package websocket;

import java.io.IOException;
import java.io.InputStream;

/**
 * Builds a websocket frame containing a server-to-client or 
 * client-to-server message following websocket protocol.
 * Provide an {@link InputStream} to read frames sent by client-to-server.
 * Provide a {@link OpCode} and a {@link String} message to create a frame
 * that will be sent server-to-client.
 * 
 * @author Ryan Mayobre
 * @see {@link Frame}
 * @see {@link FrameData}
 * @see <a href="https://tools.ietf.org/html/rfc6455#section-5.2">RFC 6455, Section 5.2 (Base Framing Protocol)</a>
 */
public class FrameBuilder implements FrameData
{
	/**
	 * Frame being built by the FrameBuilder
	 * @see {@link Frame}
	 */
	private Frame FRAME;
	
	/**
	 * Construct a frame from a {@link InputStream}
	 * @param input - InputStream from the client socket.
	 * @param test - REMOVE THIS ARGUMENT WHEN DONE
	 * @throws IOException 
	 * @throws InvalidFrameException 
	 */
	FrameBuilder(InputStream input) throws IOException, InvalidFrameException
	{
		this.FRAME = build(input, new Frame(input.read(), input.read()));
	}
	
	private Frame build(InputStream input, Frame current) throws IOException, InvalidFrameException
	{
		/*
		 * There are two valid sizes to a payload.
		 * If the size does not match the provided 
		 * byte size, throw exception.
		 */
		if(current.PAYLOAD_LENGTH == PAYLOAD_LENGTH_16)
		{
			current.PAYLOAD_LENGTH = 0;
			byte[] payload = new byte[TWO_BYTE_FRAME];
			input.read(payload);
			
			for(int i = 0; i < payload.length; i++)
				current.PAYLOAD_LENGTH = (current.PAYLOAD_LENGTH << 8) + (payload[i] & 0xFF);
		}
		else if (current.PAYLOAD_LENGTH == PAYLOAD_LENGTH_64)
		{
			current.PAYLOAD_LENGTH = 0;
			byte[] payload = new byte[EIGHT_BYTE_FRAME];
			input.read(payload);
			
			for(int i = 0; i < payload.length; i++)
				current.PAYLOAD_LENGTH = (current.PAYLOAD_LENGTH << 8) + (payload[i] & 0xFF);
		}
		/*
		 * Check for proper masking.
		 */
		byte[] maskingKey = new byte[MASK_BYTES];
		/*
		 * Check if client sent a masked frame.
		 */
		if(current.getMask())
			input.read(maskingKey);
		else
			throw new InvalidFrameException("Client did not send a masked frame.");
		/*
		 * Gather payload data.
		 */
		byte[] payload = new byte[current.PAYLOAD_LENGTH];
		input.read(payload);
		/*
		 * Check again if masked.
		 */
		if(current.getMask())
			for(int i = 0; i < current.PAYLOAD_LENGTH; i++)
				current.getPayload().write((char)(payload[i] ^ maskingKey[(i % 4)]));
		else
			throw new InvalidFrameException("Client did not send a masked frame.");
		
		/*
		 * Determine if this frame is the final fragment of the message.
		 * If FALSE, recursively call this function with the next frame.
		 */
		if(!current.getFIN())
			current.NEXT = build(input, new Frame(input.read(), input.read()));
		
		return current;
	}
	
	/**
	 * TODO get rid of and place somewhere else.
	 * @return Message encoded inside of frame from client.
	 * @see {@link Frame#PAYLOAD_DATA}, {@link Frame#NEXT_FRAME}
	 */
	public String getMessage()
	{
		if(this.FRAME == null)
			return "NO FRAME EXISTS!!!";
		else if(this.FRAME.NEXT == null)
			return new String(this.FRAME.getPayload().toByteArray());
		else
		{
			String msg = "";
			Frame current = FRAME;
			do
			{
				msg = msg + new String(current.getPayload().toByteArray());
				current = current.NEXT;
			}
			while(!(current.NEXT == null));
			
			return msg;
		}
	}
}
