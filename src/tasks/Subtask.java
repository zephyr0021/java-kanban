package tasks;

import statuses.StatusTask;

import java.time.Duration;
import java.time.LocalDateTime;

public class Subtask extends Task {
    protected TaskType type = TaskType.SUBTASK;
    private int epicId;

    public Subtask(String name, String description, StatusTask status, int epicId, int id, Duration duration,
                   LocalDateTime startTime) {
        super(name, description, status, id, duration, startTime);
        this.epicId = epicId;
    }

    public Subtask(String name, String description, StatusTask status, int epicId, int id, Duration duration) {
        super(name, description, status, id, duration);
        this.epicId = epicId;
    }

    public Subtask(String name, String description, StatusTask status, int epicId, int id, LocalDateTime startTime) {
        super(name, description, status, id, startTime);
        this.epicId = epicId;
    }

    public Subtask(String name, String description, StatusTask status, int epicId, Duration duration,
                   LocalDateTime startTime) {
        super(name, description, status, duration, startTime);
        this.epicId = epicId;
    }

    public Subtask(String name, String description, int epicId, Duration duration, LocalDateTime startTime) {
        super(name, description, duration, startTime);
        this.epicId = epicId;
    }

    public Subtask(String name, String description, int epicId, int id, Duration duration, LocalDateTime startTime) {
        super(name, description, id, duration, startTime);
        this.epicId = epicId;
    }

    public Subtask(String name, String description, StatusTask status, int epicId, int id) {
        super(name, description, status, id);
        this.epicId = epicId;
    }

    public Subtask(String name, String description, StatusTask status, int epicId) {
        super(name, description, status);
        this.epicId = epicId;
    }

    public Subtask(String name, String description, int epicId, int id) {
        super(name, description, id);
        this.epicId = epicId;
    }

    public Subtask(String name, String description, int epicId) {
        super(name, description);
        this.epicId = epicId;
    }

    public int getEpicId() {
        return epicId;
    }

    public void setEpicId(int epicId) {
        this.epicId = epicId;
    }

    public static Subtask fromString(String value) {
        String[] taskInfo = value.split(",");
        if (taskInfo[6].equals("null") && taskInfo[7].equals("null")) {
            return new Subtask(taskInfo[2], taskInfo[4], StatusTask.valueOf(taskInfo[3]), Integer.parseInt(taskInfo[5]),
                    Integer.parseInt(taskInfo[0]));
        } else if (taskInfo[6].equals("null")) {
            return new Subtask(taskInfo[2], taskInfo[4], StatusTask.valueOf(taskInfo[3]), Integer.parseInt(taskInfo[5]),
                    Integer.parseInt(taskInfo[0]), LocalDateTime.parse(taskInfo[7]));
        } else if (taskInfo[7].equals("null")) {
            return new Subtask(taskInfo[2], taskInfo[4], StatusTask.valueOf(taskInfo[3]), Integer.parseInt(taskInfo[5]),
                    Integer.parseInt(taskInfo[0]), Duration.parse(taskInfo[6]));
        } else {
            return new Subtask(taskInfo[2], taskInfo[4], StatusTask.valueOf(taskInfo[3]), Integer.parseInt(taskInfo[5]),
                    Integer.parseInt(taskInfo[0]), Duration.parse(taskInfo[6]), LocalDateTime.parse(taskInfo[7]));
        }
    }

    @Override
    public String toString() {
        return String.format("%d,%s,%s,%s,%s,%d,%s,%s,%s", id, type, name, status, description, epicId, duration,
                startTime, endTime);
    }
}
