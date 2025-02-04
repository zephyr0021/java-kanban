import statuses.StatusTask;
import tasks.Epic;
import tasks.Subtask;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

public class SubtaskTest {
    Subtask subtask;
    Epic epic;
    Duration duration1 = Duration.ofMinutes(100);
    LocalDateTime startTime1 = LocalDateTime.of(LocalDate.of(2025,2,4),
            LocalTime.of(10,0));

    @BeforeEach
    void setUp() {
        epic = new Epic("TestEpic", "TestDescription", 1);
        subtask = new Subtask("TestSubtask1", "TestDescription1", StatusTask.NEW, 1, 2, duration1, startTime1);
    }

    @Test
    void subtaskToString() {
        Assertions.assertEquals("2,SUBTASK,TestSubtask1,NEW,TestDescription1,1,PT1H40M,2025-02-04T10:00,2025-02-04T11:40", subtask.toString());
    }

    @Test
    void subtaskFromString() {
        Subtask subtask1 = Subtask.fromString("2,SUBTASK,TestSubtask1,NEW,TestDescription1,1,PT1H40M,2025-02-04T10:00");
        Subtask subtask2 = Subtask.fromString("5,SUBTASK,TestSubtask5,NEW,TestDescription,1,PT1H40M,2025-02-04T10:00");
        Assertions.assertEquals(subtask, subtask1);
        Assertions.assertNotEquals(subtask, subtask2);
    }


}
