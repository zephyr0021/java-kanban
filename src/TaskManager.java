import java.util.ArrayList;
import java.util.HashMap;

public class TaskManager {
    private static final HashMap<Integer, Task> tasks = new HashMap<>();
    private static final HashMap<Integer, Subtask> subtasks = new HashMap<>();
    private static final HashMap<Integer, Epic> epics = new HashMap<>();
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
        Epic epic = getEpic(epicId);
        if (epic != null) {
            return epic.getSubtasks();
        } else {
            System.out.println("Эпик не найден!");
            return null;

        }
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

    public static void deleteTask(int id) {
        tasks.remove(id);
    }

    public static void deleteSubtask(int id) {
        Subtask subtask = subtasks.get(id);
        if (subtask != null) {
            Epic epic = epics.get(subtask.getEpicId());
            epic.removeSubtask(id);
        }
        subtasks.remove(id);
    }

    public static void deleteEpic(int id) {
        Epic epic = epics.get(id);
        if (epic != null) {
            // Такой костыль получился, потому что нельзя одновременно пробежаться по hashmap и удалять элементы
            HashMap<Integer, Subtask> epicSubtasks = epic.getSubtasks();
            ArrayList<Integer> subtasksIds = new ArrayList<>(epicSubtasks.keySet());

            for (Integer subtaskId : subtasksIds) {
                deleteSubtask(subtaskId);
            }
        }
        epics.remove(id);


    }

    public static void clearTasks() {
        tasks.clear();
    }

    public static void clearSubtasks() {
        // Такой костыль получился, потому что нельзя одновременно пробежаться по hashmap и удалять элементы
        ArrayList<Integer> subtasksIds = new ArrayList<>(subtasks.keySet());

        for (Integer subtaskId : subtasksIds) {
            deleteSubtask(subtaskId);
        }
    }

    public static void clearEpics() {
        // Такой костыль получился, потому что нельзя одновременно пробежаться по hashmap и удалять элементы
        ArrayList<Integer> epicsIds = new ArrayList<>(epics.keySet());

        for (Integer epicId : epicsIds) {
            deleteEpic(epicId);
        }
    }

}
