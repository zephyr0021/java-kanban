import managers.InMemoryTaskManager;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import statuses.StatusTask;
import tasks.*;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class InMemoryTaskManagerTest {
    InMemoryTaskManager manager;
    Task task1;
    Task task2;
    Task task3;
    Task task4;
    Epic epic1;
    Epic epic2;
    Epic epic3;
    Subtask subtask1;
    Subtask subtask2;
    Subtask subtask3;
    Subtask subtask4;
    Subtask subtask5;
    Subtask subtask6;
    Duration duration1 = Duration.ofMinutes(100);
    LocalDateTime startTime1 = LocalDateTime.of(LocalDate.of(2025, 2, 4),
            LocalTime.of(10, 0));
    LocalDateTime startTime2 = LocalDateTime.of(LocalDate.of(2025, 2, 5),
            LocalTime.of(10, 0));
    LocalDateTime startTime3 = LocalDateTime.of(LocalDate.of(2025, 2, 6),
            LocalTime.of(10, 0));
    LocalDateTime startTime4 = LocalDateTime.of(LocalDate.of(2025, 2, 7),
            LocalTime.of(10, 0));
    LocalDateTime startTime5 = LocalDateTime.of(LocalDate.of(2025, 2, 8),
            LocalTime.of(10, 0));
    LocalDateTime startTime6 = LocalDateTime.of(LocalDate.of(2025, 2, 9),
            LocalTime.of(10, 0));
    LocalDateTime startTime7 = LocalDateTime.of(LocalDate.of(2025, 2, 10),
            LocalTime.of(10, 0));
    LocalDateTime startTime8 = LocalDateTime.of(LocalDate.of(2025, 2, 11),
            LocalTime.of(10, 0));
    LocalDateTime startTime9 = LocalDateTime.of(LocalDate.of(2025, 2, 12),
            LocalTime.of(10, 0));


    @BeforeEach
    public void setUp() {
        manager = new InMemoryTaskManager();
        task1 = new Task("TestName1", "TestDescription1", duration1, startTime1);
        task2 = new Task("TestName4", "TestDescription4", duration1, startTime2);
        epic1 = new Epic("TestName2", "TestDescription2");
        epic2 = new Epic("TestName5", "TestDescription5");
        epic3 = new Epic("TestName6", "TestDescription6");
    }

    @Test
    public void createIdForTask() {
        manager.addTask(task1);
        manager.addEpic(epic1);
        subtask1 = new Subtask("TestName3", "TestDescription3", epic1.getId(), duration1, startTime3);
        manager.addSubtask(subtask1);
        Assertions.assertEquals(1, task1.getId());
        Assertions.assertEquals(2, epic1.getId());
        Assertions.assertEquals(3, subtask1.getId());
    }

    @Test
    public void addTasks() {
        manager.addTask(task1);
        manager.addTask(task2);
        manager.addEpic(epic1);
        manager.addEpic(epic2);
        subtask1 = new Subtask("TestName3", "TestDescription3", epic1.getId(), duration1, startTime3);
        manager.addSubtask(subtask1);
        Assertions.assertEquals(2, manager.getTasks().size());
        Assertions.assertEquals(2, manager.getEpics().size());
        Assertions.assertEquals(1, manager.getSubtasks().size());
        Assertions.assertEquals(1, manager.getEpic(3).getSubtasks().size());
    }

    @Test
    public void addTaskWithTimeIntersection() {
        manager.addTask(task1);
        task2 = new Task("TestName2", "TestDescription2", duration1, LocalDateTime.of(LocalDate.of(2025, 2, 4),
                LocalTime.of(10, 5)));
        manager.addTask(task2);
        Assertions.assertEquals(1, manager.getTasks().size());
        task3 = new Task("TestName3", "TestDescription3", duration1, startTime1);
        Assertions.assertEquals(1, manager.getTasks().size());
        task4 = new Task("TestName4", "TestDescription4", duration1, startTime2);
        manager.addTask(task4);
        Assertions.assertEquals(2, manager.getTasks().size());
    }

    @Test
    public void addSubtaskWithTimeIntersection() {
        manager.addEpic(epic1);
        subtask1 = new Subtask("TestName3", "TestDescription3", epic1.getId(), duration1, startTime1);
        manager.addSubtask(subtask1);
        subtask2 = new Subtask("TestName4", "TestDescription4", epic1.getId(), duration1, LocalDateTime.of(LocalDate.of(2025, 2, 4),
                LocalTime.of(10, 5)));
        manager.addSubtask(subtask2);
        Assertions.assertEquals(1, manager.getSubtasks().size());
        subtask3 = new Subtask("TestName3", "TestDescription3", epic1.getId(), duration1, startTime1);
        Assertions.assertEquals(1, manager.getSubtasks().size());
        subtask4 = new Subtask("TestName4", "TestDescription4", epic1.getId(), duration1, startTime2);
        manager.addSubtask(subtask4);
        Assertions.assertEquals(2, manager.getSubtasks().size());
    }

    @Test
    public void addSubtaskInEpic() {
        manager.addEpic(epic1);
        subtask1 = new Subtask("TestName3", "TestDescription3", epic1.getId(), duration1, startTime1);
        subtask2 = new Subtask("TestName7", "TestDescription7", epic1.getId(), duration1, startTime2);
        manager.addSubtask(subtask1);
        manager.addSubtask(subtask2);
        Assertions.assertEquals(1, subtask1.getEpicId());
        Assertions.assertEquals(1, subtask2.getEpicId());
        Assertions.assertTrue(epic1.getSubtasks().contains(subtask1.getId()));
        Assertions.assertTrue(epic1.getSubtasks().contains(subtask2.getId()));

    }

    @Test
    public void getTasksById() {
        manager.addTask(task1);
        manager.addEpic(epic1);
        subtask1 = new Subtask("TestName3", "TestDescription3", epic1.getId(), duration1, startTime2);
        manager.addSubtask(subtask1);
        Assertions.assertEquals(task1, manager.getTask(1));
        Assertions.assertEquals(epic1, manager.getEpic(2));
        Assertions.assertEquals(subtask1, manager.getSubtask(3));
    }

    @Test
    public void getPrioritizedTasksAndSubtasks() {
        manager.addEpic(epic1);
        task1 = new Task("TestName1", "TestDescription1", duration1, startTime1);
        task2 = new Task("TestName2", "TestDescription2", duration1, startTime2);
        task3 = new Task("TestName3", "TestDescription3", duration1, startTime3);
        subtask1 = new Subtask("TestName3", "TestDescription3", epic1.getId(), duration1, startTime4);
        subtask2 = new Subtask("TestName4", "TestDescription4", epic1.getId(), duration1, startTime5);
        subtask3 = new Subtask("TestName5", "TestDescription5", epic1.getId(), duration1, startTime6);
        manager.addTask(task2);
        manager.addSubtask(subtask2);
        manager.addSubtask(subtask1);
        manager.addTask(task1);
        manager.addTask(task3);
        manager.addSubtask(subtask3);
        ArrayList<Task> prioritizedTasks = new ArrayList<>(manager.getPrioritizedTasks());
        ArrayList<Task> expectedPrioritizedTasks = new ArrayList<>();
        expectedPrioritizedTasks.add(task1);
        expectedPrioritizedTasks.add(task2);
        expectedPrioritizedTasks.add(task3);
        expectedPrioritizedTasks.add(subtask1);
        expectedPrioritizedTasks.add(subtask2);
        expectedPrioritizedTasks.add(subtask3);
        Assertions.assertEquals(expectedPrioritizedTasks, prioritizedTasks);
    }

    @Test
    public void deleteTaskById() {
        manager.addTask(task1);
        manager.addEpic(epic1);
        manager.deleteTask(1);
        manager.deleteEpic(2);
        Assertions.assertEquals(0, manager.getTasks().size());
        Assertions.assertEquals(0, manager.getEpics().size());
        Assertions.assertNull(manager.getTask(1));
        Assertions.assertNull(manager.getEpic(2));
    }

    @Test
    public void deleteSubtaskById() {
        manager.addEpic(epic1);
        subtask1 = new Subtask("TestName3", "TestDescription3", epic1.getId(), duration1, startTime1);
        subtask2 = new Subtask("TestName7", "TestDescription7", epic1.getId(), duration1, startTime2);
        manager.addSubtask(subtask1);
        manager.addSubtask(subtask2);
        manager.deleteSubtask(2);
        Assertions.assertFalse(epic1.getSubtasks().contains(2));
        Assertions.assertEquals(1, manager.getSubtasks().size());
        Assertions.assertEquals(1, manager.getEpic(1).getSubtasks().size());
        manager.deleteSubtask(3);
        Assertions.assertFalse(epic1.getSubtasks().contains(3));
        Assertions.assertEquals(0, manager.getSubtasks().size());
        Assertions.assertEquals(0, manager.getEpic(1).getSubtasks().size());

    }

    @Test
    public void deleteEpicByIdAndDeleteEpicSubtasks() {
        manager.addEpic(epic1);
        subtask1 = new Subtask("TestName3", "TestDescription3", epic1.getId(), duration1, startTime1);
        subtask2 = new Subtask("TestName7", "TestDescription7", epic1.getId(), duration1, startTime1);
        manager.addSubtask(subtask1);
        manager.addSubtask(subtask2);
        manager.deleteEpic(1);
        Assertions.assertEquals(0, manager.getEpics().size());
        Assertions.assertEquals(0, manager.getSubtasks().size());
    }

    @Test
    public void deleteTasksAndSubtasksInPrioritized() {
        manager.addEpic(epic1);
        task1 = new Task("TestName1", "TestDescription1", duration1, startTime1);
        task2 = new Task("TestName2", "TestDescription2", duration1, startTime2);
        task3 = new Task("TestName3", "TestDescription3", duration1, startTime3);
        subtask1 = new Subtask("TestName3", "TestDescription3", epic1.getId(), duration1, startTime4);
        subtask2 = new Subtask("TestName4", "TestDescription4", epic1.getId(), duration1, startTime5);
        subtask3 = new Subtask("TestName5", "TestDescription5", epic1.getId(), duration1, startTime6);
        manager.addTask(task2);
        manager.addSubtask(subtask2);
        manager.addSubtask(subtask1);
        manager.addTask(task1);
        manager.addTask(task3);
        manager.addSubtask(subtask3);
        manager.deleteTask(5);
        manager.deleteSubtask(4);
        ArrayList<Task> expectedPrioritizedTasks = new ArrayList<>();
        expectedPrioritizedTasks.add(task2);
        expectedPrioritizedTasks.add(task3);
        expectedPrioritizedTasks.add(subtask2);
        expectedPrioritizedTasks.add(subtask3);
        ArrayList<Task> prioritizedTasks = new ArrayList<>(manager.getPrioritizedTasks());
        Assertions.assertEquals(expectedPrioritizedTasks, prioritizedTasks);

    }

    @Test
    public void deleteSubtasksInPrioritizedWhenEpicDelete() {
        manager.addEpic(epic1);
        subtask1 = new Subtask("TestName3", "TestDescription3", epic1.getId(), duration1, startTime4);
        subtask2 = new Subtask("TestName4", "TestDescription4", epic1.getId(), duration1, startTime5);
        subtask3 = new Subtask("TestName5", "TestDescription5", epic1.getId(), duration1, startTime6);
        manager.addSubtask(subtask2);
        manager.addSubtask(subtask1);
        manager.addSubtask(subtask3);
        manager.deleteEpic(1);
        Assertions.assertTrue(manager.getPrioritizedTasks().isEmpty());
    }

    @Test
    public void clearTasks() {
        manager.addTask(task1);
        manager.addTask(task2);
        manager.addEpic(epic1);
        manager.addEpic(epic2);
        subtask1 = new Subtask("TestName3", "TestDescription3", epic1.getId(), duration1, startTime1);
        subtask2 = new Subtask("TestName7", "TestDescription7", epic2.getId(), duration1, startTime1);
        manager.addSubtask(subtask1);
        manager.addSubtask(subtask2);
        manager.clearTasks();
        Assertions.assertEquals(0, manager.getTasks().size());
        manager.clearSubtasks();
        Assertions.assertEquals(0, manager.getSubtasks().size());
        Assertions.assertEquals(0, epic1.getSubtasks().size());
        Assertions.assertEquals(0, epic2.getSubtasks().size());
        manager.clearEpics();
        Assertions.assertEquals(0, manager.getEpics().size());
    }

    @Test
    public void clearEpicsWithSubtasks() {
        manager.addTask(task1);
        manager.addTask(task2);
        manager.addEpic(epic1);
        manager.addEpic(epic2);
        subtask1 = new Subtask("TestName3", "TestDescription3", epic1.getId(), duration1, startTime1);
        subtask2 = new Subtask("TestName7", "TestDescription7", epic2.getId(), duration1, startTime1);
        subtask3 = new Subtask("TestName8", "TestDescription8", epic2.getId(), duration1, startTime1);
        manager.addSubtask(subtask1);
        manager.addSubtask(subtask2);
        manager.addSubtask(subtask3);
        manager.clearEpics();
        Assertions.assertEquals(0, manager.getEpics().size());
        Assertions.assertEquals(0, manager.getSubtasks().size());
    }

    @Test
    public void clearSubtasksWithEpicUpdate() {
        manager.addEpic(epic1);
        manager.addEpic(epic2);
        subtask1 = new Subtask("TestName3", "TestDescription3", StatusTask.NEW, epic1.getId(), duration1, startTime1);
        subtask2 = new Subtask("TestName7", "TestDescription7", StatusTask.IN_PROGRESS, epic1.getId(), duration1, startTime2);
        subtask3 = new Subtask("TestName8", "TestDescription8", StatusTask.DONE, epic2.getId(), duration1, startTime3);
        manager.addSubtask(subtask1);
        manager.addSubtask(subtask2);
        manager.addSubtask(subtask3);
        Assertions.assertEquals(StatusTask.IN_PROGRESS, epic1.getStatus());
        Assertions.assertEquals(StatusTask.DONE, epic2.getStatus());
        manager.clearSubtasks();
        Assertions.assertEquals(0, epic1.getSubtasks().size());
        Assertions.assertEquals(0, epic2.getSubtasks().size());
        Assertions.assertEquals(StatusTask.NEW, epic1.getStatus());
        Assertions.assertEquals(StatusTask.NEW, epic2.getStatus());
    }

    @Test
    public void clearTasksAndSubtasksInPrioritized() {
        manager.addEpic(epic1);
        task1 = new Task("TestName1", "TestDescription1", duration1, startTime1);
        task2 = new Task("TestName2", "TestDescription2", duration1, startTime2);
        task3 = new Task("TestName3", "TestDescription3", duration1, startTime3);
        subtask1 = new Subtask("TestName3", "TestDescription3", epic1.getId(), duration1, startTime4);
        subtask2 = new Subtask("TestName4", "TestDescription4", epic1.getId(), duration1, startTime5);
        subtask3 = new Subtask("TestName5", "TestDescription5", epic1.getId(), duration1, startTime6);
        manager.addTask(task2);
        manager.addSubtask(subtask2);
        manager.addSubtask(subtask1);
        manager.addTask(task1);
        manager.addTask(task3);
        manager.addSubtask(subtask3);
        manager.clearTasks();
        manager.clearSubtasks();
        Assertions.assertTrue(manager.getPrioritizedTasks().isEmpty());
    }

    @Test
    public void clearSubtasksInPrioritizedWhenClearEpics() {
        manager.addEpic(epic1);
        manager.addEpic(epic2);
        subtask1 = new Subtask("TestName3", "TestDescription3", epic1.getId(), duration1, startTime1);
        subtask2 = new Subtask("TestName4", "TestDescription4", epic2.getId(), duration1, startTime2);
        subtask3 = new Subtask("TestName5", "TestDescription5", epic2.getId(), duration1, startTime3);
        subtask4 = new Subtask("TestName3", "TestDescription3", epic1.getId(), duration1, startTime4);
        subtask5 = new Subtask("TestName4", "TestDescription4", epic2.getId(), duration1, startTime5);
        subtask6 = new Subtask("TestName5", "TestDescription5", epic1.getId(), duration1, startTime6);
        manager.addSubtask(subtask5);
        manager.addSubtask(subtask4);
        manager.addSubtask(subtask1);
        manager.addSubtask(subtask3);
        manager.addSubtask(subtask2);
        manager.addSubtask(subtask6);
        manager.clearEpics();
        Assertions.assertTrue(manager.getPrioritizedTasks().isEmpty());
    }

    @Test
    public void updateTasks() {
        manager.addTask(task1);
        manager.addEpic(epic1);
        subtask1 = new Subtask("TestName3", "TestDescription3", epic1.getId(), duration1, startTime2);
        manager.addSubtask(subtask1);

        manager.updateTask(new Task("TestName1Updated", "TestDescription1Updated", StatusTask.IN_PROGRESS, 1, duration1, startTime1));
        Assertions.assertEquals("TestName1Updated", manager.getTask(1).getName());
        Assertions.assertEquals("TestDescription1Updated", manager.getTask(1).getDescription());
        Assertions.assertEquals(StatusTask.IN_PROGRESS, manager.getTask(1).getStatus());

        manager.updateEpic(new Epic("TestName2Updated", "TestDescription2Updated", 2));
        Assertions.assertEquals("TestName2Updated", manager.getEpic(2).getName());
        Assertions.assertEquals("TestDescription2Updated", manager.getEpic(2).getDescription());
        Assertions.assertEquals(StatusTask.NEW, manager.getEpic(2).getStatus());

        manager.updateSubtask(new Subtask("TestName3Updated", "TestDescription3", epic1.getId(), 3, duration1, startTime1));
        Assertions.assertEquals("TestName3Updated", manager.getSubtask(3).getName());
        Assertions.assertEquals("TestDescription3", manager.getSubtask(3).getDescription());
        Assertions.assertEquals(StatusTask.NEW, manager.getSubtask(3).getStatus());
    }

    @Test
    public void updateSubtaskAndEpicStatus() {
        manager.addEpic(epic1);
        subtask1 = new Subtask("TestName3", "TestDescription3", epic1.getId(), duration1, startTime1);
        subtask2 = new Subtask("TestName7", "TestDescription7", epic1.getId(), duration1, startTime1);
        subtask3 = new Subtask("TestName8", "TestDescription8", epic1.getId(), duration1, startTime1);
        manager.addSubtask(subtask1);
        manager.addSubtask(subtask2);
        manager.addSubtask(subtask3);
        manager.updateSubtask(new Subtask("TestName3Updated", "TestDescription3Updated", StatusTask.IN_PROGRESS, epic1.getId(), 2, duration1, startTime1));
        Assertions.assertEquals(StatusTask.IN_PROGRESS, epic1.getStatus());

        manager.updateSubtask(new Subtask("TestName7", "TestDescription7", StatusTask.DONE, epic1.getId(), 3, duration1, startTime1));
        manager.updateSubtask(new Subtask("TestName8", "TestDescription8", StatusTask.DONE, epic1.getId(), 4, duration1, startTime1));
        Assertions.assertEquals(StatusTask.IN_PROGRESS, epic1.getStatus());

        manager.updateSubtask(new Subtask("TestName3Updated", "TestDescription3Updated", StatusTask.DONE, epic1.getId(), 2, duration1, startTime1));
        Assertions.assertEquals(StatusTask.DONE, epic1.getStatus());

        manager.updateSubtask(new Subtask("TestName3Updated", "TestDescription3Updated", StatusTask.NEW, epic1.getId(), 2, duration1, startTime1));
        manager.updateSubtask(new Subtask("TestName7", "TestDescription7", StatusTask.NEW, epic1.getId(), 3, duration1, startTime1));
        manager.updateSubtask(new Subtask("TestName8", "TestDescription8", StatusTask.NEW, epic1.getId(), 4, duration1, startTime1));
        Assertions.assertEquals(StatusTask.NEW, epic1.getStatus());
    }

    @Test
    public void updateTaskAndSubtaskInPrioritizedList() {
        manager.addEpic(epic1);
        task1 = new Task("TestName1", "TestDescription1", duration1, startTime1);
        task2 = new Task("TestName2", "TestDescription2", duration1, startTime2);
        task3 = new Task("TestName3", "TestDescription3", duration1, startTime3);
        subtask1 = new Subtask("TestName3", "TestDescription3", epic1.getId(), duration1, startTime4);
        subtask2 = new Subtask("TestName4", "TestDescription4", epic1.getId(), duration1, startTime5);
        subtask3 = new Subtask("TestName5", "TestDescription5", epic1.getId(), duration1, startTime6);
        manager.addTask(task2);
        manager.addSubtask(subtask2);
        manager.addSubtask(subtask1);
        manager.addTask(task1);
        manager.addTask(task3);
        manager.addSubtask(subtask3);
        manager.updateTask(new Task("TestName1", "TestDescription1", 5, duration1, startTime7));
        manager.updateSubtask(new Subtask("TestName4", "TestDescription4", epic1.getId(), 3, duration1, startTime8));
        ArrayList<Task> prioritizedTasks = new ArrayList<>(manager.getPrioritizedTasks());
        ArrayList<Task> expectedPrioritizedTasks = new ArrayList<>();
        expectedPrioritizedTasks.add(task2);
        expectedPrioritizedTasks.add(task3);
        expectedPrioritizedTasks.add(subtask1);
        expectedPrioritizedTasks.add(subtask3);
        expectedPrioritizedTasks.add(task1);
        expectedPrioritizedTasks.add(subtask2);
        Assertions.assertEquals(expectedPrioritizedTasks, prioritizedTasks);
    }

    @Test
    public void getHistoryTasks() {
        task3 = new Task("TestName9", "TestDescription9", duration1, startTime3);
        task4 = new Task("TestName10", "TestDescription10", duration1, startTime4);
        epic3 = new Epic("TestName3", "TestDescription3");
        manager.addTask(task1);
        manager.addTask(task2);
        manager.addTask(task3);
        manager.addTask(task4);
        manager.addEpic(epic1);
        manager.addEpic(epic2);
        manager.addEpic(epic3);

        subtask2 = new Subtask("TestName2", "TestDescription2", epic1.getId(), duration1, startTime5);
        subtask3 = new Subtask("TestName3", "TestDescription3", epic1.getId(), duration1, startTime6);
        subtask4 = new Subtask("TestName4", "TestDescription4", epic2.getId(), duration1, startTime7);
        subtask5 = new Subtask("TestName5", "TestDescription5", epic2.getId(), duration1, startTime8);
        subtask6 = new Subtask("TestName6", "TestDescription6", epic3.getId(), duration1, startTime9);

        manager.addSubtask(subtask2);
        manager.addSubtask(subtask3);
        manager.addSubtask(subtask4);
        manager.addSubtask(subtask5);
        manager.addSubtask(subtask6);

        Assertions.assertTrue(manager.getHistory().isEmpty());

        manager.getTask(1);
        manager.getEpic(5);
        manager.getSubtask(8);
        List<Task> expectedHistory = new ArrayList<>();
        expectedHistory.add(task1);
        expectedHistory.add(epic1);
        expectedHistory.add(subtask2);
        Assertions.assertEquals(3, manager.getHistory().size());
        Assertions.assertEquals(expectedHistory, manager.getHistory());

        manager.getTask(2);
        manager.getTask(3);
        manager.getEpic(6);
        manager.getSubtask(10);
        manager.getEpic(7);
        manager.getSubtask(9);
        manager.getSubtask(12);
        manager.getSubtask(11);
        Assertions.assertEquals(11, manager.getHistory().size());
        Assertions.assertEquals(task1, manager.getHistory().getFirst());
        Assertions.assertEquals(subtask5, manager.getHistory().getLast());
    }

    @Test
    public void deleteTaskdeleteTaskInHistory() {
        manager.addTask(task1);
        manager.addTask(task2);
        manager.addEpic(epic1);
        manager.addEpic(epic2);
        subtask2 = new Subtask("TestName2", "TestDescription2", epic1.getId(), duration1, startTime3);
        subtask3 = new Subtask("TestName3", "TestDescription3", epic1.getId(), duration1, startTime4);
        subtask4 = new Subtask("TestName4", "TestDescription4", epic2.getId(), duration1, startTime5);
        subtask5 = new Subtask("TestName5", "TestDescription5", epic2.getId(), duration1, startTime6);
        manager.addSubtask(subtask2);
        manager.addSubtask(subtask3);
        manager.addSubtask(subtask4);
        manager.addSubtask(subtask5);
        ArrayList<Task> expectedHistory = new ArrayList<>();
        expectedHistory.add(task1);
        expectedHistory.add(subtask4);
        expectedHistory.add(subtask5);

        manager.getTask(1);
        manager.getTask(2);
        manager.getEpic(3);
        manager.getSubtask(5);
        manager.getSubtask(6);
        manager.getSubtask(7);
        manager.getSubtask(8);

        Assertions.assertEquals(7, manager.getHistory().size());

        manager.deleteEpic(3);
        manager.deleteTask(2);
        Assertions.assertEquals(3, manager.getHistory().size());
        Assertions.assertEquals(expectedHistory, manager.getHistory());
    }

    @Test
    public void clearTasksAndHistory() {
        manager.addTask(task1);
        manager.addTask(task2);
        manager.addEpic(epic1);
        subtask2 = new Subtask("TestName2", "TestDescription2", epic1.getId(), duration1, startTime3);
        subtask3 = new Subtask("TestName3", "TestDescription3", epic1.getId(), duration1, startTime4);
        manager.addSubtask(subtask2);
        manager.addSubtask(subtask3);
        manager.getTask(1);
        manager.getTask(2);
        manager.getEpic(3);
        manager.getSubtask(4);
        manager.getSubtask(5);
        ArrayList<Task> expectedHistory = new ArrayList<>();
        expectedHistory.add(epic1);
        expectedHistory.add(subtask2);
        expectedHistory.add(subtask3);
        manager.clearTasks();
        Assertions.assertEquals(3, manager.getHistory().size());
        Assertions.assertEquals(expectedHistory, manager.getHistory());
        manager.clearEpics();
        expectedHistory.remove(epic1);
        expectedHistory.remove(subtask2);
        expectedHistory.remove(subtask3);
        Assertions.assertTrue(manager.getHistory().isEmpty());
        Assertions.assertEquals(expectedHistory, manager.getHistory());
        manager.addEpic(epic3);
        subtask3 = new Subtask("TestName4", "TestDescription4", epic3.getId(), duration1, startTime3);
        subtask4 = new Subtask("TestName5", "TestDescription5", epic3.getId(), duration1, startTime4);
        manager.addSubtask(subtask3);
        manager.addSubtask(subtask4);
        manager.getEpic(6);
        manager.getSubtask(7);
        manager.getSubtask(8);
        expectedHistory.add(epic3);
        manager.clearSubtasks();
        Assertions.assertEquals(1, manager.getHistory().size());
        Assertions.assertEquals(expectedHistory, manager.getHistory());
    }
}
