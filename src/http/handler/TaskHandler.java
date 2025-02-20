// TODO: Добавить проверку на NULL (описано в доке)
package http.handler;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import http.Endpoint;
import managers.TaskManager;

import java.io.IOException;
import java.util.Optional;

public class TaskHandler extends BaseHttpHandler implements HttpHandler {

    private final TaskManager taskManager;

    public TaskHandler(TaskManager taskManager) {
        this.taskManager = taskManager;
    }


    @Override
    public void handle(HttpExchange httpExchange) throws IOException {
        String path = httpExchange.getRequestURI().getPath();
        Endpoint endpoint = Endpoint.getEndpoint(path, httpExchange.getRequestMethod());

        switch (endpoint) {
            case GET_TASKS:
                handleGetTasks(httpExchange);
                break;
            case GET_TASK:
                handleGetTask(httpExchange, path);
                break;
            default:
                sendEndpointNotFound(httpExchange);
        }
    }

    private void handleGetTasks(HttpExchange httpExchange) throws IOException {
        sendText(httpExchange, taskManager.getTasks().toString());
    }

    private void handleGetTask(HttpExchange httpExchange, String path) throws IOException {
        Optional<Integer> taskId = getTaskId(path);
        if (taskId.isEmpty()) {
            sendEndpointNotFound(httpExchange);
            return;
        }
        sendText(httpExchange, taskManager.getTask(taskId.get()).toString());
    }

    private Optional<Integer> getTaskId(String path) {
        String[] parts = path.split("/");
        try {
            return Optional.of(Integer.parseInt(parts[2]));
        } catch (NumberFormatException e) {
            return Optional.empty();
        }
    }

}
