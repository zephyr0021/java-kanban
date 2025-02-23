package http.handler;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import exceptions.IntersectionException;
import exceptions.NotFoundException;
import http.Endpoint;
import http.json.JsonTaskBuilder;
import managers.TaskManager;
import tasks.Task;

import java.io.IOException;
import java.util.Optional;

public class TaskHandler extends BaseHttpHandler implements HttpHandler {

    private final TaskManager taskManager;

    private final JsonTaskBuilder jsonTaskBuilder;

    public TaskHandler(TaskManager taskManager) {
        this.taskManager = taskManager;
        this.jsonTaskBuilder = new JsonTaskBuilder();
    }


    @Override
    public void handle(HttpExchange httpExchange) throws IOException {
        String path = httpExchange.getRequestURI().getPath();
        String requestBody = getRequestBody(httpExchange);
        Endpoint endpoint = Endpoint.getEndpoint(path, httpExchange.getRequestMethod(), requestBody);
        switch (endpoint) {
            case GET_TASKS:
                handleGetTasks(httpExchange);
                break;
            case GET_TASK:
                handleGetTask(httpExchange, path);
                break;
            case DELETE_TASK:
                handleDeleteTask(httpExchange, path);
                break;
            case CREATE_TASK:
                handleAddTask(httpExchange, requestBody);
                break;
            case UPDATE_TASK:
                handleUpdateTask(httpExchange, requestBody);
                break;
            default:
                sendEndpointNotFound(httpExchange);
        }
    }

    private void handleGetTasks(HttpExchange httpExchange) throws IOException {
        sendText(httpExchange, jsonTaskBuilder.toJson(taskManager.getTasks()));
    }

    private void handleGetTask(HttpExchange httpExchange, String path) throws IOException {
        Optional<Integer> taskId = getTaskId(path);
        if (taskId.isEmpty()) {
            sendEndpointNotFound(httpExchange);
            return;
        }
        try {
            sendText(httpExchange, jsonTaskBuilder.toJson(taskManager.getTask(taskId.get())));
        } catch (NotFoundException e) {
            sendNotFound(httpExchange, e.getMessage());
        }
    }

    private void handleAddTask(HttpExchange httpExchange, String requestBody) throws IOException {
        try {
            Task task = (jsonTaskBuilder.fromJson(requestBody, Task.class));
            taskManager.addTask(task);
            sendTextUpdate(httpExchange, String.format("Задача добавлена в список с id %d", task.getId()));
        } catch (IntersectionException e) {
            sendHasInteractions(httpExchange, e.getMessage());
        }
    }

    private void handleUpdateTask(HttpExchange httpExchange, String requestBody) throws IOException {
        try {
            Task task = (jsonTaskBuilder.fromJson(requestBody, Task.class));
            taskManager.updateTask(task);
            sendTextUpdate(httpExchange, String.format("Задача с id %d успешно обновлена", task.getId()));
        } catch (NotFoundException e) {
            sendNotFound(httpExchange, e.getMessage());
        }
    }

    private void handleDeleteTask(HttpExchange httpExchange, String path) throws IOException {
        Optional<Integer> taskId = getTaskId(path);
        if (taskId.isEmpty()) {
            sendEndpointNotFound(httpExchange);
            return;
        }
        try {
            taskManager.deleteTask(taskId.get());
            sendText(httpExchange, "Задача успешно удалена");
        } catch (NotFoundException e) {
            sendNotFound(httpExchange, e.getMessage());
        }
    }

}
