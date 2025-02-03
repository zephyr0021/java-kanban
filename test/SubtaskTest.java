import statuses.StatusTask;
import tasks.Epic;
import tasks.Subtask;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class SubtaskTest {
    Subtask subtask;
    Epic epic;

    @BeforeEach
    void setUp() {
        epic = new Epic("TestEpic", "TestDescription", 1);
        subtask = new Subtask("TestSubtask1", "TestDescription1", StatusTask.NEW, 1, 2);
    }

    @Test
    void subtaskToString() {
        Assertions.assertEquals("2,SUBTASK,TestSubtask1,NEW,TestDescription1,1", subtask.toString());
    }

    @Test
    void subtaskFromString() {
        Subtask subtask1 = Subtask.fromString("2,SUBTASK,TestSubtask1,NEW,TestDescription1,1");
        Subtask subtask2 = Subtask.fromString("5,SUBTASK,TestSubtask5,NEW,TestDescription,1");
        Assertions.assertEquals(subtask, subtask1);
        Assertions.assertNotEquals(subtask, subtask2);
    }


}
