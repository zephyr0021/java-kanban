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

    private void handleGetEpic(HttpExchange httpExchange, String path) {
        getTaskId(path)
                .map(taskId -> {
                    try {
                        return taskManager.getEpic(taskId);
                    } catch (NotFoundException e) {
                        try {
                            sendNotFound(httpExchange, e.getMessage());
                        } catch (IOException ex) {
                            throw new RuntimeException(ex);
                        }
                    }
                    return Optional.empty();
                })
                .map(jsonTaskBuilder::toJson)
                .ifPresentOrElse(
                        json -> {
                            try {
                                sendText(httpExchange, json);
                            } catch (IOException e) {
                                throw new RuntimeException(e);
                            }
                        },
                        () -> {
                            try {
                                sendEndpointNotFound(httpExchange);
                            } catch (IOException e) {
                                throw new RuntimeException(e);
                            }
                        }
                );
    }

    private void handleGetEpicSubtasks(HttpExchange httpExchange, String path) {
        getTaskId(path)
                .map(taskId -> {
                    try {
                        return taskManager.getEpicSubtasks(taskId);
                    } catch (NotFoundException e) {
                        try {
                            sendNotFound(httpExchange, e.getMessage());
                        } catch (IOException ex) {
                            throw new RuntimeException(ex);
                        }
                    }
                    return Optional.empty();
                })
                .map(jsonTaskBuilder::toJson)
                .ifPresentOrElse(
                        json -> {
                            try {
                                sendText(httpExchange, json);
                            } catch (IOException e) {
                                throw new RuntimeException(e);
                            }
                        },
                        () -> {
                            try {
                                sendEndpointNotFound(httpExchange);
                            } catch (IOException e) {
                                throw new RuntimeException(e);
                            }
                        }
                );
    }

    private void handleAddEpic(HttpExchange httpExchange, String requestBody) throws IOException {
        try {
            Epic epic = (jsonTaskBuilder.fromJson(requestBody, Epic.class));
            taskManager.addEpic(epic);
            sendTextUpdate(httpExchange, String.format("Эпик добавлен в список с id %d", epic.getId()));
        } catch (IntersectionException e) {
            sendHasInteractions(httpExchange, e.getMessage());
        } catch (NotFoundException e) {
            sendNotFound(httpExchange, e.getMessage());
        }
    }

    private void handleDeleteEpic(HttpExchange httpExchange, String path) {
        getTaskId(path).
                ifPresentOrElse(
                        taskId -> {
                            try {
                                taskManager.deleteEpic(taskId);
                                sendText(httpExchange, "Эпик успешно удален");
                            } catch (NotFoundException | IOException e) {
                                try {
                                    sendNotFound(httpExchange, e.getMessage());
                                } catch (IOException ex) {
                                    throw new RuntimeException(ex);
                                }
                            }

                        },
                        () -> {
                            try {
                                sendEndpointNotFound(httpExchange);
                            } catch (IOException e) {
                                throw new RuntimeException(e);
                            }
                        }
                );
    }

}
