
/**
 * 
 * 
 * @author Ryan Mayobre
 *
 */
public class AsyncExecutor
{
	/**
	 * 
	 */
	private long COUNTER;
	
	/**
	 * 
	 */
	private final String NAME;
	
	/**
	 * 
	 * @param name
	 */
	AsyncExecutor(String name)
	{
		this.NAME = name;
	}

	/**
	 * Runs provided code.
	 * 
	 * @param code - Runnable class or code that will be executed.
	 * @see {@link Runnable}
	 */
	public void execute(Runnable code) 
	{
		++COUNTER;
		Thread asyncExecution = new Thread(code);
		asyncExecution.setName(NAME);
		asyncExecution.start();
	}

}
