package websocket;

import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.Socket;

import frame.Frame;
import frame.InvalidFrameException;

public class WebSocketInputStream implements Closeable
{
	/**
     * Number of masking bytes provided from client.
     */
    private static final int MASK_BYTES = 0x4;
    
	/**
     * Number of bits required to shift octet 1 into the lowest 8 bits.
     */
    private static final int OCTET_ONE = 8;
    
    /**
     * Payload length indicating that the payload's true length is a
     * yet-to-be-provided unsigned 16-bit integer.
     */
    private static final int PAYLOAD_LENGTH_16 = 0x7E;
    
    /**
     * Payload length indicating that the payload's true length is a
     * yet-to-be-provided unsigned 64-bit integer (MSB = 0).
     */
    private static final int PAYLOAD_LENGTH_64 = 0x7F;
    
    /**
     * Binary number for the two byte frame.
     */
    private static final int TWO_BYTE_FRAME = 0x2;
    
    /**
     * Binary number for the eight byte frame.
     */
    private static final int EIGHT_BYTE_FRAME = 0x8;
	
    /**
     * Incoming stream of data from client's endpoint.
     */
	private final InputStream in;
	
	WebSocketInputStream(InputStream in)
	{
		this.in = in;
	}
	
	public void read() throws WebSocketException
	{
		try 
		{
			Frame frame = new Frame(in.read(), in.read());
			frame.fin = true;
			
			
		} 
		catch (IOException e) 
		{
			e.printStackTrace();
		}
		
	}
	
	private Frame build(Frame frame, int q)
	{
		if(frame.payload_length == PAYLOAD_LENGTH_16)
		{
			frame.payload_length = 0;
			byte[] payload = new byte[TWO_BYTE_FRAME];
			in.read(payload);
			
			for(int i = 0; i < payload.length; i++)
				frame.payload_length = (frame.payload_length << 8) + (payload[i] & 0xFF);
		}
		else if (frame.payload_length == PAYLOAD_LENGTH_64)
		{
			frame.payload_length = 0;
			byte[] payload = new byte[EIGHT_BYTE_FRAME];
			in.read(payload);
			
			for(int i = 0; i < payload.length; i++)
				frame.payload_length = (frame.payload_length << 8) + (payload[i] & 0xFF);
		}
		
		if(frame.isMasked())
		{	
			byte[] maskingKey = new byte[MASK_BYTES];
			in.read(maskingKey);
			
			byte[] payload = new byte[frame.payload_length];
			in.read(payload);
			
			for(int i = 0; i < frame.payload_length; i++)
				frame.addToPayload((char)(payload[i] ^ maskingKey[(i % 4)]));
		}
		else
			throw new WebSocketException("Client did not send a masked frame.");
		
		/*
		 * Determine if this frame is the final fragment of the message.
		 * If FALSE, recursively call this function to get next frame.
		 */
//		if(!frame.isFin())
//			frame.addFrame(build(new Frame(in.read(), in.read())));
		
		return frame;
	}
	
	/**
	 * Reads incoming stream of data from {@link Socket} connection.
	 * 
	 * @return {@link Frame}
	 * @throws WebSocketException Thrown from {@link #build(Frame)} or stream could not be read.
	 */
//	public Frame read() throws WebSocketException
//	{
//		try 
//		{
//			return build(new Frame(in.read(), in.read()));
//		} 
//		catch (IOException e) 
//		{
//			throw new WebSocketException("Could not read data from stream.", e);
//		}
//	}
	
	/**
	 * Builds a {@link Frame} or a link of {@link Frame}(s), depending on size of data being streamed.
	 * 
	 * @param frame - {@link Frame} being built. Uses recursion to build extending {@link Frame}(s) if needed.
	 * @return {@link Frame}
	 * @throws WebSocketException Is thrown if client's frame was not properly built.
	 * @throws IOException Thrown if stream of data is broken or corrupted.
	 */
//	private Frame build(Frame frame) throws WebSocketException, IOException
//	{
//		if(frame.payload_length == PAYLOAD_LENGTH_16)
//		{
//			frame.payload_length = 0;
//			byte[] payload = new byte[TWO_BYTE_FRAME];
//			in.read(payload);
//			
//			for(int i = 0; i < payload.length; i++)
//				frame.payload_length = (frame.payload_length << 8) + (payload[i] & 0xFF);
//		}
//		else if (frame.payload_length == PAYLOAD_LENGTH_64)
//		{
//			frame.payload_length = 0;
//			byte[] payload = new byte[EIGHT_BYTE_FRAME];
//			in.read(payload);
//			
//			for(int i = 0; i < payload.length; i++)
//				frame.payload_length = (frame.payload_length << 8) + (payload[i] & 0xFF);
//		}
//		
//		if(frame.isMasked())
//		{	
//			byte[] maskingKey = new byte[MASK_BYTES];
//			in.read(maskingKey);
//			
//			byte[] payload = new byte[frame.payload_length];
//			in.read(payload);
//			
//			for(int i = 0; i < frame.payload_length; i++)
//				frame.addToPayload((char)(payload[i] ^ maskingKey[(i % 4)]));
//		}
//		else
//			throw new WebSocketException("Client did not send a masked frame.");
//		
//		/*
//		 * Determine if this frame is the final fragment of the message.
//		 * If FALSE, recursively call this function to get next frame.
//		 */
//		if(!frame.isFin())
//			frame.addFrame(build(new Frame(in.read(), in.read())));
//		
//		return frame;
//	}

	@Override
	public void close() throws IOException 
	{
		in.close();		
	}
}
