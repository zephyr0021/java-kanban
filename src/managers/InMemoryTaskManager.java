package managers;

import statuses.StatusTask;
import tasks.*;
import util.Managers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class InMemoryTaskManager implements TaskManager {
    private final HashMap<Integer, Task> tasks = new HashMap<>();
    private final HashMap<Integer, Subtask> subtasks = new HashMap<>();
    private final HashMap<Integer, Epic> epics = new HashMap<>();
    private final HistoryManager historyManager = Managers.getDefaultHistory();
    private int taskId = 0;

    @Override
    public ArrayList<Task> getTasks() {
        return new ArrayList<>(tasks.values());
    }

    @Override
    public Task getTask(int id) {
        Task task = tasks.get(id);
        historyManager.add(task);
        return task;
    }

    @Override
    public ArrayList<Subtask> getSubtasks() {
        return new ArrayList<>(subtasks.values());
    }

    @Override
    public Subtask getSubtask(int id) {
        Subtask subtask = subtasks.get(id);
        historyManager.add(subtask);
        return subtask;
    }

    @Override
    public ArrayList<Epic> getEpics() {
        return new ArrayList<>(epics.values());
    }

    @Override
    public Epic getEpic(int id) {
        Epic epic = epics.get(id);
        historyManager.add(epic);
        return epic;
    }

    @Override
    public ArrayList<Subtask> getEpicSubtasks(int epicId) {
        Epic epic = epics.get(epicId);
        ArrayList<Subtask> epicSubtasks = new ArrayList<>();
        if (epic != null) {
            for (Integer subtaskId : epic.getSubtasks()) {
                epicSubtasks.add(subtasks.get(subtaskId));
            }
            return epicSubtasks;
        } else {
            System.out.println("Эпик не найден!");
            return null;

        }
    }

    @Override
    public List<Task> getHistory() {
        return historyManager.getHistory();
    }

    @Override
    public void addTask(Task task) {
        if (checkNotContainsTask(task)) {
            taskId++;
            task.setId(taskId);
            tasks.put(task.getId(), task);
        } else {
            System.out.println("Данная задача уже существует в списке!");
        }
    }

    @Override
    public void addSubtask(Subtask subtask) {
        Epic epic = epics.get(subtask.getEpicId());
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

    @Override
    public void addEpic(Epic epic) {
        if (checkNotContainsEpic(epic)) {
            taskId++;
            epic.setId(taskId);
            epics.put(taskId, epic);
        } else {
            System.out.println("Данный эпик уже существует в списке!");
        }
    }

    @Override
    public void deleteTask(int id) {
        tasks.remove(id);
        historyManager.remove(id);
    }

    @Override
    public void deleteSubtask(int id) {
        Subtask subtask = subtasks.get(id);
        if (subtask != null) {
            Epic epic = epics.get(subtask.getEpicId());
            epic.removeSubtask(id);
            updateEpicStatus(subtask.getEpicId());
        }
        subtasks.remove(id);
        historyManager.remove(id);
    }

    @Override
    public void deleteEpic(int id) {
        Epic epic = epics.remove(id);
        historyManager.remove(id);
        if (epic != null) {
            for (Integer subtaskId : epic.getSubtasks()) {
                subtasks.remove(subtaskId);
                historyManager.remove(subtaskId);
            }
        }


    }

    @Override
    public void clearTasks() {
        for (Integer taskId : tasks.keySet()) {
            historyManager.remove(taskId);
        }
        tasks.clear();
    }

    @Override
    public void clearSubtasks() {
        for (Epic epic : epics.values()) {
            epic.clearSubtaskList();
            epic.setStatus(StatusTask.NEW);
        }
        for (Integer subtaskId : subtasks.keySet()) {
            historyManager.remove(subtaskId);
        }
        subtasks.clear();
    }

    @Override
    public void clearEpics() {
        for (Epic epic : epics.values()) {
            historyManager.remove(epic.getId());
            for (Integer subtaskId : epic.getSubtasks()) {
                historyManager.remove(subtaskId);
            }
        }
        epics.clear();
        subtasks.clear();
    }

    @Override
    public void updateTask(Task task) {
        int id = task.getId();
        if (tasks.get(id) == null) {
            System.out.println("Не существует такой задачи");
            return;
        }

        tasks.put(id, task);
    }

    @Override
    public void updateSubtask(Subtask subtask) {
        int id = subtask.getId();
        if (subtasks.get(id) == null) {
            System.out.println("Не существует такой подзадачи!");
            return;
        }

        subtasks.put(id, subtask);
        updateEpicStatus(subtask.getEpicId());

    }

    @Override
    public void updateEpic(Epic epic) {
        int id = epic.getId();
        if (epics.get(id) == null) {
            System.out.println("Не существует такого эпика");
        }

        Epic oldEpic = epics.get(id);
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
        Epic epic = epics.get(epicId);
        int countNewStatusPoints = 0;
        int countDoneStatusPoints = 0;
        for (Integer subtaskId : epic.getSubtasks()) {
            if (subtasks.get(subtaskId).getStatus() == StatusTask.NEW) {
                countNewStatusPoints++;
            } else if (subtasks.get(subtaskId).getStatus() == StatusTask.DONE) {
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
