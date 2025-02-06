package managers;

import statuses.StatusTask;
import tasks.*;
import util.Managers;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;

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
        if (task.getId() == 0) {
            do {
                taskId++;
                task.setId(taskId);
            } while (checkContainsAllTasks(task));
            tasks.put(taskId, task);
        } else if (!checkContainsAllTasks(task)) {
            tasks.put(task.getId(), task);
        } else {
            System.out.println("Данные с таким id существуют в списке");
        }
    }

    @Override
    public void addSubtask(Subtask subtask) {
        Epic epic = epics.get(subtask.getEpicId());
        if (epic != null) {
            if (subtask.getId() == 0) {
                do {
                    taskId++;
                    subtask.setId(taskId);
                } while (checkContainsAllTasks(subtask));
                subtasks.put(taskId, subtask);
                epic.addSubtask(subtask);
                updateEpicStatus(subtask.getEpicId());
                updateEpicTimeInfo(subtask.getEpicId());
            } else if (!checkContainsAllTasks(subtask)) {
                subtasks.put(subtask.getId(), subtask);
                epic.addSubtask(subtask);
                updateEpicStatus(subtask.getEpicId());
                updateEpicTimeInfo(subtask.getEpicId());
            } else {
                System.out.println("Данные с таким id существуют в списке");
            }
        } else {
            System.out.println("Эпик не существует. Подзадачу невозможно создать без эпика");
        }
    }

    @Override
    public void addEpic(Epic epic) {
        if (epic.getId() == 0) {
            do {
                taskId++;
                epic.setId(taskId);
            } while (checkContainsAllTasks(epic));
            epics.put(taskId, epic);
        } else if (!checkContainsAllTasks(epic)) {
            epics.put(epic.getId(), epic);
        } else {
            System.out.println("Данные с таким id существуют в списке");
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
            updateEpicTimeInfo(subtask.getEpicId());
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
        updateEpicTimeInfo(subtask.getEpicId());

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

    private <T extends Task> boolean checkContainsAllTasks(T task) {
        if (task == null) {
            return false;
        }
        return tasks.containsKey(task.getId()) || subtasks.containsKey(task.getId()) || epics.containsKey(task.getId());
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

    private void updateEpicTimeInfo(int epicId) {
        Epic epic = epics.get(epicId);
        LocalDateTime epicStartTime = epic.getSubtasks().stream().map(subtaskId -> subtasks.get(subtaskId).
                getStartTime()).filter(Objects::nonNull).min(Comparator.naturalOrder()).orElse(null);
        LocalDateTime epicEndTime = epic.getSubtasks().stream().map(subtaskId -> subtasks.get(subtaskId).
                getEndTime()).filter(Objects::nonNull).max(Comparator.naturalOrder()).orElse(null);
        Duration epicDuration = epic.getSubtasks().stream().map(subtaskId -> subtasks.get(subtaskId).getDuration()).
                filter(Objects::nonNull).reduce(Duration.ZERO, Duration::plus);
        epic.setStartTime(epicStartTime);
        epic.setEndTime(epicEndTime);
        epic.setDuration(epicDuration);
    }

}
