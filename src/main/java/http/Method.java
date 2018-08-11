package http;

/**
 * 
 * @author Ryan Mayobre
 *
 */
public enum Method 
{
	GET,
    PUT,
    POST,
    DELETE,
    HEAD,
    OPTIONS,
    TRACE,
    CONNECT;
    
	/**
	 * TODO finish javadocs for find.
	 * 
	 * @param method
	 * @return
	 */
    public static Method find(String method)
    {
		try
		{
			return valueOf(method);
		}
		catch(IllegalArgumentException e)
		{
			return null;
		}
    }
}
