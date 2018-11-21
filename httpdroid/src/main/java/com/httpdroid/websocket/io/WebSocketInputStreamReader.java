package com.httpdroid.websocket.io;

import com.httpdroid.websocket.Frame;
import com.httpdroid.websocket.exception.WebSocketException;
import com.httpdroid.websocket.exception.WebSocketIOException;

import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class WebSocketInputStreamReader implements Closeable
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
	
	WebSocketInputStreamReader(InputStream in) {
		this.in = in;
	}

    /**
     * General use of the WebSocketInputStreamReader. Requires frames to be masked in
     * order to be read. WebSocketServer requires the frames to always be masked when
     * receiving from client, however, client WebSockets do not require masking from server.
     *
     * @return List of frame fragments.
     * @throws WebSocketException Thrown because of IOExceptions or WebSocket protocols were broken.
     * @see #read(boolean)
     */
	public List<Frame> read() throws WebSocketException {
	    return read(true);
    }

	/**
	 * Reads the WebSocket's input stream of data and produces a list of fragments.
	 * These fragments will add up to become one frame of data.
	 *
	 * @param requiresMask If the stream is being used for a WebSocketServer, masking is required.
	 *                    For client-side WebSocket connections, frames sent to a client are not
	 *                    required to be masked.
	 * @return List of frame fragments. The data needs to be combined to be usable.
	 * @throws WebSocketException when frames where not masked or input stream was corrupted.
	 * @see <a href="https://tools.ietf.org/html/rfc6455#section-5.1">RFC 6455, Section 5.1 Overview</a>
	 */
	public List<Frame> read(boolean requiresMask) throws WebSocketException {
		List<Frame> fragments = new ArrayList<>();

		try {
			do {
				fragments.add(build(in.read(), in.read(), requiresMask));
			} while (!(fragments.get(fragments.size()).isFin())); // Do-while not final fragment.
		} catch (IOException e) {
			// TODO: give this exception a closing code.
			throw new WebSocketIOException("Could not read from WebSocket's input stream.", e);
		}

		return fragments;
	}

	private Frame build(int b0, int b1, boolean requiresMask) throws IOException, WebSocketException {
		Frame frame = new Frame(b0, b1);
		if(frame.payload_length == PAYLOAD_LENGTH_16) {
			frame.payload_length = 0;
			byte[] payload = new byte[TWO_BYTE_FRAME];
			in.read(payload);

			for(int i = 0; i < payload.length; i++) {
				frame.payload_length = (frame.payload_length << 8) + (payload[i] & 0xFF);
			}
		}
		else if (frame.payload_length == PAYLOAD_LENGTH_64) {
			frame.payload_length = 0;
			byte[] payload = new byte[EIGHT_BYTE_FRAME];
			in.read(payload);

			for(int i = 0; i < payload.length; i++) {
				frame.payload_length = (frame.payload_length << 8) + (payload[i] & 0xFF);
			}
		}

		// Check if mask is required.
		if(requiresMask) {
			// Check if frame is masked.
			if(frame.isMasked()) {
				// Unmask data and place inside of frame object.
				byte[] maskingKey = new byte[MASK_BYTES];
				in.read(maskingKey);

				byte[] payload = new byte[frame.payload_length];
				in.read(payload);

				for(int i = 0; i < frame.payload_length; i++) {
					frame.addToPayload((char) (payload[i] ^ maskingKey[(i % 4)]));
				}
			} else {
				throw new WebSocketException("Client did not send a masked frame.", WebSocketException.CloseCode.PROTOCOL_ERROR);
			}
		} else {
			// Place data into frame object.
			byte[] payload = new byte[frame.payload_length];
			in.read(payload);

			for(int i = 0; i < frame.payload_length; i++) {
				frame.addToPayload((char) (payload[i]));
			}
		}

		return frame;
	}

	@Override
	public void close() throws IOException {
		in.close();		
	}
}
