package cs131.pa2.filter.concurrent;

import cs131.pa2.filter.Filter;
import cs131.pa2.filter.Message;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

/**
 * A filter that takes a file name as a parameter and outputs the lines of that
 * file. This filter cannot have input.
 * 
 * @author Kevin Cui
 *
 */
public class CatFilter extends ConcurrentFilter {

	private String fileName;

	/**
	 * Constructs a filter to read and output the lines of the given file. Tests that file path is valid.
	 * 
	 * @param fileName the name of the file that will be read
	 */
	public CatFilter(String fileName) {
		this.fileName = fileName;
		try {
			Scanner fileReader = new Scanner(
					new File(ConcurrentREPL.currentWorkingDirectory + ConcurrentREPL.PATH_SEPARATOR + fileName));
			fileReader.close();
		} catch (FileNotFoundException e) {
			throw new IllegalArgumentException(Message.FILE_NOT_FOUND.with_parameter("cat " + fileName));
		}
	}

	/**
	 * Reads the lines of the file specified in construction and adds them to
	 * output.
	 * 
	 * @throws IllegalArgumentException if the file does not exist
	 */
	@Override
	public void process() throws IllegalArgumentException{
		try {
			Scanner fileReader = new Scanner(
					new File(ConcurrentREPL.currentWorkingDirectory + ConcurrentREPL.PATH_SEPARATOR + fileName));
			// Read lines from file and add them to output.
			try {
				while (fileReader.hasNextLine()) {
					output.put(fileReader.nextLine());
				}
				output.put(POISON);
			} catch (InterruptedException e) {
			
			}
			
			fileReader.close();
		} catch (FileNotFoundException e) {
			throw new IllegalArgumentException(Message.FILE_NOT_FOUND.with_parameter("cat " + fileName));
		}
	}

	@Override
	protected String processLine(String line) {
		return null;
	}

	/**
	 * @throws IllegalArgumentException if the previous filter is not null since
	 *                                  CatFilter cannot have input
	 */
	@Override
	public void setPrevFilter(Filter prevFilter) throws IllegalArgumentException {
		if (prevFilter != null) {
			throw new IllegalArgumentException(Message.CANNOT_HAVE_INPUT.with_parameter("cat " + fileName));
		}
	}
}
