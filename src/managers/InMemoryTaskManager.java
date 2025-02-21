package managers;

import exceptions.IntersectionException;
import exceptions.NotFoundException;
import statuses.StatusTask;
import tasks.*;
import util.Managers;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

public class InMemoryTaskManager implements TaskManager {
    private final HashMap<Integer, Task> tasks = new HashMap<>();
    private final HashMap<Integer, Subtask> subtasks = new HashMap<>();
    private final HashMap<Integer, Epic> epics = new HashMap<>();
    private final HistoryManager historyManager = Managers.getDefaultHistory();
    private int taskId = 0;
    private final Comparator<Task> comparator = Comparator.comparing(Task::getStartTime,
            Comparator.nullsLast(Comparator.naturalOrder()));
    private final TreeSet<Task> prioritizedTasks = new TreeSet<>(comparator);

    @Override
    public ArrayList<Task> getTasks() {
        return new ArrayList<>(tasks.values());
    }

    @Override
    public Task getTask(int id) throws NotFoundException {
        Task task = tasks.get(id);
        if (task == null) {
            throw new NotFoundException("Задача с указанным id не найдена");
        }
        historyManager.add(task);
        return task;
    }

    @Override
    public ArrayList<Subtask> getSubtasks() {
        return new ArrayList<>(subtasks.values());
    }

    @Override
    public Subtask getSubtask(int id) throws NotFoundException {
        Subtask subtask = subtasks.get(id);
        if (subtask == null) {
            throw new NotFoundException("Подзадача с указанным id не найдена");
        }
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
        if (epic != null) {
            return epic.getSubtasks().stream().map(subtasks::get).collect(Collectors.toCollection(ArrayList::new));
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
    public void addTask(Task task) throws IntersectionException {
        if (isIntersectionTaskTime(task)) {
            throw new IntersectionException("Задача пересекается с другими");
        } else if (task.getId() == 0) {
            do {
                taskId++;
                task.setId(taskId);
            } while (checkContainsAllTasks(task));
            tasks.put(taskId, task);
            prioritizedTasks.add(task);
        } else if (!checkContainsAllTasks(task)) {
            tasks.put(task.getId(), task);
            prioritizedTasks.add(task);
        } else {
            throw new IntersectionException("Задача пересекается с другими");
        }
    }

    @Override
    public void addSubtask(Subtask subtask) throws IntersectionException, NotFoundException {
        Epic epic = epics.get(subtask.getEpicId());
        if (isIntersectionTaskTime(subtask)) {
            throw new IntersectionException("Подзадача пересекается по времени с другими");
        } else if (epic != null) {
            if (subtask.getId() == 0) {
                do {
                    taskId++;
                    subtask.setId(taskId);
                } while (checkContainsAllTasks(subtask));
                subtasks.put(taskId, subtask);
                prioritizedTasks.add(subtask);
                epic.addSubtask(subtask);
                updateEpicStatus(subtask.getEpicId());
                updateEpicTimeInfo(subtask.getEpicId());
            } else if (!checkContainsAllTasks(subtask)) {
                subtasks.put(subtask.getId(), subtask);
                prioritizedTasks.add(subtask);
                epic.addSubtask(subtask);
                updateEpicStatus(subtask.getEpicId());
                updateEpicTimeInfo(subtask.getEpicId());
            } else {
                throw new IntersectionException("Подзадача пересекается по времени с другими");
            }
        } else {
            throw new NotFoundException("Эпика не существует. Задачу невозможно создать без эпика");
        }
    }

    @Override
    public void addEpic(Epic epic) {
        if (isIntersectionTaskTime(epic)) {
            System.out.println("Эпик пересекается по времени с другими");
        } else if (epic.getId() == 0) {
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
    public void deleteTask(int id) throws NotFoundException {
        Task task = tasks.get(id);
        if (task == null) {
            throw new NotFoundException("Задача не найдена");
        }
        prioritizedTasks.remove(tasks.get(id));
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
        prioritizedTasks.remove(subtask);
        subtasks.remove(id);
        historyManager.remove(id);
    }

    @Override
    public void deleteEpic(int id) {
        Epic epic = epics.remove(id);
        historyManager.remove(id);
        if (epic != null) {
            epic.getSubtasks().forEach(subtaskId -> {
                prioritizedTasks.remove(subtasks.get(subtaskId));
                subtasks.remove(subtaskId);
                historyManager.remove(subtaskId);
            });
        }
    }

    @Override
    public void clearTasks() {
        tasks.values().forEach(prioritizedTasks::remove);
        tasks.keySet().forEach(historyManager::remove);
        tasks.clear();
    }

    @Override
    public void clearSubtasks() {
        epics.values().forEach(epic -> {
            epic.clearSubtaskList();
            epic.setStatus(StatusTask.NEW);
        });
        subtasks.values().forEach(prioritizedTasks::remove);
        subtasks.keySet().forEach(historyManager::remove);
        subtasks.clear();
    }

    @Override
    public void clearEpics() {
        epics.values().forEach(epic -> {
            historyManager.remove(epic.getId());
            epic.getSubtasks().forEach(historyManager::remove);
        });
        epics.clear();
        subtasks.values().forEach(prioritizedTasks::remove);
        subtasks.clear();
    }

    @Override
    public void updateTask(Task task) throws NotFoundException {
        int id = task.getId();
        if (tasks.get(id) == null) {
            throw new NotFoundException("Не существует такой задачи");
        }
        prioritizedTasks.remove(tasks.get(id));
        prioritizedTasks.add(task);
        tasks.put(id, task);
    }

    @Override
    public void updateSubtask(Subtask subtask) {
        int id = subtask.getId();
        if (subtasks.get(id) == null) {
            System.out.println("Не существует такой подзадачи!");
            return;
        }
        prioritizedTasks.remove(subtasks.get(id));
        prioritizedTasks.add(subtask);
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

    @Override
    public TreeSet<Task> getPrioritizedTasks() {
        return prioritizedTasks.stream().filter(task -> Objects.nonNull(task.getStartTime()))
                .collect(Collectors.toCollection(() -> new TreeSet<>(comparator)));
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
        LocalDateTime epicStartTime = epic.getSubtasks().stream().map(subtaskId -> subtasks.get(subtaskId)
                .getStartTime()).filter(Objects::nonNull).min(Comparator.naturalOrder()).orElse(null);
        LocalDateTime epicEndTime = epic.getSubtasks().stream().map(subtaskId -> subtasks.get(subtaskId)
                .getEndTime()).filter(Objects::nonNull).max(Comparator.naturalOrder()).orElse(null);
        Duration epicDuration = epic.getSubtasks().stream().map(subtaskId -> subtasks.get(subtaskId).getDuration())
                .filter(Objects::nonNull).reduce(Duration.ZERO, Duration::plus);
        epic.setStartTime(epicStartTime);
        epic.setEndTime(epicEndTime);
        epic.setDuration(epicDuration);
    }

    private boolean isIntersectionTaskTime(Task task) {
        if (!Objects.nonNull(task.getStartTime()) && !Objects.nonNull(task.getEndTime())) {
            return false;
        } else {
            List<Task> intersectionsTasks = getPrioritizedTasks().stream().filter(prioritezedTask ->
                            Objects.nonNull(prioritezedTask.getEndTime()))
                    .filter(prioritizedTask -> (prioritizedTask.getStartTime().isBefore(task.getStartTime())
                            && prioritizedTask.getEndTime().isAfter(task.getStartTime())) ||
                            (prioritizedTask.getStartTime().equals(task.getStartTime()) && prioritizedTask.getEndTime().equals(task.getEndTime()))).toList();
            return !intersectionsTasks.isEmpty();
        }
    }

}
