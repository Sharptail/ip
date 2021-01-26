/**
 * This class handles any files related process for Snom
 * Eg. Create, Write, Delete save files.
 *
 * @author Sharptail
 */

import java.io.IOException;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.List;

public class SnomFile {
    private Path path;

    public SnomFile(String directory, String filename){
        this.path = Paths.get(directory + "/" + filename);
    }

    /**
     * Creates the directory and the file with the given path
     */
    public void init(){
        createFolder();
        createFile();
    }

    /**
     * Creates directory with the given path
     */
    public void createFolder(){
        try{
            Files.createDirectories(path.getParent());
        }catch(FileAlreadyExistsException e){
            // Ignored
        }catch(IOException e){
            System.out.println(e.getMessage());
        }
    }

    /**
     * Creates file with the given path
     */
    public void createFile(){
        try{
            Files.createFile(path);
        }catch(FileAlreadyExistsException e){
            // Ignored
        }catch(IOException e){
            System.out.println(e.getMessage());
        }
    }

    /**
     * Returns an array list of tasks after reading from a save file.
     *
     * @return array list of tasks
     */
    public ArrayList<Task> readFile() throws SnomException{
        ArrayList<Task> taskList = new ArrayList<>();
        try {
            List<String> lines = Files.readAllLines(path);
            for(String line: lines){
                String attr[] = line.split(",");
                switch(attr[0]){
                    case "T":
                        Todo todo = new Todo(attr[2]);
                        todo.setStatus(Boolean.parseBoolean(attr[1]));
                        taskList.add(todo);
                        break;
                    case "D":
                        Deadline deadline = new Deadline(attr[2], attr[3]);
                        deadline.setStatus(Boolean.parseBoolean(attr[1]));
                        taskList.add(deadline);
                        break;
                    case "E":
                        Event event = new Event(attr[2], attr[3]);
                        event.setStatus(Boolean.parseBoolean(attr[1]));
                        taskList.add(event);
                        break;
                    default:
                }
            }
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
        return taskList;
    }

    /**
     * Writes the given task list into the save file
     *
     * @param list list of tasks
     */
    public void saveFile(ArrayList<Task> list){
        try {
            Files.writeString(path, "");
            for(Task task: list){
                Files.writeString(path, task.toSaveString() + "\n", StandardOpenOption.APPEND);
            }
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }
}
