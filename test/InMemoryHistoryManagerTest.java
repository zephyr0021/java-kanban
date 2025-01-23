import managers.InMemoryHistoryManager;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import tasks.*;


public class InMemoryHistoryManagerTest {
    InMemoryHistoryManager manager;
    static Task task1;
    static Task task2;
    static Task task3;
    static Epic epic4;
    static Epic epic5;
    static Epic epic6;
    static Subtask subtask7;
    static Subtask subtask8;
    static Subtask subtask9;
    static Subtask subtask10;
    static Subtask subtask11;
    static Subtask subtask12;

    @BeforeAll
    public static void setup() {
        task1 = new Task("Task1", "Description1", 1);
        task2 = new Task("Task2", "Description2", 2);
        task3 = new Task("Task3", "Description3", 3);
        epic4 = new Epic("Epic4", "Description4", 4);
        epic5 = new Epic("Epic5", "Description5", 5);
        epic6 = new Epic("Epic6", "Description6", 6);
        subtask7 = new Subtask("Subtask7", "Description7", 4, 7);
        subtask8 = new Subtask("Subtask8", "Description8", 5, 8);
        subtask9 = new Subtask("Subtask9", "Description9", 6, 9);
        subtask10 = new Subtask("Subtask10", "Description10", 4, 10);
        subtask11 = new Subtask("Subtask11", "Description11", 5, 11);
        subtask12 = new Subtask("Subtask12", "Description12", 6, 12);
    }

    @BeforeEach
    public void setupTest() {
        manager = new InMemoryHistoryManager();
    }

    @Test
    public void addDifferentTasksInHistory() {
        manager.add(task1);
        manager.add(task2);
        manager.add(epic4);
        manager.add(epic5);
        manager.add(subtask7);
        manager.add(subtask8);
        Assertions.assertEquals(6, manager.getHistory().size());
    }

    @Test
    public void addAndNotRepeatsTaskInHistory() {
        manager.add(task1);
        manager.add(task1);
        manager.add(task1);
        manager.add(task2);
        manager.add(task3);
        manager.add(task3);
        manager.add(task3);
        manager.add(epic4);
        manager.add(epic5);
        manager.add(epic6);
        manager.add(subtask7);
        manager.add(subtask8);
        manager.add(subtask9);
        manager.add(subtask10);
        Assertions.assertEquals(10, manager.getHistory().size());

        manager.add(subtask11);

        Assertions.assertEquals(11, manager.getHistory().size());
        Assertions.assertEquals(task1, manager.getHistory().getFirst());
        Assertions.assertEquals(subtask11, manager.getHistory().getLast());

        manager.add(subtask12);

        Assertions.assertEquals(12, manager.getHistory().size());
        Assertions.assertEquals(task1, manager.getHistory().getFirst());
        Assertions.assertEquals(subtask12, manager.getHistory().getLast());
    }

    @Test
    public void removeTaskFromHistory() {
        manager.add(task1);
        manager.add(task2);
        manager.add(task3);
        manager.remove(1);
        Assertions.assertEquals(2, manager.getHistory().size());
        manager.remove(1);
        Assertions.assertEquals(2, manager.getHistory().size());
        manager.remove(2);
        Assertions.assertEquals(1, manager.getHistory().size());
    }

    @Test
    public void negativeAddNullInHistory() {
        manager.add(null);
        Assertions.assertTrue(manager.getHistory().isEmpty());
    }


}
