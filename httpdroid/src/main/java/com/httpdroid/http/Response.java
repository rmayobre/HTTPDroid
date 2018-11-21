package http;

import java.io.DataOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * 
 * @author Ryan Mayobre
 *
 */
public class Response
{	
	/**
	 * TODO change this
	 */
	private final String server = "Server: HTTPDroid_0.1";
	
	private final Map<String, String> header = new HashMap<String, String>();
	
	private final Status status;
	
	private final Content type;
	
	private final int dataSize;
	
	Response(Status status, Content type, int dataSize)
	{
		this.status = status;
		this.type = type;
		this.dataSize = dataSize;
	}
	
	public void addHeader(String name, String value)
	{
		header.put(name, value);
    }

	public void content(Request request)
	{
		
	}
	
	private void responseHeader(DataOutputStream out) throws IOException
	{
		out.writeBytes("HTTP/1.1 " + status.getCode() + " OK\r\n");
		out.writeBytes("Content-Type: " + type.getMime() + "\r\n");
		out.writeBytes("Content-Length: " + dataSize + "\r\n"); 
		out.writeBytes(server);
		out.writeBytes("\r\n\r\n");
	}
	
	public void send(DataOutputStream out) throws IOException
	{
		
		
		out.writeBytes("\r\n\r\n");
	}
}
