package http.handler;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import exceptions.IntersectionException;
import exceptions.NotFoundException;
import http.Endpoint;
import http.json.JsonTaskBuilder;
import managers.TaskManager;
import tasks.Subtask;

import java.io.IOException;
import java.util.Optional;

public class SubtaskHandler extends BaseHttpHandler implements HttpHandler {

    private final TaskManager taskManager;

    private final JsonTaskBuilder jsonTaskBuilder;

    public SubtaskHandler(TaskManager taskManager) {
        this.taskManager = taskManager;
        this.jsonTaskBuilder = new JsonTaskBuilder();
    }


    @Override
    public void handle(HttpExchange httpExchange) throws IOException {
        String path = httpExchange.getRequestURI().getPath();
        String requestBody = getRequestBody(httpExchange);
        Endpoint endpoint = Endpoint.getEndpoint(path, httpExchange.getRequestMethod(), requestBody);
        switch (endpoint) {
            case GET_SUBTASKS:
                handleGetSubtasks(httpExchange);
                break;
            case GET_SUBTASK:
                handleGetSubtask(httpExchange, path);
                break;
            case DELETE_SUBTASK:
                handleDeleteSubtask(httpExchange, path);
                break;
            case CREATE_SUBTASK:
                handleAddSubtask(httpExchange, requestBody);
                break;
            case UPDATE_SUBTASK:
                handleUpdateSubtask(httpExchange, requestBody);
                break;
            default:
                sendEndpointNotFound(httpExchange);
        }
    }

    private void handleGetSubtasks(HttpExchange httpExchange) throws IOException {
        sendText(httpExchange, jsonTaskBuilder.toJson(taskManager.getSubtasks()));
    }

    private void handleGetSubtask(HttpExchange httpExchange, String path) throws IOException {
        Optional<Integer> taskId = getTaskId(path);
        if (taskId.isEmpty()) {
            sendEndpointNotFound(httpExchange);
            return;
        }
        try {
            sendText(httpExchange, jsonTaskBuilder.toJson(taskManager.getSubtask(taskId.get())));
        } catch (NotFoundException e) {
            sendNotFound(httpExchange, e.getMessage());
        }
    }

    private void handleAddSubtask(HttpExchange httpExchange, String requestBody) throws IOException {
        try {
            Subtask subtask = (jsonTaskBuilder.fromJson(requestBody, Subtask.class));
            taskManager.addSubtask(subtask);
            sendTextUpdate(httpExchange, String.format("Подзадача добавлена в список с id %d", subtask.getId()));
        } catch (IntersectionException e) {
            sendHasInteractions(httpExchange, e.getMessage());
        } catch (NotFoundException e) {
            sendNotFound(httpExchange, e.getMessage());
        }
    }

    private void handleUpdateSubtask(HttpExchange httpExchange, String requestBody) throws IOException {
        try {
            Subtask subtask = (jsonTaskBuilder.fromJson(requestBody, Subtask.class));
            taskManager.updateSubtask(subtask);
            sendTextUpdate(httpExchange, String.format("Подзадача с id %d успешно обновлена", subtask.getId()));
        } catch (NotFoundException e) {
            sendNotFound(httpExchange, e.getMessage());
        }
    }

    private void handleDeleteSubtask(HttpExchange httpExchange, String path) throws IOException {
        Optional<Integer> taskId = getTaskId(path);
        if (taskId.isEmpty()) {
            sendEndpointNotFound(httpExchange);
            return;
        }
        try {
            taskManager.deleteSubtask(taskId.get());
            sendText(httpExchange, "Подзадача успешно удалена");
        } catch (NotFoundException e) {
            sendNotFound(httpExchange, e.getMessage());
        }
    }

}
