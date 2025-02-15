package ru.moysayt.steptraker.httpServer;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import ru.moysayt.steptraker.model.Subtask;
import ru.moysayt.steptraker.model.Task;
import ru.moysayt.steptraker.service.TaskManager;
import ru.moysayt.steptraker.service.directory.ManagerSaveException;
import ru.moysayt.steptraker.service.directory.NotFoundException;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;

import static java.net.HttpURLConnection.HTTP_CREATED;
import static java.net.HttpURLConnection.HTTP_OK;

public class SubtaskHandler extends BaseHttpHandler {

    Gson gson = HttpTaskServer.getGson();

    public SubtaskHandler(TaskManager taskManager) {
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
            tasks = taskManager.getSubtasks();
            response = gson.toJson(tasks);
        } else if (splitPath.length == 3) {
            try {
                int subtaskId = Integer.parseInt(splitPath[2]);
                Task subtask = taskManager.getSubtaskByID(subtaskId);
                response = gson.toJson(subtask);
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
                Subtask task = gson.fromJson(taskJson, Subtask.class);
                taskManager.createSubtask(task.getParentId(), task);
            } else if (splitPath.length == 3) {
                Subtask task = gson.fromJson(taskJson, Subtask.class);
                taskManager.updateSubtask(task);
            } else {
                sendNotFound(exchange, "Эндпоинт не найден");
            }
            sendOk(exchange, HTTP_CREATED); // Отправляем ответ
        } catch (ManagerSaveException e) {
            sendError(exchange, e.getMessage());
        }
    }

    // Обработка DELETE запросов
    public void deleteTask(HttpExchange exchange, String path) throws IOException {
        String[] splitPath = path.split("/");

        try {
            if (splitPath.length == 3) {
                try {
                    int taskId = Integer.parseInt(splitPath[2]);
                    taskManager.deleteSubtask(taskId);
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