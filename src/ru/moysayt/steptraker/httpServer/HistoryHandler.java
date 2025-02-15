package ru.moysayt.steptraker.httpServer;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import ru.moysayt.steptraker.model.Task;
import ru.moysayt.steptraker.service.TaskManager;

import java.io.IOException;
import java.util.List;

public class HistoryHandler extends BaseHttpHandler {

    Gson gson = HttpTaskServer.getGson();

    public HistoryHandler(TaskManager taskManager) {
        super(taskManager);
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String method = exchange.getRequestMethod();

        if (method.equals("GET")) {
            getHistory(exchange);
        } else {
            sendNotFound(exchange, "Неизвестный HTTP-метод");
        }
    }

        /*
    ------------------------------------------------ ОБРАБОТКА ЗАПРОСОВ
    */

    // Обработка GET запросов
    public void getHistory(HttpExchange exchange) throws IOException {
        List<? extends Task> tasks;
        String response;

        tasks = taskManager.getPrioritizedTasks();
        response = gson.toJson(tasks);

        sendText(exchange, response); // Отправляем ответ
    }
}