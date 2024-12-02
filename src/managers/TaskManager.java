package managers;

import tasks.*;
import java.util.ArrayList;
import java.util.List;

public interface TaskManager {
    ArrayList<Task> getTasks();

    Task getTask(int id);

    ArrayList<Subtask> getSubtasks();

    Subtask getSubtask(int id);

    ArrayList<Epic> getEpics();

    Epic getEpic(int id);

    ArrayList<Subtask> getEpicSubtasks(int epicId);

    List<Task> getHistory();

    void addTask(Task task);

    void addSubtask(Subtask subtask);

    void addEpic(Epic epic);

    void deleteTask(int id);

    void deleteSubtask(int id);

    void deleteEpic(int id);

    void clearTasks();

    void clearSubtasks();

    void clearEpics();

    void updateTask(Task task);

    void updateSubtask(Subtask subtask);

    void updateEpic(Epic epic);
}
