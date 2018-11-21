package http;

/**
 * 
 * TODO finish javadocs for Status enum
 * 
 * @author Ryan Mayobre
 *
 */
public enum Status
{
//	/*
//	 * 1xx: Information
//	 */
//	CONTINUE(100, "Continue"),
//    SWITCH_PROTOCOL(101, "Switching Protocols"),
//    /*
//     * 2xx: Success
//     */
//    OK(200, "OK"),
//    CREATED(201, "Created"),
//    ACCEPTED(202, "Accepted"),
//    NO_CONTENT(204, "No Content"),
//    PARTIAL_CONTENT(206, "Partial Content"),
//    MULTI_STATUS(207, "Multi-Status"),
//    /*
//     * 3xx: Redirection
//     */
//    MOVED_PERMANETLY(301, "Moved Permanently"),
//    FOUND(302, "Found"),
//    SEE_OTHER(303, "See Other"),
//    NOT_MODIFIED(304, "Not Modified"),
//    TEMPORARY_REDIRECT(307, "Temporary Redirect"),
//    /*
//     * 4xx: Client Error
//     */
//    BAD_REQUEST(400, "Bad Request"),
//    UNAUTHORIZED(401, "Unauthorized"),
//    FORBIDDEN(403, "Forbidden"),
//    NOT_FOUND(404, "Not Found"),
//    METHOD_NOT_ALLOWED(405, "Method Not Allowed"),
//    NOT_ACCEPTABLE(406, "Not Acceptable"),
//    REQUEST_TIMEOUT(408, "Request Timeout"),
//    CONFLICT(409, "Conflict"),
//    GONE(410, "Gone"),
//    LENGTH_REQUIRED(411, "Length Required"),
//    PRECONDITION_FAILED(412, "Precondition Failed"),
//    PAYLOAD_TOO_LARGE(413, "Payload Too Large"),
//    UNSUPPORTED_MEDIA_TYPE(415, "Unsupported Media Type"),
//    REQUESTED_RANGE_NOT_SATISFIABLE(416, "Requested Range Not Satisfiable"),
//    EXPECTATION_FAILED(417, "Expectation Failed"),
//    TOO_MANY_REQUESTS(429, "Too Many Requests"),
//    /*
//     * 5xx: Server Error
//     */
//    INTERNAL_SERVER_ERROR(500, "Internal Server Error"),
//    NOT_IMPLEMENTED(501, "Not Implemented"),
//    SERVICE_UNAVAILABLE(503, "Service Unavailable"),
//    HTTP_VERSION_NOT_SUPPORTED(505, "HTTP Version Not Supported");
	
	OK(200, "OK"), 
	CREATED(201, "Created"), 
	ACCEPTED(202, "Accepted"), 
	NO_CONTENT(204, "No Content"), 
	PARTIAL_CONTENT(206, "Partial Content"), 
	REDIRECT(301, "Moved Permanently"), 
	NOT_MODIFIED(304, "Not Modified"),
	BAD_REQUEST(400, "Bad Request"), 
	UNAUTHORIZED(401, "Unauthorized"), 
	FORBIDDEN(403, "Forbidden"), 
	NOT_FOUND(404, "Not Found"),
	RANGE_NOT_SATISFIABLE(416, "Requested Range Not Satisfiable"),
	INTERNAL_ERROR(500, "Internal Server Error");
	
	/**
	 * 
	 */
	private final int statusCode;
	
	/**
	 * 
	 */
	private final String statusMessage;
	
	/**
	 * 
	 * @param statusCode
	 * @param statusMessage
	 */
	Status(int statusCode, String statusMessage)
	{
		this.statusCode = statusCode;
		this.statusMessage = statusMessage;
	}
	
	/**
	 * 
	 * @param statusCode
	 * @return
	 */
	public static Status find(int statusCode)
	{
		for(Status status : Status.values())
			if(status.getCode() == statusCode)
				return status;
		
		return null;
	}
	
	/**
	 * 
	 */
	public String toString()
	{
		return "" + statusCode + " " + statusMessage;
	}
	
	/**
	 * 
	 * @return
	 */
	public int getCode()
	{
		return statusCode;
	}
	
	/**
	 * 
	 * @return
	 */
	public String getMessage()
	{
		return statusMessage;
	}
}