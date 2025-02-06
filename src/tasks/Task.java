package tasks;

import statuses.StatusTask;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Objects;

public class Task {
    protected String name;
    protected String description;
    protected StatusTask status;
    protected int id;
    protected TaskType type = TaskType.TASK;
    protected Duration duration;
    protected LocalDateTime startTime;
    protected LocalDateTime endTime;

    public Task(String name, String description, StatusTask status, int id, Duration duration, LocalDateTime startTime) {
        this.name = name;
        this.description = description;
        this.status = status;
        this.id = id;
        this.duration = Duration.ofMinutes(duration.toMinutes());
        this.startTime = startTime;
        this.endTime = startTime.plus(duration);
    }

    public Task(String name, String description, StatusTask status, int id, Duration duration) {
        this.name = name;
        this.description = description;
        this.status = status;
        this.id = id;
        this.duration = Duration.ofMinutes(duration.toMinutes());
    }

    public Task(String name, String description, StatusTask status, int id, LocalDateTime startTime) {
        this.name = name;
        this.description = description;
        this.status = status;
        this.id = id;
        this.startTime = startTime;
    }

    public Task(String name, String description, StatusTask status, Duration duration, LocalDateTime startTime) {
        this.name = name;
        this.description = description;
        this.status = status;
        this.duration = Duration.ofMinutes(duration.toMinutes());
        this.startTime = startTime;
        this.endTime = startTime.plus(duration);
    }

    public Task(String name, String description, int id, Duration duration, LocalDateTime startTime) {
        this.name = name;
        this.description = description;
        this.id = id;
        this.status = StatusTask.NEW;
        this.duration = Duration.ofMinutes(duration.toMinutes());
        this.startTime = startTime;
        this.endTime = startTime.plus(duration);
    }

    public Task(String name, String description, Duration duration, LocalDateTime startTime) {
        this.name = name;
        this.description = description;
        this.status = StatusTask.NEW;
        this.duration = Duration.ofMinutes(duration.toMinutes());
        this.startTime = startTime;
        this.endTime = startTime.plus(duration);
    }

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

    public LocalDateTime getEndTime() {
        return endTime;
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
        if (taskInfo[6].equals("null") && taskInfo[7].equals("null")) {
            return new Task(taskInfo[2], taskInfo[4], StatusTask.valueOf(taskInfo[3]), Integer.parseInt(taskInfo[0]));
        } else if (taskInfo[6].equals("null")) {
            return new Task(taskInfo[2], taskInfo[4], StatusTask.valueOf(taskInfo[3]), Integer.parseInt(taskInfo[0]),
                    LocalDateTime.parse(taskInfo[7]));
        } else if (taskInfo[7].equals("null")) {
            return new Task(taskInfo[2], taskInfo[4], StatusTask.valueOf(taskInfo[3]), Integer.parseInt(taskInfo[0]),
                    Duration.parse(taskInfo[6]));
        } else {
            return new Task(taskInfo[2], taskInfo[4], StatusTask.valueOf(taskInfo[3]), Integer.parseInt(taskInfo[0]),
                    Duration.parse(taskInfo[6]), LocalDateTime.parse(taskInfo[7]));
        }
    }

    @Override
    public String toString() {
        // TODO: Поработать над форматом вывода duration, startTime, endTime
        return String.format("%d,%s,%s,%s,%s,,%s,%s,%s", id, type, name, status, description, duration, startTime,
                endTime);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Task task = (Task) o;
        if (id == task.id) return true;
        return Objects.equals(name, task.name) && Objects.equals(description, task.description) && status == task.status
                && Objects.equals(duration, task.duration) && Objects.equals(startTime, task.startTime)
                && Objects.equals(endTime, task.endTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, description, status, id, duration, startTime, endTime);
    }
}
