package http;

import com.sun.net.httpserver.HttpServer;
import http.handler.TaskHandler;
import managers.TaskManager;
import tasks.Task;
import util.Managers;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.time.Duration;
import java.time.LocalDateTime;

public class HttpTaskServer {
    private static final int PORT = 8080;
    private final HttpServer server;
    private final TaskManager taskManager;

    public HttpTaskServer(TaskManager taskManager) throws IOException {
        this.server = HttpServer.create(new InetSocketAddress(PORT), 0);
        this.taskManager = taskManager;
    }

    public void start() {
        server.createContext("/tasks", new TaskHandler(taskManager));
        server.start();
    }

    public static void main(String[] args) throws IOException {
        TaskManager manager = Managers.getDefault();
        manager.addTask(new Task("Выгулять собаку", "Погулять с Джеком 20 минут", Duration.ofMinutes(100), LocalDateTime.now()));
        manager.addTask(new Task("Выгулять собаку2", "Погулять с Джеком 20 минут2", Duration.ofMinutes(100), LocalDateTime.of(2025, 6, 2, 10, 10)));
        HttpTaskServer server = new HttpTaskServer(manager);
        server.start();
    }
}

