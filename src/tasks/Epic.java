package tasks;

import statuses.StatusTask;

import java.util.ArrayList;

public class Epic extends Task {
    protected TaskType type = TaskType.EPIC;
    private ArrayList<Integer> subtasks;

    public Epic(String name, String description, StatusTask status, int id) {
        super(name, description, status, id);
        this.subtasks = new ArrayList<>();
    }

    public Epic(String name, String description, int id) {
        super(name, description, id);
        this.subtasks = new ArrayList<>();
    }

    public Epic(String name, String description) {
        super(name, description);
        this.subtasks = new ArrayList<>();
    }

    public void addSubtask(Subtask subtask) {
        if (checkNotContainsSubtask(subtask)) {
            subtasks.add(subtask.getId());
            subtask.setEpicId(this.id);
        } else {
            System.out.println("Данная подзадача уже существует в списке");
        }
    }

    public void removeSubtask(Integer subtaskId) {
        subtasks.remove(subtaskId);
    }

    public void clearSubtaskList() {
        subtasks.clear();
    }

    public ArrayList<Integer> getSubtasks() {
        return subtasks;
    }

    public void setSubtasks(ArrayList<Integer> subtasks) {
        this.subtasks = subtasks;
    }

    public static Epic fromString(String value) {
        String[] taskInfo = value.split(",");
        return new Epic(taskInfo[2], taskInfo[4], StatusTask.valueOf(taskInfo[3]), Integer.parseInt(taskInfo[0]));
    }

    @Override
    public String toString() {
        return String.format("%d,%s,%s,%s,%s,", id, type, name, status, description);
    }

    private boolean checkNotContainsSubtask(Subtask subtask) {
        if (subtask == null) {
            return false;
        }
        return !subtasks.contains(subtask.getId());
    }
}
