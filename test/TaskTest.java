import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import statuses.StatusTask;
import tasks.*;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

public class TaskTest {
    Task task1;
    Task task2;
    Task task3;
    Subtask subtask1;
    Subtask subtask2;
    Subtask subtask3;
    Epic epic1;
    Epic epic2;
    Epic epic3;
    Duration duration1 = Duration.ofMinutes(100);
    LocalDateTime startTime1 = LocalDateTime.of(LocalDate.of(2025,2,4),
            LocalTime.of(10,0));


    @BeforeEach
    void setUp() {
        task1 = new Task("TestName1", "TestDescription1", StatusTask.NEW, 1, duration1, startTime1);
        task2 = new Task("TestName2", "TestDescription2", StatusTask.NEW, 2);
        task3 = new Task("TestName3", "TestDescription3", StatusTask.NEW, 1);
        subtask1 = new Subtask("TestName1", "TestDescription1", StatusTask.NEW, 0, 3);
        subtask2 = new Subtask("TestName2", "TestDescription2", StatusTask.NEW, 0, 4);
        subtask3 = new Subtask("TestName3", "TestDescription3", StatusTask.NEW, 0, 3);
        epic1 = new Epic("TestName1", "TestDescription1", 5);
        epic2 = new Epic("TestName2", "TestDescription2", 6);
        epic3 = new Epic("TestName3", "TestDescription3", 5);
    }

    @Test
    void shouldEqualsIsSameIds() {
        Assertions.assertEquals(task1, task3);
        Assertions.assertEquals(subtask1, subtask3);
        Assertions.assertEquals(epic1, epic3);
    }

    @Test
    void shouldNotEqualsIsDifferentIds() {
        Assertions.assertNotEquals(task1, task2);
        Assertions.assertNotEquals(subtask1, subtask2);
        Assertions.assertNotEquals(epic1, epic2);
    }

    @Test
    void taskToString() {
        Assertions.assertEquals("1,TASK,TestName1,NEW,TestDescription1,,PT1H40M,2025-02-04T10:00,2025-02-04T11:40", task1.toString());
    }

    @Test
    void taskFromString() {
        Task task = Task.fromString("1,TASK,TestName1,NEW,TestDescription1,,PT1H40M,2025-02-04T10:00");
        Assertions.assertEquals(task1, task);
        Assertions.assertNotEquals(task2, task);
    }

}
