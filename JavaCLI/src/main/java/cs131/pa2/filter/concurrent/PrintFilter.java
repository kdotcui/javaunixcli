package cs131.pa2.filter.concurrent;


/**
 * A filter that prints each line of input to the console.
 * 
 * @author Kevin Cui
 *
 */
public class PrintFilter extends ConcurrentFilter {
	
	/**
	 * Prints the given line and returns null.
	 */
	@Override
	public String processLine(String line) {
		System.out.println(line);
		return null;
	}
}
