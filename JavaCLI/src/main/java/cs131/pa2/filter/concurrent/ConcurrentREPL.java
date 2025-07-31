package cs131.pa2.filter.concurrent;

import cs131.pa2.filter.Message;
import java.util.Scanner;
import java.util.ArrayList;
import java.util.List;

/**
 * The main implementation of the REPL (read-eval-print-loop). It reads
 * commands from the user, parses them, executes them and displays the result. All done concurrently using threads!
 * 
 * @author Kevin Cui
 * 
 *
 */
public class ConcurrentREPL {

    /**
     * The path of the current working directory
     */
    public static String currentWorkingDirectory;
    public static final String PATH_SEPARATOR = System.getProperty("file.separator");

    /**
     * The main method that will execute the concurrent REPL loop.
     * Added repl_jobs which displays all active jobs
     * Added kill which terminates the specified thread
     * 
     * @param args not used
     */
    public static void main(String[] args) {
        currentWorkingDirectory = System.getProperty("user.dir");

        Scanner consoleReader = new Scanner(System.in);
        System.out.print(Message.WELCOME);

        ArrayList<ArrayList<Thread>> backgroundJobs = new ArrayList<>();
        ArrayList<String> jobs = new ArrayList<>();
        
        int counter = 0;

        while (true) {
            System.out.print(Message.NEWCOMMAND);
            String userInput = consoleReader.nextLine().trim();

            if (userInput.isEmpty()) {
                continue;
            }

            if (userInput.equals("exit")) {
                break;
            }

            if (userInput.equals("repl_jobs")) {
                for (int i = 0; i < backgroundJobs.size(); i++) {
                    ArrayList<Thread> jobThreads = backgroundJobs.get(i);
                    if (jobThreads.get(jobThreads.size() - 1).isAlive()) {
                        System.out.println(jobs.get(i));
                    }
                }
                continue;
            }
            
            if (userInput.startsWith("kill")) {
            	String[] tokens = userInput.split(" ");
            	int target = Integer.parseInt(tokens[1]) - 1;
                if (target < backgroundJobs.size()) {
                    ArrayList<Thread> threads = backgroundJobs.get(target);
                    for (Thread t : threads) {
                        t.interrupt();
                    }
                    backgroundJobs.remove(target);
                    jobs.remove(target);
                }
                continue;
            }

            boolean isBackgroundCommand = userInput.endsWith("&");

            List<ConcurrentFilter> filters = ConcurrentCommandBuilder.createFiltersFromCommand(userInput);
            if (filters != null) {
                try {
                    ArrayList<Thread> threads = new ArrayList<>();

                    for (int i = 0; i < filters.size(); i++) {
                        Thread thread = new Thread(filters.get(i));
                        threads.add(thread);
                        thread.start();
                        if (!isBackgroundCommand) {
                            thread.join();
                        }
                    }
                    if (isBackgroundCommand) {
                        backgroundJobs.add(threads);
                        jobs.add("\t" + (counter + 1) + ". " + userInput);
                        counter++;
                    }

                } catch (Exception e) {
                    System.out.print(e.getMessage());
                }
            }
        }
        System.out.print(Message.GOODBYE);
        consoleReader.close();
    }
}
