import http.HttpTaskServer;
import http.json.JsonTaskBuilder;
import managers.TaskManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import statuses.StatusTask;
import tasks.Epic;
import tasks.Subtask;
import util.Managers;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;

public class HttpSubtaskTest {

    TaskManager manager = Managers.getDefault();
    HttpTaskServer server = new HttpTaskServer(manager);
    JsonTaskBuilder json = server.getJsonTaskBuilder();
    URI subtaskUrl = URI.create("http://localhost:8080/subtasks");
    LocalDateTime startTime1 = LocalDateTime.of(LocalDate.of(2025, 2, 4),
            LocalTime.of(10, 0));
    LocalDateTime startTime2 = LocalDateTime.of(LocalDate.of(2025, 2, 5),
            LocalTime.of(10, 0));

    public HttpSubtaskTest() throws IOException {
    }

    @BeforeEach
    public void setUp() {
        server.start();
    }

    @AfterEach
    public void tearDown() {
        server.stop();
    }

    @Test
    public void getSubtasks() throws IOException, InterruptedException {
        manager.addEpic(new Epic("test_epic", "descr"));
        manager.addSubtask(new Subtask("test_subtask", "test_subtask_descr", 1, Duration.ofMinutes(100), startTime1));
        manager.addSubtask(new Subtask("test_subtask2", "test_subtask_descr2", 1, Duration.ofMinutes(100), startTime2));

        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder().uri(subtaskUrl).GET().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        Assertions.assertEquals(200, response.statusCode());
        String subtasks = json.toJson(manager.getSubtasks());
        Assertions.assertEquals(subtasks,response.body());
    }

    @Test
    public void getSubTask() throws IOException, InterruptedException {
        manager.addEpic(new Epic("test_epic", "descr"));
        manager.addSubtask(new Subtask("testSubtask", "testSubtaskDescr", 1, Duration.ofMinutes(5), startTime1));

        Subtask subtask = manager.getSubtask(2);
        subtaskUrl = URI.create("http://localhost:8080/subtasks/" + subtask.getId());

        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder().uri(subtaskUrl).GET().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        Assertions.assertEquals(200, response.statusCode());

        Assertions.assertEquals(json.toJson(subtask), response.body());


    }

    @Test
    public void testAddSubtask() throws IOException, InterruptedException {
        String taskJson = """
                {
                        "name": "Выгулять собаку",
                        "description": "Погулять с Джеком 20 минут",
                        "epicId": "1",
                        "duration": "100",
                        "startTime": "2025-02-21 15:45:01"
                    }""";
        manager.addEpic(new Epic("test_epic", "descr"));

        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder().uri(subtaskUrl).POST(HttpRequest.BodyPublishers.ofString(taskJson)).build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        Assertions.assertEquals(201, response.statusCode());

        ArrayList<Subtask> subtasks = manager.getSubtasks();

        Assertions.assertNotNull(subtasks);
        Assertions.assertEquals(1, subtasks.size());
        Assertions.assertEquals("Выгулять собаку", subtasks.getFirst().getName());
    }

    @Test
    public void updateSubtask() throws IOException, InterruptedException {
        manager.addEpic(new Epic("test_epic", "descr"));
        manager.addSubtask(new Subtask("testSubtask", "testSubtaskDescr", 1, Duration.ofMinutes(5), startTime1));
        String taskJson = """
                {
                        "name": "Выгулять собаку",
                        "description": "Погулять с Джеком 20 минут",
                        "status": "DONE",
                        "epicId": "1",
                        "id": 2,
                        "type": "TASK",
                        "duration": "100",
                        "startTime": "2025-02-21 15:45:01"
                    }""";

        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder().uri(subtaskUrl).POST(HttpRequest.BodyPublishers.ofString(taskJson)).build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        Subtask subtaskUpd = manager.getSubtask(2);
        Assertions.assertEquals(201, response.statusCode());
        Assertions.assertEquals("Выгулять собаку", subtaskUpd.getName());
        Assertions.assertEquals("Погулять с Джеком 20 минут", subtaskUpd.getDescription());
        Assertions.assertEquals(StatusTask.DONE, subtaskUpd.getStatus());
    }

    @Test
    public void deleteSubtask() throws IOException, InterruptedException {
        manager.addEpic(new Epic("test_epic", "descr"));
        manager.addSubtask(new Subtask("testTask", "testTaskDescr", 1, Duration.ofMinutes(5), startTime1));
        Subtask subtask = manager.getSubtask(2);
        subtaskUrl = URI.create("http://localhost:8080/subtasks/" + subtask.getId());
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder().uri(subtaskUrl).DELETE().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        Assertions.assertEquals(200, response.statusCode());

        ArrayList<Subtask> subtasks = manager.getSubtasks();

        Assertions.assertTrue(subtasks.isEmpty());
    }

    @Test
    public void getSubtaskNotFound() throws IOException, InterruptedException {
        manager.addEpic(new Epic("test_epic", "descr"));
        manager.addSubtask(new Subtask("testTask", "testTaskDescr", 1, Duration.ofMinutes(5), startTime1));
        subtaskUrl = URI.create("http://localhost:8080/tasks/3");

        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder().uri(subtaskUrl).GET().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        Assertions.assertEquals(404, response.statusCode());
        Assertions.assertEquals("Подзадача с указанным id не найдена", response.body());

    }

    @Test
    public void addTaskIntersection() throws IOException, InterruptedException {
        manager.addEpic(new Epic("test_epic", "descr"));
        manager.addSubtask(new Subtask("testTask", "testTaskDescr", 1, Duration.ofMinutes(5), startTime1));
        String taskJson = """
                {
                        "name": "Выгулять собаку",
                        "description": "Погулять с Джеком 20 минут",
                        "epicId": "1",
                        "duration": "5",
                        "startTime": "2025-02-04 10:00:00"
                    }""";

        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder().uri(subtaskUrl).POST(HttpRequest.BodyPublishers.ofString(taskJson)).build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        Assertions.assertEquals(406, response.statusCode());
        Assertions.assertEquals("Подзадача пересекается с другими", response.body());
    }

    @Test
    public void updateNotFound() throws IOException, InterruptedException {
        manager.addEpic(new Epic("test_epic", "descr"));
        manager.addSubtask(new Subtask("testTask", "testTaskDescr", 1, Duration.ofMinutes(5), startTime1));
        String taskJson = """
                {
                        "name": "Выгулять собаку",
                        "description": "Погулять с Джеком 20 минут",
                        "status": "DONE",
                        "epicId": "1",
                        "id": 50,
                        "type": "SUBTASK",
                        "duration": "100",
                        "startTime": "2025-02-21 15:45:01",
                        "endTime": "2025-02-21 17:25:01"
                    }""";

        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder().uri(subtaskUrl).POST(HttpRequest.BodyPublishers.ofString(taskJson)).build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        Assertions.assertEquals(404, response.statusCode());
        Assertions.assertEquals("Не существует такой подзадачи", response.body());
    }

    @Test
    public void deleteNotFound() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        subtaskUrl = URI.create("http://localhost:8080/tasks/3");
        HttpRequest request = HttpRequest.newBuilder().uri(subtaskUrl).DELETE().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        Assertions.assertEquals(404, response.statusCode());
        Assertions.assertEquals("Подзадача не найдена", response.body());
    }

    // TODO: Доделать тест
    @Test
    public void EndpointNotFound() throws IOException, InterruptedException {}
}
