package http.handler;

import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

public class BaseHttpHandler {

    protected void sendText(HttpExchange httpExchange, String text) throws IOException {
        byte[] response = text.getBytes(StandardCharsets.UTF_8);
        httpExchange.getResponseHeaders().set("Content-Type", "application/json");
        httpExchange.sendResponseHeaders(200, response.length);
        try (OutputStream os = httpExchange.getResponseBody()) {
            os.write(response);
        }
    }

    protected void sendNotFound(HttpExchange httpExchange, String text) throws IOException {
        byte[] response = text.getBytes(StandardCharsets.UTF_8);
        httpExchange.getResponseHeaders().set("Content-Type", "application/json");
        httpExchange.sendResponseHeaders(404, response.length);
        try (OutputStream os = httpExchange.getResponseBody()) {
            os.write(response);
        }
    }

    protected void sendHasInteractions(HttpExchange httpExchange, String text) throws IOException {
        byte[] response = text.getBytes(StandardCharsets.UTF_8);
        httpExchange.getResponseHeaders().set("Content-Type", "application/json");
        httpExchange.sendResponseHeaders(406, response.length);
        try (OutputStream os = httpExchange.getResponseBody()) {
            os.write(response);
        }
    }

    protected void sendEndpointNotFound(HttpExchange httpExchange) throws IOException {
        httpExchange.getResponseHeaders().set("Content-Type", "application/json");
        httpExchange.sendResponseHeaders(404, 0);
        try (OutputStream os = httpExchange.getResponseBody()) {
            os.write("Такого эндпоинта не существует".getBytes(StandardCharsets.UTF_8));
        }
    }

    protected String getRequestBody(HttpExchange httpExchange) throws IOException {
        try (InputStream is = httpExchange.getRequestBody()) {
            return new String(is.readAllBytes(), StandardCharsets.UTF_8);
        }
    }

}
