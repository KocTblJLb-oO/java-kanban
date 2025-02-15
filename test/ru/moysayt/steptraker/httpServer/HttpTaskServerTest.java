package ru.moysayt.steptraker.httpServer;

import com.google.gson.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.moysayt.steptraker.httpServer.other.DurationAdapter;
import ru.moysayt.steptraker.httpServer.other.LocalDateTimeAdapter;
import ru.moysayt.steptraker.model.Epic;
import ru.moysayt.steptraker.model.StatusOfTask;
import ru.moysayt.steptraker.model.Subtask;
import ru.moysayt.steptraker.model.Task;
import ru.moysayt.steptraker.service.InMemoryTaskManager;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class HttpTaskServerTest {
    HttpTaskServer httpTaskServer;
    InMemoryTaskManager inMemoryTaskManager;
    Gson gson;

    Task task1;
    Task task2;
    Task task3;
    Task task4;
    Epic epic;
    Subtask subtask;
    LocalDateTime localDateTime;
    Duration duration;

    @BeforeEach
    public void startTest() throws IOException {
        inMemoryTaskManager = new InMemoryTaskManager();
        httpTaskServer = new HttpTaskServer(inMemoryTaskManager);
        gson = new GsonBuilder()
                .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
                .registerTypeAdapter(Duration.class, new DurationAdapter())
                .create();

        localDateTime = LocalDateTime.now();
        duration = Duration.ofMinutes(10);
        task1 = new Task("title test task", "testTask", StatusOfTask.NEW);
        task2 = new Task("", "testTask", StatusOfTask.NEW);
        task3 = new Task("", "testTask", StatusOfTask.NEW, localDateTime, duration);
        task4 = new Task("", "testTask", StatusOfTask.NEW, localDateTime.plus(Duration.ofMinutes(5)), duration);
        epic = new Epic("title epic task", "testEpic", StatusOfTask.NEW);
        subtask = new Subtask(1, "", "testSubtask", StatusOfTask.NEW);

        httpTaskServer.start();
    }

    @AfterEach
    public void stopTest() {
        httpTaskServer.stop();
    }

    /*
    ------------------------------------------------ Задачи
     */

    // Добавление задачи
    @Test
    public void testAddTask() throws IOException, InterruptedException {
        String taskJson = gson.toJson(task1);
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks");
        HttpRequest request = HttpRequest
                .newBuilder()
                .uri(url)
                .POST(HttpRequest.BodyPublishers.ofString(taskJson))
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(201, response.statusCode(), "Код ответа сервера не совпадает");
        assertEquals(1, inMemoryTaskManager.getTasks().size(), "Задача не добавилась в список");
    }

    // Удаление задачи
    @Test
    public void testDeleteTask() throws IOException, InterruptedException {
        String taskJson = gson.toJson(task1);
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks");
        HttpRequest requestAdd = HttpRequest
                .newBuilder()
                .uri(url)
                .POST(HttpRequest.BodyPublishers.ofString(taskJson))
                .build();
        client.send(requestAdd, HttpResponse.BodyHandlers.ofString());

        HttpRequest requestDelete = HttpRequest
                .newBuilder()
                .uri(URI.create("http://localhost:8080/tasks/1"))
                .DELETE()
                .build();

        HttpResponse<String> response = client.send(requestDelete, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, response.statusCode(), "Код ответа сервера не совпадает");
        assertEquals(0, inMemoryTaskManager.getTasks().size(), "Задача не удалилась из списка");
    }

    // Получение задачи
    @Test
    public void testGetTask() throws IOException, InterruptedException {
        String taskJson = gson.toJson(task1);
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks");
        HttpRequest requestAdd = HttpRequest
                .newBuilder()
                .uri(url)
                .POST(HttpRequest.BodyPublishers.ofString(taskJson))
                .build();
        client.send(requestAdd, HttpResponse.BodyHandlers.ofString());

        HttpRequest requestDelete = HttpRequest
                .newBuilder()
                .uri(URI.create("http://localhost:8080/tasks/1"))
                .GET()
                .build();

        HttpResponse<String> response = client.send(requestDelete, HttpResponse.BodyHandlers.ofString());
        JsonElement jsonElement = JsonParser.parseString(response.body());
        JsonObject jsonObject = jsonElement.getAsJsonObject();

        assertEquals(200, response.statusCode(), "Код ответа сервера не совпадает");
        assertEquals("title test task", jsonObject.get("title").getAsString(), "Не удалось получить задачу");
    }

    /*
    ------------------------------------------------ Эпики и Сабтаски
     */

    // Добавление сабтаска
    @Test
    public void testAddSubtask() throws IOException, InterruptedException {
        // Добавляем эпик
        String taskJson = gson.toJson(epic);
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/epics");
        HttpRequest request = HttpRequest
                .newBuilder()
                .uri(url)
                .POST(HttpRequest.BodyPublishers.ofString(taskJson))
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(201, response.statusCode(), "Код ответа сервера не совпадает при добавлении эпика");
        assertEquals(1, inMemoryTaskManager.getEpics().size(), "Эпик не добавился в список");

        // Добавляем сабтаск
        String taskJson2 = gson.toJson(subtask);
        URI url2 = URI.create("http://localhost:8080/subtasks");
        HttpRequest request2 = HttpRequest
                .newBuilder()
                .uri(url2)
                .POST(HttpRequest.BodyPublishers.ofString(taskJson2))
                .build();
        HttpResponse<String> response2 = client.send(request2, HttpResponse.BodyHandlers.ofString());

        assertEquals(201, response2.statusCode(), "Код ответа сервера не совпадает при добавлении сабтаска");
        assertEquals(1, inMemoryTaskManager.getSubtasks().size(), "Сабтаск не добавилась в список");
    }

    // Удаление задачи
    @Test
    public void testDeleteSubtask() throws IOException, InterruptedException {
        // Добавляем эпик и сабтаск
        String taskJson = gson.toJson(epic);
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/epics");
        HttpRequest request = HttpRequest
                .newBuilder()
                .uri(url)
                .POST(HttpRequest.BodyPublishers.ofString(taskJson))
                .build();
        client.send(request, HttpResponse.BodyHandlers.ofString());

        String taskJson2 = gson.toJson(subtask);
        URI url2 = URI.create("http://localhost:8080/subtasks");
        HttpRequest request2 = HttpRequest
                .newBuilder()
                .uri(url2)
                .POST(HttpRequest.BodyPublishers.ofString(taskJson2))
                .build();
        client.send(request2, HttpResponse.BodyHandlers.ofString());

        // Удаляем эпик
        HttpRequest requestDelete = HttpRequest
                .newBuilder()
                .uri(URI.create("http://localhost:8080/epics/1"))
                .DELETE()
                .build();

        HttpResponse<String> response = client.send(requestDelete, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, response.statusCode(), "Код ответа при удалении эпика сервера не совпадает");
        // Проверяем автоматическое удаление сабтасков при удалении эпика
        assertEquals(0, inMemoryTaskManager.getSubtasks().size(), "Сабтаск не удалился из списка");
    }

    // Получение эпика
    @Test
    public void testGetSubtask() throws IOException, InterruptedException {
        String taskJson = gson.toJson(epic);
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/epics");
        HttpRequest requestAdd = HttpRequest
                .newBuilder()
                .uri(url)
                .POST(HttpRequest.BodyPublishers.ofString(taskJson))
                .build();
        client.send(requestAdd, HttpResponse.BodyHandlers.ofString());

        HttpRequest requestDelete = HttpRequest
                .newBuilder()
                .uri(URI.create("http://localhost:8080/epics/1"))
                .GET()
                .build();

        HttpResponse<String> response = client.send(requestDelete, HttpResponse.BodyHandlers.ofString());
        JsonElement jsonElement = JsonParser.parseString(response.body());
        JsonObject jsonObject = jsonElement.getAsJsonObject();

        assertEquals(200, response.statusCode(), "Код ответа сервера не совпадает");
        assertEquals("title epic task", jsonObject.get("title").getAsString(), "Не удалось получить задачу");
    }
}
