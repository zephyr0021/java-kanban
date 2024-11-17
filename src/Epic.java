import java.util.ArrayList;
import java.util.HashMap;

public class Epic extends Task{
    HashMap<Integer, Subtask> subtasks;
    public Epic(String name, String description) {
        super(name, description);
        subtasks = new HashMap<>();
    }
}
