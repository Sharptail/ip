package snom.storage;

import java.io.IOException;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.List;

import snom.exceptions.SnomException;
import snom.model.task.Deadline;
import snom.model.task.Event;
import snom.model.task.Task;
import snom.model.task.TaskList;
import snom.model.task.Todo;

/**
 * Manages storage related process for Snom
 * Eg. Import task, save task
 */
public class StorageManager extends FileManager{
    public StorageManager(String filename){
        super(filename);
        super.createFolder();
        super.createFile();
    }

    /**
     * Returns an array list of {@code Task} from input list of strings
     *
     * @return               array list of {@code Task}
     * @throws SnomException if invalid date for deadline or event
     */
    public ArrayList<Task> importTask() throws SnomException{
        List<String> lines = super.readFile();
        ArrayList<Task> taskList = new ArrayList<>();
        for(String line: lines) {
            String attr[] = line.split(",");
            switch (attr[0]) {
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
        return taskList;
    }

    /**
     * Writes the given {@code TaskList} into the save file
     *
     * @param taskList list of {@code Task}
     */
    public void saveFile(TaskList taskList){
        try {
            Files.writeString(path, "");
            for(Task task: taskList.getList()){
                Files.writeString(path, task.getSaveString() + "\n", StandardOpenOption.APPEND);
            }
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }
}