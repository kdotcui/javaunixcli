package cs131.pa2.filter.concurrent;

import java.util.*;
/**
 * A filter that outputs the last n lines of its input, where n is specified in
 * construction. This filter requires input.
 * 
 * @author Kevin Cui
 *
 */
public class TailFilter extends ConcurrentFilter {
	//perhaps createa new datastrcuture
	private int numLines;
	private List<String> inputs;
	
	/**
	 * Constructs a filter to return the last given number of lines of input.
	 * 
	 * @param numLines the number of lines that the filter will output
	 */
	public TailFilter(String numLines) {
		this.numLines = Integer.valueOf(numLines);
		this.inputs = new ArrayList<>();
	}
	
	/**
	 * Overrided process method: Since the given TailFilter doesn't know 
 	 * the exact number of lines from its predecessor,
	 * we must obtain all lines in input before only returning 
	 * the last n lines
	 */
	@Override
	public void process() throws InterruptedException {
	    while (true) {
	        String line = input.take();
	        if (line.equals(POISON)) {
	            break;
	        }
	        processLine(line);
	    }

	    int startIndex = Math.max(0, inputs.size() - numLines);
	    for (int i = startIndex; i < inputs.size(); i++) {
	        output.put(inputs.get(i));
	    }

	    if (output != null) {
	        output.put(POISON);
	    }
	}

	
	/**
	 * Returns the given line if it is within the last n lines of input, where
	 * n is the number of lines passed as a parameter to the constructor.
	 */
	@Override
	public String processLine(String line) {
		inputs.add(line);
		return null;
//		if (input.size() >= numLines+1) {
//			return null;
//		}
//		return line;
	}
}
