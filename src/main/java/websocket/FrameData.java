package websocket;

/**
 * TODO Javadoc FrameData
 * @author Ryan Mayobre
 * @see <a href="https://tools.ietf.org/html/rfc6455#section-5.2">RFC 6455, Section 5.2 (Base Framing Protocol)</a>
 */
public interface FrameData 
{
	/**
     * Binary mask to extract the masking flag bit of a WebSocket frame.
     * @see {@link FrameData}
     */
    final int MASK = 0x80;
    
    /**
     * Number of masking bytes provided from client.
     * @see {@link FrameData}
     */
    final int MASK_BYTES = 0x4;
    
    /**
     * Binary mask to extract the final fragment flag bit of a WebSocket frame.
     * @see {@link FrameData}
     */
    final int MASK_FINAL = 0x80;
    
    /**
	 * Binary mask to extract RSV1 bit of a WebSocket frame.
	 * @see {@link FrameData}
	 */
    final int MASK_RSV1 = 0x40;
    
    /**
	 * Binary mask to extract RSV2 bit of a WebSocket frame.
	 * @see {@link FrameData}
	 */
    final int MASK_RSV2 = 0x40;
    
    /**
	 * Binary mask to extract RSV3 bit of a WebSocket frame.
	 * @see {@link FrameData}
	 */
    final int MASK_RSV3 = 0x40;
    
	/**
     * Binary mask to extract the opcode bits of a WebSocket frame.
     * @see {@link FrameData}
     */
    final int MASK_OPCODE = 0x0F;
    
    /**
	 * Maximum size of Control frame.
	 * @see {@link FrameData}
	 */
    final int MAX_CONTROL_PAYLOAD = 0x7D;
    
    /**
     * Binary mask to remove all but the bits of octet 2.
     * @see {@link FrameData}
     */
    final int MASK_HIGH_WORD_LOW_BYTE = 0x00ff0000;

    /**
     * Binary mask to remove all but the bits of octet 1.
     * @see {@link FrameData}
     */
    final int MASK_LOW_WORD_HIGH_BYTE = 0x0000ff00;

    /**
     * Binary mask to remove all but the lowest 8 bits (octet 0).
     * @see {@link FrameData}
     */
    final int MASK_LOW_WORD_LOW_BYTE = 0x000000ff;

    /**
     * Number of bits required to shift octet 1 into the lowest 8 bits.
     * @see {@link FrameData}
     */
    final int OCTET_ONE = 8;
    
    /**
     * Payload length indicating that the payload's true length is a
     * yet-to-be-provided unsigned 16-bit integer.
     * @see {@link FrameData}
     */
    final int PAYLOAD_LENGTH_16 = 0x7E;
    
    /**
     * Payload length indicating that the payload's true length is a
     * yet-to-be-provided unsigned 64-bit integer (MSB = 0).
     * @see {@link FrameData}
     */
    final int PAYLOAD_LENGTH_64 = 0x7F;
    
    /**
     * Binary number for the two byte frame.
     * @see {@link FrameData}
     */
    final int TWO_BYTE_FRAME = 0x2;
    
    /**
     * Binary number for the eight byte frame.
     * @see {@link FrameData}
     */
    final int EIGHT_BYTE_FRAME = 0x8;
}
