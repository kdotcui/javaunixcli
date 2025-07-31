package cs131.pa2.filter.concurrent;

import cs131.pa2.filter.Filter;
import cs131.pa2.filter.Message;
import java.io.File;

/**
 * A filter that outputs all the files and directories in the shell's current
 * working directory. This filter cannot have input.
 * 
 * @author Kevin Cui
 *
 */
public class ListFilesFilter extends ConcurrentFilter {

	/**
	 * Outputs line by line the names of the files and directories in the shell's
	 * current working directory.
	 */
	@Override
	public void process() throws InterruptedException{
		File currentWorkingDirectory = new File(ConcurrentREPL.currentWorkingDirectory);
		File[] files = currentWorkingDirectory.listFiles();
		for (File file : files) {
			this.output.put(file.getName());
		}
		output.put(POISON);
	}

	@Override
	public String processLine(String line) {
		return null;
	}

	/**
	 * @throws IllegalArgumentException if the previous filter is not null since
	 *                                  ListFilesFilter cannot have input
	 */
	@Override
	public void setPrevFilter(Filter prevFilter) throws IllegalArgumentException {
		if (prevFilter != null) {
			throw new IllegalArgumentException(Message.CANNOT_HAVE_INPUT.with_parameter("ls"));
		}
	}
}
