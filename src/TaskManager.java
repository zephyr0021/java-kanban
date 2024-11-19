import java.util.ArrayList;
import java.util.HashMap;

public class TaskManager {
    private final HashMap<Integer, Task> tasks = new HashMap<>();
    private final HashMap<Integer, Subtask> subtasks = new HashMap<>();
    private final HashMap<Integer, Epic> epics = new HashMap<>();
    private int taskId = 0;

    public HashMap<Integer, Task> getTasks() {
        return tasks;
    }

    public Task getTask(int id) {
        return tasks.get(id);
    }

    public HashMap<Integer, Subtask> getSubtasks() {
        return subtasks;
    }

    public Subtask getSubtask(int id) {
        return subtasks.get(id);
    }

    public HashMap<Integer, Epic> getEpics() {
        return epics;
    }

    public Epic getEpic(int id) {
        return epics.get(id);
    }

    public ArrayList<Integer> getEpicSubtasks(int epicId) {
        Epic epic = getEpic(epicId);
        if (epic != null) {
            return epic.getSubtasksIds();
        } else {
            System.out.println("Эпик не найден!");
            return null;

        }
    }

    private boolean checkNotContainsTask(Task task) {
        if (task == null) {
            return false;
        }
        return !tasks.containsKey(task.getId());
    }

    private boolean checkNotContainsSubtask(Subtask subtask) {
        if (subtask == null) {
            return false;
        }
        return !subtasks.containsKey(subtask.getId());
    }

    private boolean checkNotContainsEpic(Epic epic) {
        if (epic == null) {
            return false;
        }
        return !epics.containsKey(epic.getId());
    }

    public void addTask(Task task) {
        if (checkNotContainsTask(task)) {
            taskId++;
            task.setId(taskId);
            tasks.put(task.getId(), task);
        } else {
            System.out.println("Данная задача уже существует в списке!");
        }
    }

    public void addSubtask(Subtask subtask) {
        Epic epic = getEpic(subtask.getEpicId());
        if (epic != null) {
            if (checkNotContainsSubtask(subtask)) {
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

    public void addEpic(Epic epic) {
        if (checkNotContainsEpic(epic)) {
            taskId++;
            epic.setId(taskId);
            epics.put(taskId, epic);
        } else {
            System.out.println("Данный эпик уже существует в списке!");
        }
    }

    public void deleteTask(int id) {
        tasks.remove(id);
    }

    public void deleteSubtask(int id) {
        Subtask subtask = getSubtask(id);
        if (subtask != null) {
            Epic epic = getEpic(subtask.getEpicId());
            epic.removeSubtask(id);
        }
        subtasks.remove(id);
    }

    public void deleteEpic(int id) {
        Epic epic = getEpic(id);
        if (epic != null) {
            // Такой костыль получился, потому что нельзя одновременно пробежаться по arrayList и удалять элементы
            ArrayList<Integer> epicSubtasksIds = new ArrayList<>(epic.getSubtasksIds());
            for (Integer subtaskId : epicSubtasksIds) {
                deleteSubtask(subtaskId);
            }
        }
        epics.remove(id);


    }

    public void clearTasks() {
        tasks.clear();
    }

    public void clearSubtasks() {
        // Такой костыль получился, потому что нельзя одновременно пробежаться по hashmap и удалять элементы
        ArrayList<Integer> subtasksIds = new ArrayList<>(subtasks.keySet());

        for (Integer subtaskId : subtasksIds) {
            deleteSubtask(subtaskId);
        }
    }

    public void clearEpics() {
        // Такой костыль получился, потому что нельзя одновременно пробежаться по hashmap и удалять элементы
        ArrayList<Integer> epicsIds = new ArrayList<>(epics.keySet());

        for (Integer epicId : epicsIds) {
            deleteEpic(epicId);
        }
    }

    public void updateTask(Task task, int id) {
        task.setId(id);
        tasks.put(id, task);
    }

    public void updateSubtask(Subtask subtask, int id) {
        subtask.setId(id);
        subtasks.put(id, subtask);
        Epic epic = getEpic(subtask.getEpicId());
        boolean needTurnEpicToNew = false;
        boolean needTurnEpicToDone = false;
        switch (subtask.getStatus()) {
            case NEW:
                for (Integer subtaskId : epic.getSubtasksIds()) {
                    needTurnEpicToNew = getSubtask(subtaskId).getStatus() == StatusTask.NEW;
                }
                break;
            case DONE:
                for (Integer subtaskId : epic.getSubtasksIds()) {
                    needTurnEpicToDone = getSubtask(subtaskId).getStatus() == StatusTask.DONE;
                }
                break;
        }
        if (needTurnEpicToNew) {
            epic.setStatus(StatusTask.NEW);
        } else if (needTurnEpicToDone) {
            epic.setStatus(StatusTask.DONE);
        } else {
            epic.setStatus(StatusTask.IN_PROGRESS);
        }
    }

}
