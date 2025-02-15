package ru.moysayt.steptraker.httpServer;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import ru.moysayt.steptraker.service.TaskManager;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

import static java.net.HttpURLConnection.*;

public abstract class BaseHttpHandler implements HttpHandler {

    TaskManager taskManager;

    public BaseHttpHandler(TaskManager taskManager) {
        this.taskManager = taskManager;

    }

    // Отправляет ответ при успешном поиске
    protected void sendText(HttpExchange h, String text) throws IOException {
        byte[] resp = text.getBytes(StandardCharsets.UTF_8);
        h.getResponseHeaders().add("Content-Type", "application/json;charset=utf-8");
        h.sendResponseHeaders(HTTP_OK, resp.length);
        h.getResponseBody().write(resp);
        h.close();
    }

    // Успех без текста
    protected void sendOk(HttpExchange h, int code) throws IOException {
        h.sendResponseHeaders(code, 0);
        h.close();
    }

    // Отправляет ответ если не найдено
    protected void sendNotFound(HttpExchange h, String text) throws IOException {
        byte[] resp = text.getBytes(StandardCharsets.UTF_8);
        h.getResponseHeaders().add("Content-Type", "application/json;charset=utf-8");
        h.sendResponseHeaders(HTTP_NOT_FOUND, resp.length);
        h.getResponseBody().write(resp);
        h.close();
    }

    // Отправляет ответ если задачи пересекаются по времени
    protected void sendHasInteractions(HttpExchange h, String text) throws IOException {
        byte[] resp = text.getBytes(StandardCharsets.UTF_8);
        h.getResponseHeaders().add("Content-Type", "application/json;charset=utf-8");
        h.sendResponseHeaders(HTTP_NOT_ACCEPTABLE, resp.length);
        h.getResponseBody().write(resp);
        h.close();
    }

    // Отправляет ответ при ошибке сохранения файла
    protected void sendError(HttpExchange h, String text) throws IOException {
        byte[] resp = text.getBytes(StandardCharsets.UTF_8);
        h.getResponseHeaders().add("Content-Type", "application/json;charset=utf-8");
        h.sendResponseHeaders(HTTP_INTERNAL_ERROR, resp.length);
        h.getResponseBody().write(resp);
        h.close();
    }
}