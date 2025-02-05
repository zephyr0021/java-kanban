import managers.FileBackedTaskManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import tasks.Epic;
import tasks.Subtask;
import tasks.Task;

import java.io.*;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;

public class FileBackendTaskManagerTest {
    String header = "id,type,name,status,description,epic,duration,start_time,end_time";
    FileBackedTaskManager manager;
    File testTmpFile;
    Task task1;
    Task task2;
    Epic epic1;
    Epic epic2;
    Epic epic3;
    Subtask subtask1;
    Subtask subtask2;
    Subtask subtask3;
    Duration duration1 = Duration.ofMinutes(100);
    LocalDateTime startTime1 = LocalDateTime.of(LocalDate.of(2025,2,4),
            LocalTime.of(10,0));

    @BeforeEach
    public void setUp() throws IOException {

        testTmpFile = File.createTempFile("manager", ".csv");
        manager = new FileBackedTaskManager(testTmpFile);
        task1 = new Task("TestName1", "TestDescription1", duration1, startTime1);
        task2 = new Task("TestName4", "TestDescription4", duration1, startTime1);
        epic1 = new Epic("TestName2", "TestDescription2", duration1, startTime1);
        epic2 = new Epic("TestName5", "TestDescription5", duration1, startTime1);
        epic3 = new Epic("TestName6", "TestDescription6", duration1, startTime1);
        subtask2 = new Subtask("TestName8", "TestDescription8", 4, duration1, startTime1);
        subtask3 = new Subtask("TestName9", "TestDescription9", 5, duration1, startTime1);
    }

    @AfterEach
    public void tearDown() {
        manager.getManagerFile().deleteOnExit();
    }

    @Test
    public void saveEmptyFileFromManager() {
        System.out.println(manager.getManagerFile().getAbsolutePath());
        Assertions.assertEquals(0, manager.getManagerFile().length());
    }

    @Test
    public void loadManagerFromEmptyFile() {
        FileBackedTaskManager manager = FileBackedTaskManager.loadFromFile(testTmpFile);
        Assertions.assertEquals(0, manager.getTasks().size());
        Assertions.assertEquals(0, manager.getSubtasks().size());
        Assertions.assertEquals(0, manager.getEpics().size());
        Assertions.assertEquals(0, manager.getHistory().size());
    }

    @Test
    public void saveTasksFileFromManager() {
        manager.addTask(task1);
        manager.addEpic(epic1);
        manager.addSubtask(new Subtask("TestName7", "TestDescription7", 2, duration1, startTime1));
        try (BufferedReader buffer = new BufferedReader(new FileReader(testTmpFile))) {
            ArrayList<String> tasks = new ArrayList<>(buffer.lines().toList());
            tasks.remove(header);
            String[] arrTasks = tasks.toArray(new String[0]);
            Task taskFromFile = Task.fromString(arrTasks[0]);
            Epic epicFromFile = Epic.fromString(arrTasks[1]);
            Subtask subtaskFromFile = Subtask.fromString(arrTasks[2]);
            Assertions.assertEquals(manager.getTask(1), taskFromFile);
            Assertions.assertEquals(manager.getEpic(2), epicFromFile);
            Assertions.assertEquals(manager.getSubtask(3), subtaskFromFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void loadTasksFromFileToManager() {
        task1 = new Task("TestName1", "TestDescription1", 1, duration1, startTime1);
        task2 = new Task("TestName4", "TestDescription4", 2, duration1, startTime1);
        epic1 = new Epic("TestName2", "TestDescription2", 3, duration1, startTime1);
        subtask1 = new Subtask("TestName8", "TestDescription8", 3, 4, duration1, startTime1);
        try (FileWriter writer = new FileWriter(testTmpFile)) {
            writer.write(String.format("%s\n", header));
            writer.write(String.format("%s\n", task1.toString()));
            writer.write(String.format("%s\n", task2.toString()));
            writer.write(String.format("%s\n", epic1.toString()));
            writer.write(String.format("%s\n", subtask1.toString()));
        } catch (IOException e) {
            e.printStackTrace();
        }

        FileBackedTaskManager manager = FileBackedTaskManager.loadFromFile(testTmpFile);
        Assertions.assertEquals(manager.getTask(1), task1);
        Assertions.assertEquals(manager.getTask(2), task2);
        Assertions.assertEquals(manager.getEpic(3), epic1);
        Assertions.assertEquals(manager.getSubtask(4), subtask1);
    }

    @Test
    public void loadTasksFromFileToManagerAddOtherTasks() {
        task1 = new Task("TestName1", "TestDescription1", 1, duration1, startTime1);
        task2 = new Task("TestName4", "TestDescription4", 2, duration1, startTime1);
        epic1 = new Epic("TestName2", "TestDescription2", 3, duration1, startTime1);
        subtask1 = new Subtask("TestName8", "TestDescription8", 3, 4, duration1, startTime1);
        try (FileWriter writer = new FileWriter(testTmpFile)) {
            writer.write(String.format("%s\n", header));
            writer.write(String.format("%s\n", task1.toString()));
            writer.write(String.format("%s\n", task2.toString()));
            writer.write(String.format("%s\n", epic1.toString()));
            writer.write(String.format("%s\n", subtask1.toString()));
        } catch (IOException e) {
            e.printStackTrace();
        }

        FileBackedTaskManager manager = FileBackedTaskManager.loadFromFile(testTmpFile);
        Task task3 = new Task("TestName1", "TestDescription1", duration1, startTime1);
        Task task4 = new Task("TestName4", "TestDescription4", duration1, startTime1);
        Epic epic4 = new Epic("TestName4", "TestDescription4", duration1, startTime1);
        Subtask subtask4 = new Subtask("TestName8", "TestDescription8", 7, duration1, startTime1);
        manager.addTask(task3);
        manager.addTask(task4);
        manager.addEpic(epic4);
        manager.addSubtask(subtask4);
        Assertions.assertEquals(manager.getTask(5), task3);
        Assertions.assertEquals(manager.getTask(6), task4);
        Assertions.assertEquals(manager.getEpic(7), epic4);
        Assertions.assertEquals(manager.getSubtask(8), subtask4);
    }


}
