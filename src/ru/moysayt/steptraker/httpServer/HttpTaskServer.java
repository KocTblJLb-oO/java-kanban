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

    private final TaskManager taskManager;
    private final HttpServer httpServer;
    Gson gson;

    public HttpTaskServer(TaskManager taskManager) throws IOException {
        gson = HttpTaskServer.getGson();

        this.taskManager = taskManager;

        httpServer = HttpServer.create(new InetSocketAddress(8080), 0);
        httpServer.createContext("/tasks", new TaskHandler(taskManager, gson));
        httpServer.createContext("/subtasks", new SubtaskHandler(taskManager, gson));
        httpServer.createContext("/epics", new EpicHandler(taskManager, gson));
        httpServer.createContext("/history", new HistoryHandler(taskManager, gson));
        httpServer.createContext("/prioritized", new PrioritizedHandler(taskManager, gson));
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

    public static Gson getGson() {
        return new GsonBuilder()
                .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
                .registerTypeAdapter(Duration.class, new DurationAdapter())
                .create();
    }
}
