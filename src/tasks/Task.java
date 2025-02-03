package tasks;

import statuses.StatusTask;

import java.util.Objects;

public class Task {
    protected String name;
    protected String description;
    protected StatusTask status;
    protected int id;
    protected TaskType type = TaskType.TASK;

    public Task(String name, String description, StatusTask status, int id) {
        this.name = name;
        this.description = description;
        this.status = status;
        this.id = id;
    }

    public Task(String name, String description, StatusTask status) {
        this.name = name;
        this.description = description;
        this.status = status;
    }

    public Task(String name, String description, int id) {
        this.name = name;
        this.description = description;
        this.id = id;
        this.status = StatusTask.NEW;
    }

    public Task(String name, String description) {
        this.name = name;
        this.description = description;
        this.status = StatusTask.NEW;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public StatusTask getStatus() {
        return status;
    }

    public int getId() {
        return id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setStatus(StatusTask status) {
        this.status = status;
    }


    public static Task fromString(String value) {
        String[] taskInfo = value.split(",");
        return new Task(taskInfo[2], taskInfo[4], StatusTask.valueOf(taskInfo[3]), Integer.parseInt(taskInfo[0]));
    }

    @Override
    public String toString() {
        return String.format("%d,%s,%s,%s,%s,", id, type, name, status, description);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Task task = (Task) o;
        if (id == task.id) return true;
        return Objects.equals(name, task.name) && Objects.equals(description, task.description) && status == task.status;
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, description, status, id);
    }
}
