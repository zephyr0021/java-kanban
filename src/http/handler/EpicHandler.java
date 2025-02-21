package http.handler;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import exceptions.IntersectionException;
import exceptions.NotFoundException;
import http.Endpoint;
import http.json.JsonTaskBuilder;
import managers.TaskManager;
import tasks.Epic;

import java.io.IOException;
import java.util.Optional;

public class EpicHandler extends BaseHttpHandler implements HttpHandler {

    private final TaskManager taskManager;

    private final JsonTaskBuilder jsonTaskBuilder;

    public EpicHandler(TaskManager taskManager) {
        this.taskManager = taskManager;
        this.jsonTaskBuilder = new JsonTaskBuilder();
    }


    @Override
    public void handle(HttpExchange httpExchange) throws IOException {
        String path = httpExchange.getRequestURI().getPath();
        String requestBody = getRequestBody(httpExchange);
        Endpoint endpoint = Endpoint.getEndpoint(path, httpExchange.getRequestMethod(), requestBody);
        switch (endpoint) {
            case GET_EPICS:
                handleGetEpics(httpExchange);
                break;
            case GET_EPIC:
                handleGetEpic(httpExchange, path);
                break;
            case GET_EPIC_SUBTASKS:
                handleGetEpicSubtasks(httpExchange, path);
                break;
            case DELETE_EPIC:
                handleDeleteEpic(httpExchange, path);
                break;
            case CREATE_EPIC:
                handleAddEpic(httpExchange, requestBody);
                break;
            default:
                sendEndpointNotFound(httpExchange);
        }
    }

    private void handleGetEpics(HttpExchange httpExchange) throws IOException {
        sendText(httpExchange, jsonTaskBuilder.toJson(taskManager.getEpics()));
    }

    private void handleGetEpic(HttpExchange httpExchange, String path) throws IOException {
        Optional<Integer> taskId = getTaskId(path);
        if (taskId.isEmpty()) {
            sendEndpointNotFound(httpExchange);
            return;
        }
        try {
            sendText(httpExchange, jsonTaskBuilder.toJson(taskManager.getEpic(taskId.get())));
        } catch (NotFoundException e) {
            sendNotFound(httpExchange, e.getMessage());
        }
    }

    private void handleGetEpicSubtasks(HttpExchange httpExchange, String path) throws IOException {
        Optional<Integer> taskId = getTaskId(path);
        if (taskId.isEmpty()) {
            sendEndpointNotFound(httpExchange);
            return;
        }
        try {
            sendText(httpExchange, jsonTaskBuilder.toJson(taskManager.getEpicSubtasks(taskId.get())));
        } catch (NotFoundException e) {
            sendNotFound(httpExchange, e.getMessage());
        }
    }

    private void handleAddEpic(HttpExchange httpExchange, String requestBody) throws IOException {
        try {
            Epic epic = (jsonTaskBuilder.fromJson(requestBody, Epic.class));
            taskManager.addEpic(epic);
            sendText(httpExchange, String.format("Эпик добавлен в список с id %d", epic.getId()));
        } catch (IntersectionException e) {
            sendHasInteractions(httpExchange, e.getMessage());
        } catch (NotFoundException e) {
            sendNotFound(httpExchange, e.getMessage());
        }
    }

    private void handleDeleteEpic(HttpExchange httpExchange, String path) throws IOException {
        Optional<Integer> taskId = getTaskId(path);
        if (taskId.isEmpty()) {
            sendEndpointNotFound(httpExchange);
            return;
        }
        try {
            taskManager.deleteEpic(taskId.get());
            sendText(httpExchange, "Эпик успешно удален");
        } catch (NotFoundException e) {
            sendNotFound(httpExchange, e.getMessage());
        }
    }

}
