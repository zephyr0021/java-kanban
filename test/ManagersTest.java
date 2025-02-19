import managers.TaskManager;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import tasks.Epic;
import tasks.Task;
import util.Managers;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

public class ManagersTest {

    @Test
    public void getDefault() {
        Duration duration1 = Duration.ofMinutes(100);
        LocalDateTime startTime1 = LocalDateTime.of(LocalDate.of(2025, 2, 4),
                LocalTime.of(10, 0));
        LocalDateTime startTime2 = LocalDateTime.of(LocalDate.of(2025, 2, 5),
                LocalTime.of(10, 0));
        TaskManager manager = Managers.getDefault();
        Assertions.assertNotNull(manager);
        manager.addTask(new Task("Task1", "Descr1", duration1, startTime1));
        manager.addTask(new Task("Task2", "Descr2", duration1, startTime2));
        manager.addEpic(new Epic("Epic1", "Descr1"));
        manager.addEpic(new Epic("Epic2", "Descr2"));
        Assertions.assertEquals("Task1", manager.getTasks().getFirst().getName());
        Assertions.assertEquals(2, manager.getTasks().size());
        manager.deleteTask(1);
        Assertions.assertEquals(1, manager.getTasks().size());
        manager.getTask(2);
        manager.getEpic(3);
        manager.getEpic(4);
        Assertions.assertEquals(3, manager.getHistory().size());
    }
}
