package managers;

import exceptions.ManagerSaveException;
import tasks.Epic;
import tasks.Subtask;
import tasks.Task;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class FileBackedTaskManager extends InMemoryTaskManager {

    File manager = new File("manager.csv");
    @Override
    public void addTask(Task task) {
        super.addTask(task);
        save();
    }

    @Override
    public void addSubtask(Subtask subtask) {
        super.addSubtask(subtask);
        save();
    }

    @Override
    public void addEpic(Epic epic) {
        super.addEpic(epic);
        save();
    }

    @Override
    public void deleteTask(int id) {
        super.deleteTask(id);
        save();
    }

    @Override
    public void deleteSubtask(int id) {
        super.deleteSubtask(id);
        save();
    }

    @Override
    public void deleteEpic(int id) {
        super.deleteEpic(id);
        save();
    }

    @Override
    public void clearTasks() {
        super.clearTasks();
        save();
    }

    @Override
    public void clearSubtasks() {
        super.clearSubtasks();
        save();
    }

    @Override
    public void clearEpics() {
        super.clearEpics();
    }

    @Override
    public void updateTask(Task task) {
        super.updateTask(task);
        save();
    }

    @Override
    public void updateSubtask(Subtask subtask) {
        super.updateSubtask(subtask);
        save();
    }

    @Override
    public void updateEpic(Epic epic) {
        super.updateEpic(epic);
        save();
    }


    private void save() throws ManagerSaveException {
        try (FileWriter writer = new FileWriter(manager)) {
            writer.write("id,type,name,status,description,epic\n");
            for (Task task : getTasks()) {
                writer.write(String.format("%s\n", task.toString()));
            }

            for (Subtask subtask : getSubtasks()) {
                writer.write(String.format("%s\n", subtask.toString()));
            }

            for (Epic epic : getEpics()) {
                writer.write(String.format("%s\n", epic.toString()));
            }
        }
        catch (IOException e) {
            throw new ManagerSaveException("Возникла ошибка при автосохранении менеджера", manager);
        }
    }
}
