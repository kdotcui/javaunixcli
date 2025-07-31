package cs131.pa2.filter.concurrent;

import cs131.pa2.filter.Filter;
import cs131.pa2.filter.Message;

/**
 * A filter that outputs the shell's current working directory. This
 * filter cannot have input.
 * 
 * @author Kevin Cui
 *
 */
public class PrintWorkingDirectoryFilter extends ConcurrentFilter {

	/*
	 * Outputs the shell's current working directory.
	 */
	@Override
	public void process() throws InterruptedException {
		output.put(ConcurrentREPL.currentWorkingDirectory);
		output.put(POISON);
	}
	
	@Override
	protected String processLine(String line) {
		return null;
	}
	
	/**
	 * @throws IllegalArgumentException if previous filter is not null since
	 *                                  PrintWorkingDirectoryFilter cannot have input
	 */
	@Override
	public void setPrevFilter(Filter prevFilter) throws IllegalArgumentException {
		if (prevFilter != null) {
			throw new IllegalArgumentException(Message.CANNOT_HAVE_INPUT.with_parameter("pwd"));	
		}
	}
	
}
