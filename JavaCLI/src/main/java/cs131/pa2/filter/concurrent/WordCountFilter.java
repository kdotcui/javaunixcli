package cs131.pa2.filter.concurrent;

/**
 * A filter that outputs the number of lines, words, and characters that are
 * inputed. This filter requires input.
 * 
 * @author Kevin Cui
 *
 */
public class WordCountFilter extends ConcurrentFilter {

	private int lineCount;
	private int wordCount;
	private int charCount;

	/**
	 * Counts the number of lines, words, and characters from input and outputs
	 * those counts.
	 * Then adds poison pill once done.
	 */
	@Override
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
		output.put(lineCount + " " + wordCount + " " + charCount);
		output.put(POISON);
	}

	/**
	 * Increases the counts and returns null.
	 */
	@Override
	protected String processLine(String line) {
		lineCount++;
		wordCount += line.split("\\s+").length;
		charCount += line.length();
		return null;
	}

}
