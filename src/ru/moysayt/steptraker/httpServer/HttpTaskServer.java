package ru.moysayt.steptraker.httpServer;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sun.net.httpserver.HttpServer;
import ru.moysayt.steptraker.httpServer.other.DurationAdapter;
import ru.moysayt.steptraker.httpServer.other.LocalDateTimeAdapter;
import ru.moysayt.steptraker.manager.Managers;
import ru.moysayt.steptraker.NeMain;
import ru.moysayt.steptraker.service.TaskManager;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.time.Duration;
import java.time.LocalDateTime;

public class HttpTaskServer {

    private static TaskManager taskManager;
    private static HttpServer httpServer;

    public HttpTaskServer(TaskManager taskManager) throws IOException {
        Gson gson = new GsonBuilder()
                .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
                .registerTypeAdapter(Duration.class, new DurationAdapter())
                .create();

        HttpTaskServer.taskManager = taskManager;

        httpServer = HttpServer.create(new InetSocketAddress(8080), 0);
        httpServer.createContext("/tasks", new TaskHandler(HttpTaskServer.taskManager, gson));
        httpServer.createContext("/subtasks", new SubtaskHandler(HttpTaskServer.taskManager, gson));
        httpServer.createContext("/epics", new EpicHandler(HttpTaskServer.taskManager, gson));
        httpServer.createContext("/history", new HistoryHandler(HttpTaskServer.taskManager, gson));
        httpServer.createContext("/prioritized", new PrioritizedHandler(HttpTaskServer.taskManager, gson));
    }

    public static void main(String[] args) throws IOException {
        HttpTaskServer httpTaskServer = new HttpTaskServer(Managers.getDefault());
        httpTaskServer.start();
        NeMain.generalTest(httpTaskServer); // Метод заполняет таскменеджер тестовыми данными
    }

    public void start() {
        httpServer.start();
    }

    public void stop() {
        httpServer.stop(0);
    }

    public TaskManager getTaskManager() {
        return taskManager;
    }
}
