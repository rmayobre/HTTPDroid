package com.httpdroid.library.websocket.frame;

import com.httpdroid.library.websocket.Frame;

import java.io.UnsupportedEncodingException;

import websocket.OpCode;

/**
 * Class structure for a Text frame.
 * @author Ryan Mayobre
 * @see {@link Frame}
 */
public class TextFrame extends Frame
{
	/**
	 * Constructor for creating a frame.
	 * @param message - message to be placed inside frame.
	 * @throws UnsupportedEncodingException Thrown if message 
	 * does not support UTF-8 encoding. If this gets thrown, then
	 * close connection on a 1007 (Invalid Frame Payload Data) status
	 * code.
	 * @see {@link Frame#Frame(OpCode code, byte[] data)}
	 */
	TextFrame(String message) throws UnsupportedEncodingException 
	{
		super(OpCode.TEXT, message.getBytes("UTF-8"));
	}
	
	/**
	 * @return {@link #payload}
	 */
	public String getMessage()
	{
		return payload.toString();
	}
}
