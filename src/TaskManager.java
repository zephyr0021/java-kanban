import java.util.ArrayList;
import java.util.HashMap;

public class TaskManager {
    private final HashMap<Integer, Task> tasks = new HashMap<>();
    private final HashMap<Integer, Subtask> subtasks = new HashMap<>();
    private final HashMap<Integer, Epic> epics = new HashMap<>();
    private int taskId = 0;

    public ArrayList<Task> getTasks() {
        return new ArrayList<>(tasks.values());
    }

    public Task getTask(int id) {
        return tasks.get(id);
    }

    public ArrayList<Subtask> getSubtasks() {
        return new ArrayList<>(subtasks.values());
    }

    public Subtask getSubtask(int id) {
        return subtasks.get(id);
    }

    public ArrayList<Epic> getEpics() {
        return new ArrayList<>(epics.values());
    }

    public Epic getEpic(int id) {
        return epics.get(id);
    }

    public ArrayList<Subtask> getEpicSubtasks(int epicId) {
        Epic epic = getEpic(epicId);
        ArrayList<Subtask> subtasks = new ArrayList<>();
        if (epic != null) {
            for (Integer subtaskId : epic.getSubtasks()) {
                subtasks.add(getSubtask(subtaskId));
            }
            return subtasks;
        } else {
            System.out.println("Эпик не найден!");
            return null;

        }
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
                updateEpicStatus(subtask.getEpicId());
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
            updateEpicStatus(subtask.getEpicId());
        }
        subtasks.remove(id);
    }

    public void deleteEpic(int id) {
        Epic epic = epics.remove(id);
        if (epic != null) {
            for (Integer subtaskId : epic.getSubtasks()) {
                subtasks.remove(subtaskId);
            }
        }


    }

    public void clearTasks() {
        tasks.clear();
    }

    public void clearSubtasks() {
        for (Epic epic : epics.values()) {
            epic.clearSubtaskList();
            epic.setStatus(StatusTask.NEW);
        }
        subtasks.clear();
    }

    public void clearEpics() {
        // Такой костыль получился, потому что нельзя одновременно пробежаться по hashmap и удалять элементы
        epics.clear();
        subtasks.clear();
    }

    public void updateTask(Task task) {
        int id = task.getId();
        if (getTask(id) == null) {
            System.out.println("Не существует такой задачи");
            return;
        }

        tasks.put(id, task);
    }

    public void updateSubtask(Subtask subtask) {
        int id = subtask.getId();
        if (getSubtask(id) == null) {
            System.out.println("Не существует такой подзадачи!");
            return;
        }

        subtasks.put(id, subtask);
        updateEpicStatus(subtask.getEpicId());
    }

    public void updateEpic(Epic epic) {
        int id = epic.getId();
        if (getEpic(id) == null) {
            System.out.println("Не существует такого эпика");
        }

        Epic oldEpic = getEpic(id);
        epic.setSubtasks(oldEpic.getSubtasks());
        epic.setStatus(oldEpic.getStatus());
        epics.put(id, epic);
    }

    private boolean checkNotContainsTask(Task task) {
        if (task == null) {
            return false;
        }
        return !tasks.containsKey(task.getId()) && task.getId() == 0;
    }

    private boolean checkNotContainsSubtask(Subtask subtask) {
        if (subtask == null) {
            return false;
        }
        return !subtasks.containsKey(subtask.getId()) && subtask.getId() == 0;
    }

    private boolean checkNotContainsEpic(Epic epic) {
        if (epic == null) {
            return false;
        }
        return !epics.containsKey(epic.getId()) && epic.getId() == 0;
    }

    private void updateEpicStatus(int epicId) {
        Epic epic = getEpic(epicId);
        int countNewStatusPoints = 0;
        int countDoneStatusPoints = 0;
        for (Integer subtaskId : epic.getSubtasks()) {
            if (getSubtask(subtaskId).getStatus() == StatusTask.NEW) {
                countNewStatusPoints++;
            } else if (getSubtask(subtaskId).getStatus() == StatusTask.DONE) {
                countDoneStatusPoints++;
            }
        }
        if (countNewStatusPoints == epic.getSubtasks().size() || epic.getSubtasks().isEmpty()) {
            epic.setStatus(StatusTask.NEW);
        } else if (countDoneStatusPoints == epic.getSubtasks().size()) {
            epic.setStatus(StatusTask.DONE);
        } else {
            epic.setStatus(StatusTask.IN_PROGRESS);
        }

    }

}
