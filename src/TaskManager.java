import java.util.HashMap;

public class TaskManager {
    public static HashMap<Integer, Task> tasks = new HashMap<>();
    public static int taskId = 0;

    public static void addTask(Task task) {
        tasks.put(++taskId, task);
        task.setId(taskId);
    }


}
