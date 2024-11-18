import java.util.HashMap;

public class TaskManager {
    private static HashMap<Integer, Task> tasks = new HashMap<>();
    private static HashMap<Integer, Subtask> subtasks = new HashMap<>();
    private static HashMap<Integer, Epic> epics = new HashMap<>();
    private static int taskId = 0;

    public static HashMap<Integer, Task> getTasks() {
        return tasks;
    }

    public static Task getTask(int id) {
        return tasks.get(id);
    }

    public static HashMap<Integer, Subtask> getSubtasks() {
        return subtasks;
    }

    public static Subtask getSubtask(int id) {
        return subtasks.get(id);
    }

    public static HashMap<Integer, Epic> getEpics() {
        return epics;
    }

    public static Epic getEpic(int id) {
        return epics.get(id);
    }

    public static HashMap<Integer, Subtask> getEpicSubtasks(int epicId) {
        return getEpic(epicId).getSubtasks();
    }

    private static boolean checkContainsTask(Task task) {
        if (task == null) {
            return false;
        }
        return !tasks.containsKey(task.getId());
    }

    private static boolean checkContainsSubtask(Subtask subtask) {
        if (subtask == null) {
            return false;
        }
        return !subtasks.containsKey(subtask.getId());
    }

    private static boolean checkContainsEpic(Epic epic) {
        if (epic == null) {
            return false;
        }
        return !epics.containsKey(epic.getId());
    }

    public static void addTask(Task task) {
        if (checkContainsTask(task)) {
            taskId++;
            task.setId(taskId);
            tasks.put(task.getId(), task);
        } else {
            System.out.println("Данная задача уже существует в списке!");
        }
    }

    public static void addSubtask(Subtask subtask, int epicId) {
        Epic epic = getEpic(epicId);
        if (epic != null) {
            if (checkContainsSubtask(subtask)) {
                taskId++;
                subtask.setId(taskId);
                subtasks.put(taskId, subtask);
                epic.addSubtask(subtask);
            } else {
                System.out.println("Данная подзадача уже существует в списке!");
            }
        } else {
            System.out.println("Эпик не существует. Подзадачу невозможно создать без эпика");
        }
    }

    public static void addEpic(Epic epic) {
        if (checkContainsEpic(epic)) {
            taskId++;
            epic.setId(taskId);
            epics.put(taskId, epic);
        } else {
            System.out.println("Данный эпик уже существует в списке!");
        }
    }





}
