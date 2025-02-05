import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import statuses.StatusTask;
import tasks.Epic;
import tasks.Subtask;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

public class EpicTest {

    Epic epic;
    Subtask subtask1;
    Subtask subtask2;
    Subtask subtask3;
    Duration duration1 = Duration.ofMinutes(100);
    LocalDateTime startTime1 = LocalDateTime.of(LocalDate.of(2025,2,4),
            LocalTime.of(10,0));

    @BeforeEach
    void setUp() {
        epic = new Epic("TestEpic", "TestDescription", 1, duration1, startTime1);
        subtask1 = new Subtask("TestSubtask1", "TestDescription1", StatusTask.NEW, 0, 2, duration1, startTime1);
        subtask2 = new Subtask("TestSubtask2", "TestDescription2", StatusTask.DONE, 0, 3, duration1, startTime1);
        subtask3 = new Subtask("TestSubtask3", "TestDescription3", StatusTask.IN_PROGRESS, 0, 4, duration1, startTime1);
    }

    @Test
    void addSubtask() {
        epic.addSubtask(subtask1);
        epic.addSubtask(subtask2);
        Assertions.assertEquals(1, subtask1.getEpicId());
        Assertions.assertEquals(1, subtask2.getEpicId());
        Assertions.assertEquals(2, epic.getSubtasks().size());
    }

    @Test
    void removeSubtask() {
        epic.addSubtask(subtask1);
        epic.removeSubtask(2);
        Assertions.assertEquals(0, epic.getSubtasks().size());
    }

    @Test
    void negativeAddSameSubtask() {
        epic.addSubtask(subtask1);
        epic.addSubtask(subtask1);
        Assertions.assertEquals(1, epic.getSubtasks().size());
    }

    @Test
    void negativeRemoveOtherSubtask() {
        epic.addSubtask(subtask1);
        epic.removeSubtask(3);
        Assertions.assertEquals(1, epic.getSubtasks().size());
    }

    @Test
    void clearSubtaskList() {
        epic.addSubtask(subtask1);
        epic.addSubtask(subtask2);
        epic.addSubtask(subtask3);
        epic.clearSubtaskList();
        Assertions.assertEquals(0, epic.getSubtasks().size());

    }

    @Test
    void epicToString() {
        Assertions.assertEquals("1,EPIC,TestEpic,NEW,TestDescription,", epic.toString());
    }

    @Test
    void epicFromString() {
        Epic epic1 = Epic.fromString("1,EPIC,TestEpic,NEW,TestDescription,");
        Epic epic2 = Epic.fromString("5,EPIC,TestEpic2,NEW,TestDescription,");
        Assertions.assertEquals(epic, epic1);
        Assertions.assertNotEquals(epic, epic2);
    }
}
