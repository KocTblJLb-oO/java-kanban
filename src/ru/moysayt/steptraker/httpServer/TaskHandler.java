package ru.moysayt.steptraker.httpServer;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import ru.moysayt.steptraker.model.Task;
import ru.moysayt.steptraker.service.TaskManager;
import ru.moysayt.steptraker.service.directory.ManagerSaveException;
import ru.moysayt.steptraker.service.directory.NotFoundException;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;

import static java.net.HttpURLConnection.HTTP_CREATED;
import static java.net.HttpURLConnection.HTTP_OK;

public class TaskHandler extends BaseHttpHandler {

    Gson gson = HttpTaskServer.getGson();

    public TaskHandler(TaskManager taskManager) {
        super(taskManager);
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String method = exchange.getRequestMethod();
        String path = exchange.getRequestURI().getPath();

        switch (method) {
            case "GET":
                getTask(exchange, path);
                break;
            case "POST":
                postTask(exchange, path);
            case "DELETE":
                deleteTask(exchange, path);
            default:
                sendNotFound(exchange, "Неизвестный HTTP-метод");
        }
    }

        /*
    ------------------------------------------------ ОБРАБОТКА ЗАПРОСОВ
    */

    // Обработка GET запросов
    public void getTask(HttpExchange exchange, String path) throws IOException {
        String[] splitPath = path.split("/");
        List<? extends Task> tasks;
        String response = "";

        if (splitPath.length == 2) {
            tasks = taskManager.getTasks();
            response = gson.toJson(tasks);
        } else if (splitPath.length == 3) {
            try {
                int taskId = Integer.parseInt(splitPath[2]);
                Task task = taskManager.getTaskByID(taskId);
                response = gson.toJson(task);
            } catch (NotFoundException e) {
                sendNotFound(exchange, e.getMessage());
            }
        } else {
            sendNotFound(exchange, "Эндпоинт не найден");
        }

        sendText(exchange, response); // Отправляем ответ
    }

    // Обработка POST запросов
    public void postTask(HttpExchange exchange, String path) throws IOException {
        String[] splitPath = path.split("/");
        String taskJson = new String(exchange.getRequestBody().readAllBytes(), StandardCharsets.UTF_8);

        try {
            if (splitPath.length == 2) {
                Task task = gson.fromJson(taskJson, Task.class);
                taskManager.createTask(task);
            } else if (splitPath.length == 3) {
                Task task = gson.fromJson(taskJson, Task.class);
                taskManager.updateTask(task);
            } else {
                sendNotFound(exchange, "Эндпоинт не найден");
            }
        } catch (ManagerSaveException e) {
            sendHasInteractions(exchange, e.getMessage());
        }
        sendOk(exchange, HTTP_CREATED); // Отправляем ответ
    }

    // Обработка DELETE запросов
    public void deleteTask(HttpExchange exchange, String path) throws IOException {
        String[] splitPath = path.split("/");

        try {
            if (splitPath.length == 3) {
                try {
                    int taskId = Integer.parseInt(splitPath[2]);
                    taskManager.deleteTask(taskId);
                } catch (NotFoundException e) {
                    sendNotFound(exchange, e.getMessage());
                }
            } else {
                sendNotFound(exchange, "Эндпоинт не найден");
            }
            sendOk(exchange, HTTP_OK); // Отправляем ответ
        } catch (ManagerSaveException e) {
            sendError(exchange, e.getMessage());
        }
    }
}
