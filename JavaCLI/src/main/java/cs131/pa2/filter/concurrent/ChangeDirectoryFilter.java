package cs131.pa2.filter.concurrent;

import cs131.pa2.filter.Filter;
import cs131.pa2.filter.Message;

import java.nio.file.Path;

/**
 * A filter that takes a directory name as a parameter and changes the shell's working directory
 * to that directory. This filter cannot have input or output.
 * 
 * @author Kevin Cui
 *
 */
public class ChangeDirectoryFilter extends ConcurrentFilter {

	private String directory;

	/**
	 * Constructs the filter to change the shell's working directory to the given directory.
	 * 
	 * @param directory the relative path of the destination directory
	 */
	public ChangeDirectoryFilter(String directory) {
		this.directory = directory;
		Path newPath = Path.of(ConcurrentREPL.currentWorkingDirectory).resolve(directory);
		if (!newPath.toFile().isDirectory()) {
			throw new IllegalArgumentException(Message.DIRECTORY_NOT_FOUND.with_parameter("cd " + directory));
		}
	}
	
	/**
	 * Sets the shell's working directory to the given directory.
	 * 
	 * @throws IllegalArgumentException if the directory specified in construction does not exist
	 */
	@Override
	public void process() throws IllegalArgumentException {
		Path newPath = Path.of(ConcurrentREPL.currentWorkingDirectory).resolve(directory);
		if (!newPath.toFile().isDirectory()) {
			throw new IllegalArgumentException(Message.DIRECTORY_NOT_FOUND.with_parameter("cd " + directory));
		}
		ConcurrentREPL.currentWorkingDirectory = newPath.normalize().toString();
	}

	@Override
	protected String processLine(String line) {
		return null;
	}
	
	/**
	 * @throws IllegalArgumentException if previous filter is not null since
	 *                                  ChangeDirectoryFilter cannot have input
	 */
	@Override
	public void setPrevFilter(Filter prevFilter) throws IllegalArgumentException {
		if (prevFilter != null) {
			throw new IllegalArgumentException(Message.CANNOT_HAVE_INPUT.with_parameter("cd " + directory));
		}
	}

	/**
	 * @throws IllegalArgumentException if next filter is not null since
	 *                                  ChangeDirectoryFilter cannot have output
	 */
	@Override
	public void setNextFilter(Filter nextFilter) throws IllegalArgumentException {
		if (nextFilter != null) {
			throw new IllegalArgumentException(Message.CANNOT_HAVE_OUTPUT.with_parameter("cd " + directory));	
		}
	}
}
