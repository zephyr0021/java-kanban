import http.HttpTaskServer;
import http.json.JsonTaskBuilder;
import managers.TaskManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import statuses.StatusTask;
import tasks.Task;
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

public class HttpTaskTest {

    TaskManager manager = Managers.getDefault();
    HttpTaskServer server = new HttpTaskServer(manager);
    JsonTaskBuilder json = server.getJsonTaskBuilder();
    URI taskUrl = URI.create("http://localhost:8080/tasks");


    LocalDateTime startTime1 = LocalDateTime.of(LocalDate.of(2025, 2, 4),
            LocalTime.of(10, 0));
    LocalDateTime startTime2 = LocalDateTime.of(LocalDate.of(2025, 2, 5),
            LocalTime.of(10, 0));
    public HttpTaskTest() throws IOException {
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
    public void getTasks() throws IOException, InterruptedException {
        Task task = new Task("testTask", "testTaskDescr", Duration.ofMinutes(5), startTime1);
        Task task2 = new Task("testTask2", "testTaskDescr", Duration.ofMinutes(5), startTime2);

        manager.addTask(task);
        manager.addTask(task2);

        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder().uri(taskUrl).GET().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        Assertions.assertEquals(200, response.statusCode());
        String tasks = json.toJson(manager.getTasks());
        Assertions.assertEquals(tasks,response.body());
    }

    @Test
    public void getTask() throws IOException, InterruptedException {
        manager.addTask(new Task("testTask", "testTaskDescr", Duration.ofMinutes(5), startTime1));

        Task task = manager.getTask(1);
        taskUrl = URI.create("http://localhost:8080/tasks/" + task.getId());

        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder().uri(taskUrl).GET().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        Assertions.assertEquals(200, response.statusCode());

        Assertions.assertEquals(json.toJson(task), response.body());


    }

    @Test
    public void testAddTask() throws IOException, InterruptedException {
        String taskJson = """
                {
                        "name": "Выгулять собаку",
                        "description": "Погулять с Джеком 20 минут",
                        "duration": "100",
                        "startTime": "2025-02-21 15:45:01",
                        "endTime": "2025-02-21 17:25:01"
                    }""";

        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder().uri(taskUrl).POST(HttpRequest.BodyPublishers.ofString(taskJson)).build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        Assertions.assertEquals(201, response.statusCode());

        ArrayList<Task> tasks = manager.getTasks();

        Assertions.assertNotNull(tasks);
        Assertions.assertEquals(1, tasks.size());
        Assertions.assertEquals("Выгулять собаку", tasks.getFirst().getName());
    }

    @Test
    public void updateTask() throws IOException, InterruptedException {
        manager.addTask(new Task("testTask", "testTaskDescr", Duration.ofMinutes(5), startTime1));
        String taskJson = """
                {
                        "name": "Выгулять собаку",
                        "description": "Погулять с Джеком 20 минут",
                        "status": "DONE",
                        "id": 1,
                        "type": "TASK",
                        "duration": "100",
                        "startTime": "2025-02-21 15:45:01",
                        "endTime": "2025-02-21 17:25:01"
                    }""";

        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder().uri(taskUrl).POST(HttpRequest.BodyPublishers.ofString(taskJson)).build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        Task taskUpd = manager.getTask(1);
        Assertions.assertEquals(201, response.statusCode());
        Assertions.assertEquals("Выгулять собаку", taskUpd.getName());
        Assertions.assertEquals("Погулять с Джеком 20 минут", taskUpd.getDescription());
        Assertions.assertEquals(StatusTask.DONE, taskUpd.getStatus());
    }

    @Test
    public void deleteTask() throws IOException, InterruptedException {
        Task task = new Task("testTask", "testTaskDescr", Duration.ofMinutes(5), startTime1);
        manager.addTask(task);
        taskUrl = URI.create("http://localhost:8080/tasks/" + task.getId());
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder().uri(taskUrl).DELETE().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        Assertions.assertEquals(200, response.statusCode());

        ArrayList<Task> tasks = manager.getTasks();

        Assertions.assertTrue(tasks.isEmpty());
    }

    @Test
    public void getTaskNotFound() throws IOException, InterruptedException {
        manager.addTask(new Task("testTask", "testTaskDescr", Duration.ofMinutes(5), startTime1));
        taskUrl = URI.create("http://localhost:8080/tasks/3");

        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder().uri(taskUrl).GET().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        Assertions.assertEquals(404, response.statusCode());
        Assertions.assertEquals("Задача с указанным id не найдена", response.body());

    }

    @Test
    public void addTaskIntersection() throws IOException, InterruptedException {
        manager.addTask(new Task("testTask", "testTaskDescr", Duration.ofMinutes(5), startTime1));
        String taskJson = """
                {
                        "name": "Выгулять собаку",
                        "description": "Погулять с Джеком 20 минут",
                        "duration": "5",
                        "startTime": "2025-02-04 10:00:00"
                    }""";

        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder().uri(taskUrl).POST(HttpRequest.BodyPublishers.ofString(taskJson)).build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        Assertions.assertEquals(406, response.statusCode());
        Assertions.assertEquals("Задача пересекается с другими", response.body());
    }

    @Test
    public void updateNotFound() throws IOException, InterruptedException {
        manager.addTask(new Task("testTask", "testTaskDescr", Duration.ofMinutes(5), startTime1));
        String taskJson = """
                {
                        "name": "Выгулять собаку",
                        "description": "Погулять с Джеком 20 минут",
                        "status": "DONE",
                        "id": 50,
                        "type": "TASK",
                        "duration": "100",
                        "startTime": "2025-02-21 15:45:01",
                        "endTime": "2025-02-21 17:25:01"
                    }""";

        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder().uri(taskUrl).POST(HttpRequest.BodyPublishers.ofString(taskJson)).build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        Assertions.assertEquals(404, response.statusCode());
        Assertions.assertEquals("Не существует такой задачи", response.body());
    }

    @Test
    public void deleteNotFound() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        taskUrl = URI.create("http://localhost:8080/tasks/3");
        HttpRequest request = HttpRequest.newBuilder().uri(taskUrl).DELETE().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        Assertions.assertEquals(404, response.statusCode());
        Assertions.assertEquals("Задача не найдена", response.body());
    }

    // TODO: Доделать тест
    @Test
    public void EndpointNotFound() throws IOException, InterruptedException {}
}
