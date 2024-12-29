package ru.moysayt.steptraker;

import ru.moysayt.steptraker.model.Epic;
import ru.moysayt.steptraker.model.StatusOfTask;
import ru.moysayt.steptraker.model.Subtask;
import ru.moysayt.steptraker.model.Task;
import ru.moysayt.steptraker.service.FileBackedTaskManager;
import ru.moysayt.steptraker.service.InMemoryTaskManager;
import ru.moysayt.steptraker.service.history.InMemoryHistoryManager;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class Main {

    public static void main(String[] args) throws IOException {
        System.out.println("Поехали!");
        InMemoryTaskManager inMemoryTaskManager = new InMemoryTaskManager();


        // Создаём две задачи
        Task t1 = new Task("Заголовок 1", "Описание задачи 1", StatusOfTask.NEW);
        Task t2 = new Task("Заголовок 2", "Описание задачи 2", StatusOfTask.NEW);
        inMemoryTaskManager.createTask(t1);
        inMemoryTaskManager.createTask(t2);

        // Создаём эпик с двумя подзадачами
        Epic e1 = new Epic("Эпик 1", "Описание эпика 1", StatusOfTask.NEW);
        inMemoryTaskManager.createEpic(e1);
        Subtask s1 = new Subtask(3, "Подзадача 1", "Описание подзадачи 1", StatusOfTask.NEW);
        Subtask s2 = new Subtask(3, "Подзадача 2", "Описание подзадачи 2", StatusOfTask.NEW);
        inMemoryTaskManager.createSubtask(3, s1);
        inMemoryTaskManager.createSubtask(3, s2);

        // Создаём эпик с одной подзадачей
        Epic e2 = new Epic("Эпик 2. С одной подзадачей", "Описание эпика 2. " + "С одной подзадачей",
                StatusOfTask.NEW);
        inMemoryTaskManager.createEpic(e2);
        Subtask s3 = new Subtask(6, "Подзадача 3. С эпиком с 1 зад.",
                "Описание подзадачи 3. С эпиком с 1 зад.", StatusOfTask.NEW);
        inMemoryTaskManager.createSubtask(6, s3);

        showAllTask(inMemoryTaskManager);
        showAllEpic(inMemoryTaskManager);
        showAllSubtask(inMemoryTaskManager);

        // Меняем статус задачи и подзадачи
        System.out.println("""
                ------------------------------------------------
                ИЗМЕНЕНИЕ СТАТУСА
                ------------------------------------------------""");
        Task t1newStatus = inMemoryTaskManager.getTaskByID(1);
        t1newStatus.setStatus(StatusOfTask.IN_PROGRESS);
        inMemoryTaskManager.updateTask(t1newStatus);
        Subtask s3NewStatus = inMemoryTaskManager.getSubtaskByID(7);
        s3NewStatus.setStatus(StatusOfTask.DONE);
        inMemoryTaskManager.updateSubtask(s3NewStatus);
        System.out.println(inMemoryTaskManager.getTaskByID(1));
        // Печатаем вместе с родительским эпиком
        System.out.println(inMemoryTaskManager.getSubtaskByID(7));
        System.out.println(inMemoryTaskManager.getEpicByID(6));

        // Удалим одну задачу и эпик с двумя подзадачами
        System.out.println("""
                ------------------------------------------------
                УДАЛЕНИЕ
                ------------------------------------------------""");
        inMemoryTaskManager.deleteTask(2);
        inMemoryTaskManager.deleteEpic(3);

        showAllTask(inMemoryTaskManager);
        showAllEpic(inMemoryTaskManager);
        showAllSubtask(inMemoryTaskManager);

        System.out.println("""
                ------------------------------------------------
                ИСТОРИЯ
                ------------------------------------------------""");
        showHistory((InMemoryHistoryManager<Task>) inMemoryTaskManager.historyManager);

        System.out.println("""
                ------------------------------------------------
                ------------------------------------------------
                ------------------------------------------------
                ФАЙЛОВЫЙ МЕНЕДЖЕР
                ------------------------------------------------""");


        // Создаём временный файл и получаем его путь
        File file = File.createTempFile("fileForTask-", ".csv");
        System.out.println(file.toPath());

        // Создаём файловый менеджер
        FileBackedTaskManager fileBackedTaskManager = new FileBackedTaskManager(file);

        // Создаём задачи
        fileBackedTaskManager.createTask(t1);
        fileBackedTaskManager.createTask(t2);

        // Создаём эпик и 2 подзадачи
        fileBackedTaskManager.createEpic(e1);
        fileBackedTaskManager.createSubtask(fileBackedTaskManager.getEpics().getFirst().getId(), s1);
        fileBackedTaskManager.createSubtask(fileBackedTaskManager.getEpics().getFirst().getId(), s2);

        // Эпик и подзадача с другим статусом
        fileBackedTaskManager.createEpic(e2);
        fileBackedTaskManager.createSubtask(e2.getId(), s3NewStatus);

        // Удалим такс с id 1
        fileBackedTaskManager.deleteTask(1);

        System.out.println("""
                ------------------------------------------------
                Дополнительное задание. Реализуем пользовательский сценарий
                ------------------------------------------------""");

        FileBackedTaskManager fileBackedTaskManager2 = FileBackedTaskManager.loadFromFile(file);

        for (Task task : fileBackedTaskManager2.getTasks()) {
            System.out.println(task);
        }
        for (Epic epic : fileBackedTaskManager2.getEpics()) {
            System.out.println(epic);
        }
        for (Subtask subtask : fileBackedTaskManager2.getSubtasks()) {
            System.out.println(subtask);
        }
    }

    public static void showAllTask(InMemoryTaskManager inMemoryTaskManager) {
        for (Task task : inMemoryTaskManager.getTasks()) {
            System.out.println(task);
        }
    }

    public static void showAllEpic(InMemoryTaskManager inMemoryTaskManager) {
        for (Epic epic : inMemoryTaskManager.getEpics()) {
            System.out.println(epic);
        }
    }

    public static void showAllSubtask(InMemoryTaskManager inMemoryTaskManager) {
        for (Subtask subtask : inMemoryTaskManager.getSubtasks()) {
            System.out.println(subtask);
        }
    }

    public static void showHistory(InMemoryHistoryManager<Task> historyManager) {
        ArrayList<Task> history = new ArrayList<>(historyManager.getHistory());
        for (Task task : history) {
            System.out.println(task);
        }
    }
}
