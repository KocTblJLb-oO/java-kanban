package ru.moysayt.steptraker.httpServer;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import ru.moysayt.steptraker.model.Task;
import ru.moysayt.steptraker.service.TaskManager;

import java.io.IOException;
import java.util.List;

public class PrioritizedHandler extends BaseHttpHandler {

    public PrioritizedHandler(TaskManager taskManager, Gson gson) {
        super(taskManager, gson);
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String method = exchange.getRequestMethod();

        if (method.equals("GET")) {
            getPrioritized(exchange);
        } else {
            sendNotFound(exchange, "Неизвестный HTTP-метод");
        }
    }

        /*
    ------------------------------------------------ ОБРАБОТКА ЗАПРОСОВ
    */

    // Обработка GET запросов
    public void getPrioritized(HttpExchange exchange) throws IOException {
        List<? extends Task> tasks;

        tasks = taskManager.getPrioritizedTasks();
        String response = gson.toJson(tasks);

        sendText(exchange, response); // Отправляем ответ
    }
}