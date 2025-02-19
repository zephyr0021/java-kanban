package managers;

import exceptions.ManagerLoadFromFileException;
import exceptions.ManagerSaveException;
import tasks.Epic;
import tasks.Subtask;
import tasks.Task;
import tasks.TaskType;

import java.io.*;
import java.util.ArrayList;

public class FileBackedTaskManager extends InMemoryTaskManager {

    private final File managerFile;

    public FileBackedTaskManager(String path) {
        this.managerFile = new File(path);
    }

    public FileBackedTaskManager(File manager) {
        this.managerFile = manager;
    }

    public FileBackedTaskManager() {
        this.managerFile = new File("manager.csv");
    }

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

    public File getManagerFile() {
        return this.managerFile;
    }

    public static FileBackedTaskManager loadFromFile(File file) throws ManagerLoadFromFileException {
        try (BufferedReader buffer = new BufferedReader(new FileReader(file))) {
            FileBackedTaskManager manager = new FileBackedTaskManager();
            ArrayList<String> tasks = new ArrayList<>(buffer.lines().toList());
            String header = "id,type,name,status,description,epic,duration,start_time,end_time";
            tasks.remove(header);
            tasks.forEach(task -> {
                String[] taskInfo = task.split(",");
                switch (TaskType.valueOf(taskInfo[1])) {
                    case TaskType.TASK:
                        manager.addTask(Task.fromString(task));
                        break;
                    case TaskType.SUBTASK:
                        manager.addSubtask(Subtask.fromString(task));
                        break;
                    case TaskType.EPIC:
                        manager.addEpic(Epic.fromString(task));
                        break;
                    default:
                        System.out.println("Не определена задача в файле");
                }
            });
            return manager;
        } catch (IOException e) {
            throw new ManagerLoadFromFileException("Возникла ошибка при загрузке данных из файла", file);
        }
    }


    private void save() throws ManagerSaveException {
        try (FileWriter writer = new FileWriter(managerFile)) {
            String header = "id,type,name,status,description,epic,duration,start_time,end_time\n";
            writer.write(header);
            for (Task task : getTasks()) {
                writer.write(String.format("%s\n", task.toString()));
            }

            for (Epic epic : getEpics()) {
                writer.write(String.format("%s\n", epic.toString()));
            }

            for (Subtask subtask : getSubtasks()) {
                writer.write(String.format("%s\n", subtask.toString()));
            }
        } catch (IOException e) {
            throw new ManagerSaveException("Возникла ошибка при автосохранении менеджера", managerFile);
        }
    }

    public static void main(String[] args) {
        FileBackedTaskManager manager = FileBackedTaskManager.loadFromFile(new File("manager.csv"));
        System.out.println("Задачи:");
        for (Task task : manager.getTasks()) {
            System.out.println(task);
        }
        System.out.println("Эпики:");
        for (Epic epic : manager.getEpics()) {
            System.out.println(epic);
        }
        System.out.println("Подзадачи:");
        for (Subtask subtask : manager.getSubtasks()) {
            System.out.println(subtask);
        }
    }
}
