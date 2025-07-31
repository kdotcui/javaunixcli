package cs131.pa2.filter.concurrent;

import java.util.concurrent.LinkedBlockingQueue;
import cs131.pa2.filter.Filter;

/**
 * An abstract class that extends the Filter and implements the basic functionality of all filters. Each filter should
 * extend this class and implement functionality that is specific for this filter. 
 * You should not modify this class
 * .
 * @author Kevin Cui
 *
 */
public abstract class ConcurrentFilter extends Filter implements Runnable {
	/**
	 * The input queue for this filter
	 */
	protected LinkedBlockingQueue<String> input;
	/**
	 * The output queue for this filter
	 */
	protected LinkedBlockingQueue<String> output;
	
	/**
	 * String to use to tell next filter that there will be no more input.
	 */
	protected final String POISON = "POISION PILL asdfasdfasdf";
	
	@Override
	public void setPrevFilter(Filter prevFilter) {
		prevFilter.setNextFilter(this);
	}
	
	@Override
	public void setNextFilter(Filter nextFilter) {
		if (nextFilter instanceof ConcurrentFilter){
			ConcurrentFilter sequentialNext = (ConcurrentFilter) nextFilter;
			this.next = sequentialNext;
			sequentialNext.prev = this;
			if (this.output == null){
				this.output = new LinkedBlockingQueue<String>();
			}
			sequentialNext.input = this.output;
		} else {
			throw new RuntimeException("Should not attempt to link dissimilar filter types.");
		}
	}
	
	/**
	 * run method which calls process and catches exceptions
	 */
	public void run() {
		try {
			process();
		} catch(InterruptedException e){
		}
		

	}
	/**
	 * Processes the input queue and passes the result to the output queue. Adds poison pill once done processing.
	 */
	public void process() throws InterruptedException {
		while (true){
			String line = input.take();
			if (line.equals(POISON)) {
				break;
			}
			String processedLine = processLine(line);
			if (processedLine != null){
				output.put(processedLine);
			}
		}	
		if (output != null) {
			output.put(POISON);
		}
	}
	
	@Override
	public boolean isDone() {
		return input.size() == 0;
	}
	
	/**
	 * Called by the {@link #process()} method for every encountered line in the input queue.
	 * It then performs the processing specific for each filter and returns the result.
	 * Each filter inheriting from this class must implement its own version of processLine() to
	 * take care of the filter-specific processing.
	 * @param line the line got from the input queue
	 * @return the line after the filter-specific processing
	 */
	protected abstract String processLine(String line);
	
}
