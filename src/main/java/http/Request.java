package http;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

/**
 * TODO Remove terminal printouts.
 * 
 * @author Ryan Mayobre
 *
 */
public class Request
{
	/**
	 * Method token from client request.
	 * 
	 * @see {@link Method}
	 */
	private final Method REQUEST_METHOD;
	
	/**
	 * URI request from client side.
	 * 
	 * If REQUEST_URI is presented as '/',
	 * default to call the index.html file.
	 * 
	 * IF file or path is inaccurate or does not
	 * exist, then send back user a '400 Bad Request' error.
	 */
	private final String REQUEST_URI;
	
	/**
	 * Path requested from client to gain access to.
	 * 
	 * Check for valid path and if path does not
	 * fall behind web root directories.
	 * 
	 * IF path DOES fall behind web root, then drop connection.
	 */
	private final String REQUEST_PATH;
	
	/**
	 * TODO finish HEADER javadoc.
	 */
	private final Map<String, String> REQUEST_HEADER = new HashMap<String, String>();
	
	/**
	 * TODO finish javadoc
	 * @param input
	 * @throws IOException
	 */
	public Request(InputStream input) throws IOException
	{
		BufferedReader in = new BufferedReader(new InputStreamReader(input));
//		String[] requestLine = input.readLine().split("\\s+");
		String request = in.readLine();
		System.out.println(request); // For testing
		String[] requestLine = request.split("\\s+");
		/*
		 * Gather requests from request-line.
		 */
		REQUEST_METHOD = Method.find(requestLine[0]);
		REQUEST_URI = requestLine[1];
		REQUEST_PATH = REQUEST_URI.substring(0, REQUEST_URI.lastIndexOf("/")+1);
		/*
		 * Place request headers inside of HashMap.
		 */
		String header = in.readLine();
		while(!header.isEmpty())
		{
			System.out.println(header); // For testing
			String[] h = header.split(":\\s+", 2);
			REQUEST_HEADER.put(h[0], h[1]);
			header = in.readLine();
		}
	}
	
	/**
	 * TODO finish javadoc
	 * @return {@link Request#REQUEST_METHOD}
	 */
	public Method getMethod()
	{
		return REQUEST_METHOD;
	}
	
	/**
	 * TODO finish javadoc
	 * @return {@link Request#REQUEST_URI}
	 */
	public String getURI()
	{
		return REQUEST_URI;
	}
	
	/**
	 * TODO finish javadoc
	 * @return {@link Request#REQUEST_PATH}
	 */
	public String getPath()
	{
		return REQUEST_PATH;
	}
	
	/**
	 * TODO finish getheader javadocs
	 * @param header
	 * @return
	 */
	private String getHeader(String header)
	{
		return REQUEST_HEADER.get(header);
	}
	
	/**
	 * TODO finish javadoc
	 * @return
	 */
	public boolean isWebSocketUpgrade()
	{
		if(getHeader("Upgrade").equals("websocket")
				&& getHeader("Connection").equals("Upgrade")
				&& getHeader("Sec-WebSocket-Version").equals("13"))
			return true;
		else
			return false;
	}
	
	/**
	 * TODO finish javadoc
	 * @return
	 */
	public String getKey()
	{
		return getHeader("Sec-WebSocket-Key");
	}
}
