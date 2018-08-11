package websocket;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class WebSocketMessage
{
	/**
     * Constant indicating end of stream.
     */
    public static final int EOF = -1;

    /**
     * Payload length indicating that the payload's true length is a
     * yet-to-be-provided unsigned 16-bit integer.
     */
    public static final int LENGTH_16 = 0x7E;

    /**
     * A payload specified with 16 bits must have at least this
     * length in order to be considered valid.
     */
    public static final int LENGTH_16_MIN = 126;

    /**
     * Payload length indicating that the payload's true length is a
     * yet-to-be-provided unsigned 64-bit integer (MSB = 0).
     */
    public static final int LENGTH_64 = 0x7F;

    /**
     * A payload specified with 64 bits must have at least this
     * length in order to be considered valid.
     */
    public static final int LENGTH_64_MIN = 0x10000;

    /**
     * Binary mask to limit an int to 8 bits (an unsigned byte).
     */
    public static final int MASK_BYTE = 0x000000FF;

    /**
     * Binary mask to extract the final fragment flag bit of a WebSocket frame.
     */
    public static final int MASK_FINAL = 0x80;

    /**
     * Binary mask to extract the masking flag bit of a WebSocket frame.
     */
    public static final int MASK_MASK = 0x80;

    /**
     * Binary mask to limit a value in the range [0-3] (inclusive).
     */
    public static final int MASK_MASKING_INDEX = 0x03;

    /**
     * Binary mask to extract the opcode bits of a WebSocket frame.
     */
    public static final int MASK_OPCODE = 0x0F;

    /**
     * Binary mask to extract the control bit of an opcode.
     */
    public static final int MASK_CONTROL_OPCODE = 0x08;

    /**
     * Binary mask to extract the payload size of a WebSocket frame.
     */
    public static final int MASK_PAYLOAD_SIZE = 0x7F;

    /**
     * Binary mask to extract the reserved flag bits of a WebSocket frame.
     */
    public static final int MASK_RESERVED = 0x70;

    /**
     * Number of masking bytes provided by the client.
     */
    public static final int NUM_MASKING_BYTES = 4;

    /**
     * Number of octets (bytes) in a 64-bit number.
     */
    public static final int NUM_OCTET_64 = 8;

    /**
     * Number of bits in an octet.
     */
    public static final int OCTET = 8;
    
    public static final int TWO_BYTE_FRAME = 126;
    
    public static final int EIGHT_BYTE_FRAME = 127;
    
    public byte[] readMessage(InputStream in) throws IOException
    {
    	boolean fin = false;
    	ByteArrayOutputStream messageBos = new ByteArrayOutputStream();
    	
    	int opcode;
    	do
    	{
    		int b0 = in.read();
    		fin = b0 >> 7 == 1;
    		opcode = b0 & 0xF;
    		
    		int b1 = in.read();
    		boolean mask = b1 >> 7 == 1;
    		int payloadLen = b1 & MASK_PAYLOAD_SIZE;
    		
    		byte[] payloadBytes;
    		switch(payloadLen)
    		{
    			case TWO_BYTE_FRAME:
    				payloadLen = 0;
    				payloadBytes = new byte[2];
    				in.read(payloadBytes);
    				
    				for(int i = 0; i < payloadBytes.length; i++)
    					payloadLen = (payloadLen << OCTET) + (payloadBytes[i] & 0xFF);
    				break;
    			case EIGHT_BYTE_FRAME:
    				payloadLen = 0;
    				payloadBytes = new byte[OCTET];
    				in.read(payloadBytes);
    				
    				for(int i = 0; i < payloadBytes.length; i++)
    					payloadLen = (payloadLen << OCTET) + (payloadBytes[i] & 0xFF);
    				break;
    		}
    		
    		byte[] maskingKey = new byte[NUM_MASKING_BYTES];
    		if(mask)
    			in.read(maskingKey);
    		
    		byte[] payload = new byte[payloadLen];
    		in.read(payload);
    		
    		if(mask)
    			for(int i = 0; i < payloadLen; i++)
    				messageBos.write((char)(payload[i] ^ maskingKey[(i % NUM_MASKING_BYTES)]));
    		else
    			for(int i = 0; i < payloadLen; i++)
    				messageBos.write((char)payload[i]);
    		
    	}
    	while(!fin);
    	
    	return messageBos.toByteArray();
    }
}
