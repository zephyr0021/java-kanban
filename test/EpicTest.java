import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import statuses.StatusTask;
import tasks.Epic;
import tasks.Subtask;

import static org.junit.jupiter.api.Assertions.*;

public class EpicTest {

    Epic epic;
    Subtask subtask1;
    Subtask subtask2;
    Subtask subtask3;
    @BeforeEach
    void setUp() {
        epic = new Epic("TestEpic","TestDescription", 1);
        subtask1 = new Subtask("TestSubtask1","TestDescription1", StatusTask.NEW, 0, 2);
        subtask2 = new Subtask("TestSubtask2","TestDescription2", StatusTask.DONE, 0, 3);
        subtask3 = new Subtask("TestSubtask3","TestDescription3", StatusTask.IN_PROGRESS, 0, 4);
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
}
