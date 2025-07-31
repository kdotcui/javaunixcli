package cs131.pa2.filter.concurrent;

import java.io.PrintStream;
import java.io.FileNotFoundException;

import cs131.pa2.filter.Filter;
import cs131.pa2.filter.Message;

/**
 * A filter that writes each line of input to a file. If the given file does not exist, the 
 * file will be created. This filter requires input and cannot have output.
 * 
 * @author Kevin Cui
 *
 */
public class RedirectFilter extends ConcurrentFilter {
	
	private String fileName;
	
	public RedirectFilter(String fileName) {
		this.fileName = fileName;
	}
	
	/**
	 * Writes each line of input to the file specified in construction.
	 * @throws InterruptedException 
	 */
	@Override
	public void process() throws InterruptedException {
		try {
			PrintStream outputFile = new PrintStream(ConcurrentREPL.currentWorkingDirectory + ConcurrentREPL.PATH_SEPARATOR + fileName);
			while(!Thread.interrupted()) {
				String line = input.take();
				if (line.equals(this.POISON)) {
					break;
				}
				outputFile.println(line);
			}
			outputFile.close();
		} catch(FileNotFoundException e) {
		}
	}
	
	@Override
	protected String processLine(String line) {
		return null;
	}
	
	/**
	 * @throws IllegalArgumentException if next filter is not null since RedirectFilter cannot have output
	 */
	@Override
	public void setNextFilter(Filter nextFilter) throws IllegalArgumentException {
		if (nextFilter != null) {
			throw new IllegalArgumentException(Message.CANNOT_HAVE_OUTPUT.with_parameter("> " + fileName));
		}
	}
	
}
