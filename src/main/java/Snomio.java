/**
 * Deals with interactions with the user.
 * This Snomio simply compile both BufferedReader and BufferedWriter
 * for easier I/O usages. For eg. read commands, contents, numbers
 * from user's input.
 *
 * Solution below adapted from https://github.com/Kattis/kattio/blob/master/Kattio.java
 *
 * @author: Sharptail
 */
import java.io.*;
import java.util.StringTokenizer;

public class Snomio extends PrintWriter {
    private BufferedReader reader;
    private StringTokenizer tokenizer;

    public Snomio(InputStream is, OutputStream os){
        super(new BufferedOutputStream(os));
        reader = new BufferedReader(new InputStreamReader(is));
    }

    /**
     * Prints a horizontal line.
     * Note that the output is not flushed. Remember to flush after calling the method.
     */
    public void showLine(){
        println("--------------------------------");
    }

    /**
     * Prints given message as an error
     *
     * @param errMsg error message
     */
    public void showError(String errMsg){
        showLine();
        println("Error: " + errMsg);
        showLine();
        flush();
    }

    /**
     * Prints an error of unable to load save file and prompt user a new save file will be created.
     */
    public void showLoadingError(){
        showError("Failed to load file. A new save file will be created.");
    }

    /**
     * Prints the welcome message of Snom on startup.
     */
    public void showWelcomeMsg(){
        showLine();
        println("Bonjour! I'm Snom! *squish*");
        println("Try giving me some commands, I might be able to do something!");
        println("[type 'bye' to exit program]");
        showLine();
        flush();
    }

    /**
     * Prints out the entire task list.
     *
     * @throws SnomException if there is content after the command or there isn't any task in the task list
     */
    public void showTaskList(TaskList taskList) throws SnomException{
        showLine();
        if(taskList.getSize() > 0){
            println("Here are the tasks in your list:");
            for(int i = 0; i < taskList.getSize(); i++){
                println((i+1) + ". " + taskList.getTask(i).toString());
            }
        }else{
            throw new SnomException("You have no task in your list right now, try adding some and try again :D");
        }
        showLine();
        flush();
    }

    /**
     * Prints out the task added into the taskList and size of current taskList.
     *
     * @param task     task added
     * @param listSize task list size
     */
    public void showTaskAdded(Task task, int listSize){
        showLine();
        println("Got it. I've added this task:");
        println("\t" + task.toString());
        println("Now you have " + listSize + " tasks in the list.");
        showLine();
        flush();
    }

    /**
     * Prints the list of recent finished tasks.
     *
     * @param finishedTasks list of finished tasks
     */
    public void showFinishedTasks(Task[] finishedTasks) {
        showLine();
        println("Great Job! I've marked this task(s) as finish:");
        for(Task task: finishedTasks){
            println("\t" + task.toString());
        }
        showLine();
        flush();
    }

    /**
     * Prints the list of recently deleted tasks.
     *
     * @param deletedTasks list of deleted tasks
     */
    public void showDeletedTasks(Task[] deletedTasks) {
        showLine();
        println("Noted, I've removed this task(s)");
        for(Task task: deletedTasks){
            println("\t" + task.toString());
        }
        showLine();
        flush();
    }

    /**
     * Prints exit message.
     */
    public void showExitMessage(){
        showLine();
        println("Ciao! Hope to see you again soon!");
        showLine();
        flush();
    }

    /**
     * This method will read in the whole line and split each word into tokens.
     * Then it will extract out the first token and return it.
     *
     * If there are already words/tokens in the tokenizer, it will return the next first token instead.
     *
     * @return  the first word that extracted from the tokenizer
     */
    public String readWord() {
        String input = null;
        String token;

        while(tokenizer == null || !tokenizer.hasMoreTokens()){
            try {
                input = reader.readLine();
            } catch (IOException e) {
                e.printStackTrace();
            }
            tokenizer = new StringTokenizer(input);
        }
        token = tokenizer.nextToken();

        return token;
    }

    /**
     * This method is exclusive to read the content of the commands (todo, deadline, event)
     *
     * @param command        command to be executed
     * @return               the content of a task
     * @throws SnomException throws exception if the content is empty
     */
    public String readContent(String command) throws SnomException {
        if(tokenizer.hasMoreTokens()){
            return tokenizer.nextToken("");
        }else{
            throw new SnomException("OOPS!!! The description of a " + command + " cannot be empty.");
        }
    }

    /**
     * This method is exclusive to read the content of the commands with Delimiters (deadline, event)
     *
     * @param command        command to be executed
     * @param delim          delimiter to split the date from content
     * @return               the content of a task
     * @throws SnomException throws exception if the content is empty
     */
    public String[] readContentWithDate(String command, String delim) throws SnomException {
        String content = this.readContent(command);
        String[] array = content.split(delim);
        if(array.length == 2){
            return array;
        }else if(array.length < 2){
            throw new SnomException("Please enter at least one date for your " + command + "!");
        }else{
            throw new SnomException("Oops! You have entered more than ONE date, please try again!");
        }
    }

    /**
     * This method is exclusive to read the content of the commands with number list (finish, delete)
     *
     * @param command        command to be executed
     * @return               a number list for command to be executed
     * @throws SnomException throws exception if the number is invalid
     */
    public int[] readContentWithNumbers(String command) throws SnomException{
        int[] nums = new int[tokenizer.countTokens()];
        if(nums.length > 0){
            for(int i = 0; i < nums.length; i++){
                nums[i] = this.readInt();
            }
            return nums;
        }else{
            throw new SnomException("Oops! Please at least give one task number to " + command + " a task");
        }
    }

    /**
     * This method is similar to BufferedReader.readLine() except it will read from the tokenizer
     * if there are remaining tokens in it. It will return everything in the tokenizer as a whole sentence.
     *
     * @return the whole line of input or the rest of the sentence from the previous read line
     */
    public String readLine(){
        String line = "";

        if(tokenizer == null || !tokenizer.hasMoreTokens()){
            try {
                line = reader.readLine();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }else{
            line += tokenizer.nextToken("");
        }

        return line;
    }

    /**
     * This method only reads the first integer input and returns it.
     *
     * @return  single integer input
     */
    public int readInt() throws SnomException{
        try{
            return Integer.parseInt(readWord());
        }catch(NumberFormatException e){
            throw new SnomException("Oops! You have entered invalid task numbers, please try again!");
        }
    }

    /**
     * This method only reads the first double input and returns it.
     *
     * @return  single double input
     */
    public double readDouble(){
        return Double.parseDouble(readWord());
    }
}
